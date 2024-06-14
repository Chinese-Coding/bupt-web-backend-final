package cn.edu.bupt.web.config;

import cn.edu.bupt.web.common.LoginCheckFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class FilterConfig {
    @Bean
    public OncePerRequestFilter loginCheckOncePerRequestFilter
            (RedisTemplate<String, String> redisTemplate) {
        return new LoginCheckFilter(redisTemplate);
    }
}
