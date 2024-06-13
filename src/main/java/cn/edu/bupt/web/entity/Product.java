package cn.edu.bupt.web.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;

    private String name;

    private String description;

    private String category;

    private BigDecimal price;

    private Integer stock;

    private LocalDateTime createTime;
}
