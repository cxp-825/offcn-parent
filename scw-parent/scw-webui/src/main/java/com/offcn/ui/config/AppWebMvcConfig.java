package com.offcn.ui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//访问login.html  ==>  显示登录页
@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //当访问路径是 /login.html的时候，设置视图为login 配合视图解析器进行解析
        registry.addViewController("/login.html").setViewName("login");
    }
}
