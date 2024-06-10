package cn.edu.bupt.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    private Long id;

    private Long userId;

    private BigDecimal totalAmount;

    private String status;

    private LocalDateTime createTime;
}
