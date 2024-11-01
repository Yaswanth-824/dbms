package com.example.Travel.And.Tourisum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.Travel.And.Tourisum.Config.fileconfig;

@SpringBootApplication
@EnableConfigurationProperties(fileconfig.class)
public class TravelAndTourisumApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelAndTourisumApplication.class, args);
	}

}
