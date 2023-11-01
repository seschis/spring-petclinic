package org.springframework.samples.petclinic.system;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource.petclinic")
	public DataSourceProperties petclinicDataSourceProperties() {
		return new DataSourceProperties();
	}
}
