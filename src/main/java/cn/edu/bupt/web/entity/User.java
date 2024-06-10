package cn.edu.bupt.web.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private Integer level;

    private LocalDateTime createTime;
}
