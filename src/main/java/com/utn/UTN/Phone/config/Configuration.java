package com.utn.UTN.Phone.config;

import com.utn.UTN.Phone.session.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

    @org.springframework.context.annotation.Configuration
    //@PropertySource("app.properties")
    @EnableScheduling
    @EnableCaching
    public class Configuration {

        @Autowired
        SessionFilter sessionFilter;


        @Bean
        public FilterRegistrationBean myFilter() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(sessionFilter);
            registration.addUrlPatterns("/api/*");
            return registration;
        }
    }

