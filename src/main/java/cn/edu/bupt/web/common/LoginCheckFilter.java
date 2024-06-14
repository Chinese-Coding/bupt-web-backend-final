package cn.edu.bupt.web.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * 检查用户是否已经完成登录
 */
@Slf4j
@Component
public class LoginCheckFilter extends OncePerRequestFilter {
    // 路径匹配器, 支持通配符
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    // 这些路径不需要处理
    private static final String[] urls = new String[]{
            "/user/*"
    };

    private static final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var requestURI = request.getRequestURI();

        /*
         * `doFilter` 表示请求继续沿着过滤器链传递,
         * 最终到达目标资源 (如控制器或静态资源)
         * */
        if (check(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        var username = redisTemplate.opsForValue().get(authorizationHeader);
        if (username != null)
            filterChain.doFilter(request, response);
        else {
            log.info("用户验证出现意外");
            response.getWriter().write(new ObjectMapper().writeValueAsString(R.error("NOTLOGIN")));
        }
    }

    /**
     * 路径匹配, 检查本次请求是否需要放行
     */
    public boolean check(String requestURI) {
        for (var url : urls)
            if (PATH_MATCHER.match(url, requestURI))
                return true;
        return false;
    }
}
