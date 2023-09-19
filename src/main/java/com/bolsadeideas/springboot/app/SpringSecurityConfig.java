package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig {

	@Autowired
	private LoginSuccessHandler successHandler;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
    private JpaUserDetailsService userDetailService;

	/*@Bean
	UserDetailsService userDetailsService() throws Exception {

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(
				User.withUsername("admin").password(passwordEncoder.encode("admin")).roles("ADMIN", "USER").build());

		manager.createUser(User.withUsername("user").password(passwordEncoder.encode("user")).roles("USER").build());

		return manager;
	}*/

	/*@Bean
	AuthenticationManager authManager(HttpSecurity http) throws Exception {
		
		AuthenticationManagerBuilder build = http.getSharedObject(AuthenticationManagerBuilder.class);
		
		build.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(passwordEncoder)
			.usersByUsernameQuery("select username, password, enabled from users where username=?")
			.authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");
		
		return build.build();
	}*/

	@Autowired
	public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
		build.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
	}

	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(mvc.pattern("/"), mvc.pattern("/css/**"), mvc.pattern("/js/**"),
						mvc.pattern("/images/**"), mvc.pattern("/listar**"), mvc.pattern("/locale"),
						mvc.pattern("/api/clientes/**"))
				.permitAll()
				/* .requestMatchers(mvc.pattern("/ver/**")).hasAnyRole("USER") */
				/* .requestMatchers(mvc.pattern("/uploads/**")).hasAnyRole("USER") */
				/* .requestMatchers(mvc.pattern("/form/**")).hasAnyRole("ADMIN") */
				/* .requestMatchers(mvc.pattern("/eliminar/**")).hasAnyRole("ADMIN") */
				/* .requestMatchers(mvc.pattern("/factura/**")).hasAnyRole("ADMIN") */
				.anyRequest().authenticated());

		http.formLogin(form -> form.successHandler(successHandler).loginPage("/login").permitAll());
		http.logout(logout -> logout.permitAll());
		http.exceptionHandling(sec -> sec.accessDeniedPage("/error_403"));

		return http.build();
	}

}
