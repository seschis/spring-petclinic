package org.springframework.samples.petclinic.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain web(HttpSecurity http) throws Exception {

		http.httpBasic(withDefaults()).formLogin(withDefaults());

		http.authorizeHttpRequests(authorize -> authorize.dispatcherTypeMatchers(FORWARD, ERROR)
			.permitAll()
			// .requestMatchers("/customers/**").hasRole("USER")
			.anyRequest()
			.permitAll());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.withDefaultPasswordEncoder()
			.username("user")
			.password("password")
			.roles("USER")
			.build();

		System.out.printf("\n\ncredentials: %s : %s\n\n\n", "user", "password");
		return new InMemoryUserDetailsManager(userDetails);
	}

}
