package com.order.system.stock.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public stockDomainService stockDomainService() {
        return new stockDomainServiceImpl();
    }
}
