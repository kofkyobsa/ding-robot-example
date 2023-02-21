package com.cpp.devops.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-09-14 18:27
 **/
@Configuration
public class SecurityConfig {

    @Bean
    HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setUnsafeAllowAnyHttpMethod(true);
        //firewall.setAllowedHostnames();
        return firewall;
    }
}
