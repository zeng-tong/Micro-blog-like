package com.zengtong.Configuration;

import com.zengtong.Interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor).addPathPatterns("/login","/","/weibo","/comment","/deleteWeibo","/deleteComment",
                "/addMessage","/showDetailMessage","/showListMessage",
                "/like","/dislike","/follow","/feedWeibo","/index",
                "/listComments","/addComment","/followee/{uid}",
                "/addWeibo","/profile/{uid}","/follower/{uid}",
                "/conversation","/messagebox");
    }



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }



}
