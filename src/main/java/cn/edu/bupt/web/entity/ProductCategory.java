package cn.edu.bupt.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ProductCategory {
    @TableId(type = IdType.INPUT)
    private Long productId;

    @TableId(type = IdType.INPUT)
    private Long categoryId;
}
