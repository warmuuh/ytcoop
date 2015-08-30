package com.github.warmuuh.ytcoop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import com.github.warmuuh.ytcoop.social.AccountConnectionSignUpService;

@Configuration
@EnableWebSecurity
@EnableSocial
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	        	.authorizeRequests()
	        	.antMatchers("/webjars/**").permitAll()
	        	.antMatchers("/auth/**").permitAll()
	        	.antMatchers("/login").permitAll()
	        	.anyRequest().hasAnyRole("USER")
	        .and()
	            .formLogin().loginPage("/login")
	        .and()
	            .apply(new SpringSocialConfigurer()
	                .postLoginUrl("/")
	                .alwaysUsePostLoginUrl(true));
	    }
	 
//	 
//	 @Bean
//	 public InMemoryUsersConnectionRepository userConnectionrepository(ConnectionFactoryLocator connectionFactoryLocator){
//		InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
//		repository.setConnectionSignUp(new AccountConnectionSignUpService());
//		return repository;
//	 }
}
