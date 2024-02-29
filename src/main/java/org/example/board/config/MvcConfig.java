package org.example.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profiles/**", "/posts/images/**")
                .addResourceLocations("file:///Users/baeyeong-ug/Desktop/image/profiles/")
                .addResourceLocations("file:///Users/baeyeong-ug/Desktop/image/images/");
    }

}
