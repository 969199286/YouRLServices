package com.YouRL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class YouRlApplication {

	public static void main(String[] args) {
		SpringApplication.run(YouRlApplication.class, args);
	}


}
