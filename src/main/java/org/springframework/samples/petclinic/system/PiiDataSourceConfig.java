package org.springframework.samples.petclinic.system;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.samples.petclinic.customer.Customer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = { Customer.class }, entityManagerFactoryRef = "piiEntityManagerFactory",
		transactionManagerRef = "piiTransactionManager")
public class PiiDataSourceConfig {

	@Bean
	@ConfigurationProperties("spring.datasource.pii")
	public DataSourceProperties piiDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource piiDataSource() {
		return piiDataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean
	public JdbcTemplate piiJdbcTemplate(@Qualifier("piiDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean piiEntityManagerFactory(
			@Qualifier("piiDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource).packages(Customer.class).build();
	}

	@Bean
	public PlatformTransactionManager piiTransactionManager(
			@Qualifier("piiEntityManagerFactory") LocalContainerEntityManagerFactoryBean piiEntityManagerFactory) {
		return new JpaTransactionManager(Objects.requireNonNull(piiEntityManagerFactory.getObject()));
	}

	@Bean
	public ResourceDatabasePopulator piiPopulator(@Value("${spring.datasource.pii.schema-locations}") String schema,
			@Value("${spring.datasource.pii.data-locations}") String data) {
		var r = new ResourceDatabasePopulator(new ClassPathResource(schema), new ClassPathResource(data));
		r.setSeparator(";");
		return r;
	}

	@Bean
	public DataSourceInitializer piiInitializer(@Qualifier("piiPopulator") ResourceDatabasePopulator piiPopulator,
			@Qualifier("piiDataSource") DataSource dataSource) {

		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(piiPopulator);

		return initializer;
	}

}
