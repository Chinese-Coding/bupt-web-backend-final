package cn.edu.bupt.web.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {
    private Long id;

    private String name;

    private String description;

    private LocalDateTime createTime;
}
