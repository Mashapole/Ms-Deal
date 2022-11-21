package com.enfint.deal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class DealApplication {

	public static void main(String[] args) {

        SpringApplication.run(DealApplication.class, args);
        log.info("----------------------------");
        log.info("The ms-deal has started");
        log.info("----------------------------");
	}

}
