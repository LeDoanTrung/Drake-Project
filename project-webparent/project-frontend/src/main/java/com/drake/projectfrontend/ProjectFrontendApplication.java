package com.drake.projectfrontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.drake.projectfrontend.*", "com.drake.projectfrontend" })
@EnableJpaRepositories(basePackages = {"com.drake.projectfrontend.*"}) 
@EntityScan({"com.drake.common.*"})
public class ProjectFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectFrontendApplication.class, args);
	}

}
