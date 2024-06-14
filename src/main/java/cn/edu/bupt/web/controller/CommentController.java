package cn.edu.bupt.web.controller;

import cn.edu.bupt.web.common.R;
import cn.edu.bupt.web.entity.Comment;
import cn.edu.bupt.web.entity.User;
import cn.edu.bupt.web.service.CommentService;
import cn.edu.bupt.web.service.ProductService;
import cn.edu.bupt.web.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @Resource
    private ProductService productService;

    final RedisTemplate<String, String> redisTemplate;

    public CommentController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/page")
    public R<Page<Comment>> page(@NotNull Long productId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        var page = new Page<Comment>(pageNum, pageSize);
        var lqw = new LambdaQueryWrapper<Comment>();
        lqw.eq(Comment::getProductId, productId).eq(Comment::getStatus, 1)
                .orderByAsc(Comment::getCreateTime);
        commentService.page(page, lqw);
        return R.success(page);
    }

    @PostMapping
    public R<Long> add(@NotNull Long productId, String content, HttpServletRequest request) {
        // 资格认证
        final var authorizationHeader = request.getHeader("Authorization");
        var username = redisTemplate.opsForValue().get(authorizationHeader);
        if (username == null)
            return R.error("请先登录");
        var user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null)
            return R.error("当前评论用户不存在");
        var product = productService.getById(productId);
        if (product == null)
            return R.error("当前评论的商品不存在");

        var userId = user.getId();
        var comment = new Comment(userId, productId, content);
        commentService.save(comment);
        log.info("用户 {} 评论了商品 {} 评论为: {}", username, productId, comment);
        return R.success(comment.getId());
    }

    @PostMapping("/release")
    public R<String> release(@NotNull Long id) {
        var comment = commentService.getById(id);
        if (comment == null)
            return R.error("评论不存在");
        comment.setStatus(1);
        var productId = comment.getProductId();
        var product = productService.getById(productId);
        if (product == null)
            return R.error("商品不存在");
        product.commentCountPlus(1);
        commentService.updateById(comment);
        // 新增评论后, 给商品评论数加一
        productService.updateById(product);
        return R.success("发布成功");
    }

    /**
     * 根据评论 id 删除评论
     */
    @DeleteMapping
    public R<String> delete(@NotNull Long id, HttpServletRequest request) {
        var username = redisTemplate.opsForValue().get(request.getHeader("Authorization"));
        if (username == null)
            return R.error("请先登录");
        var comment = commentService.getById(id);
        var user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (comment == null)
            return R.success("评论不存在");
        if (user == null)
            return R.error("当前用户不存在");
        // 只能删除自己发表的评论 TODO: 增加超级管理员删除评论的功能
        if (!comment.getUserId().equals(user.getId()))
            return R.error("当前用户没有权限删除该评论");
        commentService.removeById(id);
        return R.success("删除成功");
    }
}
