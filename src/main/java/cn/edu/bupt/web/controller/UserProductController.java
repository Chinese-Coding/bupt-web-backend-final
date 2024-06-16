package cn.edu.bupt.web.controller;

import cn.edu.bupt.web.common.R;
import cn.edu.bupt.web.entity.Product;
import cn.edu.bupt.web.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/Shop")
public class UserProductController {
    @Resource
    private ProductService productService;

    private final RedisTemplate<String, String> redisTemplate;

    public UserProductController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/queryAll")
    public R<List<Product>> queryAll(@RequestParam(value = "field", defaultValue = "id") String field,
                                     @RequestParam(value = "isDesc", defaultValue = "false") boolean isDesc) {

        var cacheKey = "products:" + field + ":" + isDesc;
        var cachedProducts = redisTemplate.opsForValue().get(cacheKey);

        if (cachedProducts != null) {
            var products = parseProductsFromJson(cachedProducts);
            return R.success(products);
        }

        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        switch (field) {
            case "price":
                if (isDesc)
                    lqw.orderByDesc(Product::getPrice);
                else
                    lqw.orderByAsc(Product::getPrice);
                break;
            case "name":
                if (isDesc)
                    lqw.orderByDesc(Product::getName);
                else
                    lqw.orderByAsc(Product::getName);
                break;
            case "comment_count":
                if (isDesc)
                    lqw.orderByDesc(Product::getCommentCount);
                else
                    lqw.orderByAsc(Product::getCommentCount);
                break;
            default:
                if (isDesc)
                    lqw.orderByDesc(Product::getId);
                else
                    lqw.orderByAsc(Product::getId);
                break;
        }

        var products = productService.list(lqw);

        // 将查询结果转换为JSON字符串，并存入Redis缓存，设置缓存时间为1小时
        var productsJson = convertProductsToJson(products);
        redisTemplate.opsForValue().set(cacheKey, productsJson, 1, TimeUnit.HOURS);

        return R.success(products);
    }

    private List<Product> parseProductsFromJson(String json) {
        // 实现从JSON字符串解析为Product列表的逻辑
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertProductsToJson(List<Product> products) {
        // 实现将Product列表转换为JSON字符串的逻辑
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.writeValueAsString(products);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/search")
    public R<List<Product>> search(@RequestParam(value = "name", defaultValue = "") String name,
                                   @RequestParam(value = "category", defaultValue = "all") String category) {
        // 构建 Redis 缓存键
        var cacheKey = "products:search:" + name + ":" + category;
        var cachedProducts = redisTemplate.opsForValue().get(cacheKey);

        if (cachedProducts != null) {
            var products = parseProductsFromJson(cachedProducts);
            return R.success(products);
        }

        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        if (!name.isEmpty())
            lqw.like(Product::getName, name);

        if (!category.equals("all"))
            lqw.eq(Product::getCategory, category);

        // lqw.orderByAsc(Product::getId);
        var products = productService.list(lqw);
        convertProductsToJson(products);
        return R.success(products);
    }
}
