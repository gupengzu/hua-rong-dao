package org.example.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.JWTUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI=request.getRequestURI();
        log.info("进入拦截器，请求路径: {}", requestURI);
        if(requestURI.equals("/error"))
        // 判断是否为发送验证码请求或注册请求
        if (requestURI.equals("/register") ) {
            log.info("注册相关请求，直接放行");
            return true;
        }
        if (requestURI.equals("/registry") || requestURI.startsWith("/registry/")) {
            log.info("注册相关请求，直接放行");
            return true;
        }

        if(requestURI.contains("/login")){
            log.info("登录请求");
            return true;
        }

        String token=request.getHeader("token");

        if(token==null||token.isEmpty()){
            log.info("token为空");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            JWTUtils.parseToken(token);
        }catch (Exception e){
            log.info("token解析失败,401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}
