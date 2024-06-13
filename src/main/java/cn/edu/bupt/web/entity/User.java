package cn.edu.bupt.web.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {
    private Long id;

    private String username;

    private String password;

    private String phone;

    private Integer level;

    private LocalDateTime createTime;

    public User(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.level = 1;
        this.createTime = LocalDateTime.now();
    }
}
