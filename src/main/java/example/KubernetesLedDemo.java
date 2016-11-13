package com.ctjc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.ctjc")
@SpringBootApplication
public class KubernetesLedDemo {

    public static void main(String[] args) {
        SpringApplication.run(KubernetesLedDemo.class, args);
    }

}
