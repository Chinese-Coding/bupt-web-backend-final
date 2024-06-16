package cn.edu.bupt.web.controller;

import cn.edu.bupt.web.common.R;
import cn.edu.bupt.web.entity.Product;
import cn.edu.bupt.web.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/Shop_admin")
public class AdminProductController {
    @Resource
    private ProductService productService;

    @PostMapping("/addProduct")
    public R<Long> addProduct(@NotBlank String name, @NotBlank String description, @NotBlank String category,
                              @NotNull BigDecimal price, @NotNull Integer stock) {
        var product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);
        product.setCommentCount(0);
        var isSaved = productService.save(product);
        if (isSaved)
            return R.success(product.getId());
        else
            return R.error("Failed to add product");
    }

    @PostMapping("/updateProduct")
    public R<String> updateProduct(@NotNull Long id, String name, String description, String category, BigDecimal price, Integer stock) {
        var product = productService.getById(id);
        if (product == null)
            return R.error("Product not found");

        if (name != null)
            product.setName(name);
        if (description != null)
            product.setDescription(description);
        if (category != null)
            product.setCategory(category);
        if (price != null)
            product.setPrice(price);
        if (stock != null)
            product.setStock(stock);


        var updated = productService.updateById(product);
        if (updated)
            return R.success("Product updated successfully");
        else
            return R.error("Failed to update product");
    }
}
