package com.cpp.devops.system;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-09-14 14:58
 **/
public class LimitInterceptor implements HandlerInterceptor {

    private Map<String, RateLimiter> rateLimiterMap = new HashMap();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {

            Limit limit = ((HandlerMethod) handler).getMethodAnnotation(Limit.class);

            if (limit == null) {
                limit = ((HandlerMethod) handler).getMethod().getDeclaringClass().getAnnotation(Limit.class);
            }
            if (limit == null) {
                return true;
            }

            Limit finalLimit = limit;
            RateLimiter rateLimiter = rateLimiterMap.computeIfAbsent(limit.name(),
                                                                     key -> RateLimiter.create(finalLimit.limitNum()));
            return rateLimiter.acquire() >= 0;
        }
        return true;
    }

}
