package cn.edu.bupt.web.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {
    private Long id;

    private Long orderId;

    private BigDecimal paymentAmount;

    private String paymentStatus;

    private LocalDateTime paymentTime;
}
