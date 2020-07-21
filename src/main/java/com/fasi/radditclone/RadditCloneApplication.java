package com.fasi.radditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasi.radditclone.config.SwaggerConfiguration;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class RadditCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RadditCloneApplication.class, args);
	}

}
