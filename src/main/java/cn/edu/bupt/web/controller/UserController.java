package cn.edu.bupt.web.controller;

import cn.edu.bupt.web.common.R;
import cn.edu.bupt.web.entity.User;
import cn.edu.bupt.web.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    final RedisTemplate<String, String> redisTemplate;

    public UserController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/register")
    public R<String> register(@NotBlank String username, @NotBlank String password,
                              @NotBlank String phone, @NotBlank String code) {
        log.info("用户注册: username({}), password({}) code({})", username, password, code);
        var codeInRedis = redisTemplate.opsForValue().get(phone);
        if (codeInRedis != null && codeInRedis.equals(code)) {
            var user = new User(username, password, phone);
            userService.save(user);
            redisTemplate.delete(phone);
            return R.success("注册成功");
        }
        return R.error("验证码错误");
    }

    @PostMapping("/login")
    public R<String> login(@NotBlank String username, @NotBlank String password) {
        var lqw = new LambdaQueryWrapper<User>();
        lqw.eq(User::getUsername, username);
        var user = userService.getOne(lqw);
        if (user == null)
            return R.error("用户不存在");
        if (user.getPassword().equals(password)) {
            var token = generateToken(username);
            redisTemplate.opsForValue().set(token, username, 30, TimeUnit.MINUTES);
            log.info("用户 {}, 的 Authorization: {}", username, token);
            return R.success(token);
        }
        return R.error("密码错误");
    }

    @GetMapping("/logout")
    public R<String> logout(String token) {
        redisTemplate.delete(token);
        return R.success("退出成功");
    }


    @GetMapping("/sendMsg")
    public R<String> sendMsg(@NotBlank String phone) {
        var code = Integer.toHexString(new Random().nextInt()).substring(0, 6);
        // 将验证码放到 redis 中,
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
        return R.success(code);
    }

    /**
     * 生成token (格式为token: 用户名-六位随机数)
     */
    public String generateToken(String username) {
        var token = new StringBuilder("token:");
        token.append(username).append("-").
                append(new Random().nextInt(999999 - 111111 + 1) + 111111);
        log.info("token: {}", token);
        return token.toString();
    }
}
