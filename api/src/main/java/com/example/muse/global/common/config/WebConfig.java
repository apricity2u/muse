package com.example.muse.global.common.config;

import com.example.muse.global.filter.HibernateStatsFilter;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<HibernateStatsFilter> hibernateStatsFilter(
            EntityManagerFactory entityManagerFactory) {

        FilterRegistrationBean<HibernateStatsFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new HibernateStatsFilter(entityManagerFactory));
        reg.addUrlPatterns("/*");
        reg.setName("hibernateStatsFilter");
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return reg;
    }

    @Bean(name = "multipartResolver")
    public CustomMultipartResolver multipartResolver() {

        return new CustomMultipartResolver();
    }
}
