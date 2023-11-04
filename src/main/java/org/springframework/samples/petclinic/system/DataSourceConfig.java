package org.springframework.samples.petclinic.system;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackageClasses = { Owner.class, Vet.class, Pet.class, Visit.class, PetType.class, Specialty.class },
		entityManagerFactoryRef = "petclinicEntityManagerFactory",
		transactionManagerRef = "petclinicTransactionManager")
public class DataSourceConfig {

	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource.petclinic")
	public DataSourceProperties petclinicDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	public DataSource petclinicDataSource() {
		return petclinicDataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean
	@Primary
	public JdbcTemplate petclinicJdbcTemplate(@Qualifier("petclinicDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean petclinicEntityManagerFactory(
			@Qualifier("petclinicDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource)
			.packages(Owner.class, Vet.class, Pet.class, Visit.class, PetType.class, Specialty.class)
			.build();
	}

	@Bean
	@Primary
	public PlatformTransactionManager petclinicTransactionManager(
			@Qualifier("petclinicEntityManagerFactory") LocalContainerEntityManagerFactoryBean petclinicEntityManagerFactory) {
		return new JpaTransactionManager(Objects.requireNonNull(petclinicEntityManagerFactory.getObject()));
	}

}
