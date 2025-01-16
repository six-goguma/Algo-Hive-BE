package com.knu.algo_hive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AlgoHiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgoHiveApplication.class, args);
    }

}
