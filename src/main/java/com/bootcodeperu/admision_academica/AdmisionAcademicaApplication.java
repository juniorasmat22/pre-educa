package com.bootcodeperu.admision_academica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdmisionAcademicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdmisionAcademicaApplication.class, args);
	}

}
