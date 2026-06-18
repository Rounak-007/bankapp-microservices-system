package com.bankapp.v1.service_registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

	// Define the SLF4J logger instance
	private static final Logger log = LoggerFactory.getLogger(ServiceRegistryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistryApplication.class, args);
		log.info("Service Registry has started up successfully!");
	}

}
