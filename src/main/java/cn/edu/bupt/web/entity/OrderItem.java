package cn.edu.bupt.web.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long id;

    private Long orderId;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;
}
