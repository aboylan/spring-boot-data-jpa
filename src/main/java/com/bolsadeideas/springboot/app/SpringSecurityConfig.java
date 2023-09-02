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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
	SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

		http.authorizeHttpRequests((authz) -> {
			authz.requestMatchers(mvcMatcherBuilder.pattern("/"), new AntPathRequestMatcher("/css/**"),
					new AntPathRequestMatcher("/js/**"), new AntPathRequestMatcher("/images/**"),
					mvcMatcherBuilder.pattern("/listar")).permitAll()
					.requestMatchers(mvcMatcherBuilder.pattern("/ver/**")).hasAnyRole("USER")
					.requestMatchers(mvcMatcherBuilder.pattern("/uploads/**")).hasAnyRole("USER")
					.requestMatchers(mvcMatcherBuilder.pattern("/form/**")).hasAnyRole("ADMIN")
					.requestMatchers(mvcMatcherBuilder.pattern("/eliminar/**")).hasAnyRole("ADMIN")
					.requestMatchers(mvcMatcherBuilder.pattern("/factura/**")).hasAnyRole("ADMIN").anyRequest()
					.authenticated();
		});

		return http.build();
	}
}
