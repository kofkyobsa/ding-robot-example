package com.cpp.devops.system.config;

import com.cpp.devops.system.LimitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-09-14 15:30
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public LimitInterceptor initInterceptor() {
        return new LimitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initInterceptor()).addPathPatterns("/**");
    }
}

