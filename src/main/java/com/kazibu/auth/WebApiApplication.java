package com.kazibu.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.kazibu")
@EnableJpaRepositories(basePackages = "com.kazibu")
@EntityScan(basePackages = "com.kazibu")
public class WebApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebApiApplication.class, args);
  }

}
