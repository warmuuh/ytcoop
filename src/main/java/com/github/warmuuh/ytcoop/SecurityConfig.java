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
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	        .csrf().disable()
	        	.authorizeRequests()
		        	.antMatchers("/webjars/**", "/auth/**", "/login").permitAll()
		        	.anyRequest().authenticated()
	        .and()
	            .formLogin().loginPage("/login")
	        .and()
	            .apply(new SpringSocialConfigurer()
	                .postLoginUrl("/")
	                .alwaysUsePostLoginUrl(true));
	    }
	 
//	 

}
