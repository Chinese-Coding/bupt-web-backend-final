package cn.edu.bupt.web.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;

    private Long userId;

    private Long productId;

    private String content;

    private LocalDateTime createTime;
}
