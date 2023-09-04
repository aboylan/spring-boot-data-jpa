package com.bolsadeideas.springboot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SpringSecurityConfig {

	@Bean
	static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService() throws Exception {

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(
				User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("ADMIN", "USER").build());

		manager.createUser(User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build());

		return manager;
	}

	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(mvc.pattern("/"), mvc.pattern("/css/**"), mvc.pattern("/js/**"),
						mvc.pattern("/images/**"), mvc.pattern("/listar"))
				.permitAll().requestMatchers(mvc.pattern("/ver/**")).hasAnyRole("USER")
				.requestMatchers(mvc.pattern("/uploads/**")).hasAnyRole("USER").requestMatchers(mvc.pattern("/form/**"))
				.hasAnyRole("ADMIN").requestMatchers(mvc.pattern("/eliminar/**")).hasAnyRole("ADMIN")
				.requestMatchers(mvc.pattern("/factura/**")).hasAnyRole("ADMIN").anyRequest().authenticated());

		http.formLogin(form -> form.loginPage("/login").permitAll());
		http.logout(logout -> logout.permitAll());
		http.exceptionHandling(sec -> sec.accessDeniedPage("/error_403"));

		return http.build();
	}

}
