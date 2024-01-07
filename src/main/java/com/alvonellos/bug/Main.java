package com.alvonellos.bug;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log
@EnableScheduling
@EnableConfigurationProperties
@RequiredArgsConstructor
@SpringBootApplication
public class Main implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	final Environment environment;

	/**
	 * Callback used to run the bean.
	 *
	 * @param args incoming main method arguments
	 * @throws Exception on error
	 */
	@Override
	public void run(String... args) throws Exception {
		log.info("Datasource: " + environment.getProperty("spring.datasource.username"));
	}
}
