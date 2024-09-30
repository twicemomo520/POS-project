package com.example.pos10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//
//
@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
public class Pos10Application2 {

	public static void main(String[] args) {
		SpringApplication.run(Pos10Application2.class, args);
	}

}
