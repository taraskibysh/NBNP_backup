package com.nbnp.trip_to_go.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SimpleCorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        chain.doFilter(req, res);
    }

    @Bean
    public FilterRegistrationBean<SimpleCorsFilter> corsFilter() {
        FilterRegistrationBean<SimpleCorsFilter> bean = new FilterRegistrationBean<>(new SimpleCorsFilter());
        bean.setOrder(0);
        return bean;
    }
}