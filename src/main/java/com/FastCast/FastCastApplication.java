package com.FastCast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.FastCast.Security.JWT.JwtTokenUtil;


@SpringBootApplication
public class FastCastApplication {
	
	@Bean
	public JwtTokenUtil jwtTokenUtil() {
	    return  new JwtTokenUtil();
	}
	


	public static void main(String[] args) {
		SpringApplication.run(FastCastApplication.class, args);
	}

}
