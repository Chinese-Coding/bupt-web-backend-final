package cn.edu.bupt.web.config;

import cn.edu.bupt.web.common.LoginCheckFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class FilterConfig {
    @Bean
    public OncePerRequestFilter loginCheckOncePerRequestFilter() {
        return new LoginCheckFilter();
    }
}
