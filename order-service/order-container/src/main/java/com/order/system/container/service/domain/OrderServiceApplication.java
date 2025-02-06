package com.order.system.container.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = { "com.order.system.data.order.repository",
                                        "com.order.system.data.customer.repository",
                                        "com.order.system.data.order.adapter" })
@EntityScan(basePackages = { "com.order.system.domain",
                             "com.order.system.data",
                             "com.order.system" })
@SpringBootApplication(scanBasePackages = "com.order.system")
public class OrderServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
