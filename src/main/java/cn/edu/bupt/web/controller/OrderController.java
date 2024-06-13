package cn.edu.bupt.web.controller;

import cn.edu.bupt.web.common.R;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.edu.bupt.web.entity.Order;
@RestController
@RequestMapping("/order")
public class OrderController {
    final RedisTemplate<String, String> redisTemplate;

    public OrderController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/product")
    public R<Long> neworder(@NotBlank Long Product_id,@NotBlank Integer num){
        return null;
    }
}
