package com.bankapp.v1.config_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

//@SpringBootTest
@SpringBootTest(properties = {
		"spring.profiles.active=native",
		"spring.cloud.config.server.native.search-locations=classpath:/config"
		// ^ points to a mock folder in src/test/resources/config if needed
})
@ImportAutoConfiguration(classes = RefreshAutoConfiguration.class)
class ConfigServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
