package com.black.common.config;

import com.black.common.handle.tokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig implements WebMvcConfigurer{
    @Autowired
    private tokenInterceptor tokenInterceptor;
    //    资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+com.black.common.utils.fileUtils.path+"/");
    }
//token拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(tokenInterceptor).addPathPatterns("/v1/**")
//                .excludePathPatterns("/v1/login")
//                .excludePathPatterns("/v1/down")
//                .excludePathPatterns("/v1/home");
    }
}
