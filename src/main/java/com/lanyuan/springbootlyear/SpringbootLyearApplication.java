package com.lanyuan.springbootlyear;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lanyuan.springbootlyear.mapper")
public class SpringbootLyearApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootLyearApplication.class, args);
	}

}
