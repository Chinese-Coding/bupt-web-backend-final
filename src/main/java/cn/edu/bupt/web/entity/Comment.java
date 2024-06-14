package cn.edu.bupt.web.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment {
    private Long id;

    private Long userId;

    private Long productId;

    private String content;

    /*
     * 标记评论状态 (实现先评论但是可以不发布)
     * 0: 评论未发布
     * 1: 评论已发布
     * */
    private Integer status;

    private LocalDateTime createTime;

    public Comment(Long userId, Long productId, String content) {
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.createTime = LocalDateTime.now();
    }
}
