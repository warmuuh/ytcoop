package com.github.warmuuh.ytcoop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

import com.github.warmuuh.ytcoop.security.JwtAuthenticationSuccessHandler;
import com.github.warmuuh.ytcoop.security.StatelessAuthenticationFilter;
import com.github.warmuuh.ytcoop.security.TokenAuthenticationService;
import com.github.warmuuh.ytcoop.social.SimpleSocialUsersDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

	@Value("${jwt.token.secret}") 
	String tokenSecret;
	
	@Autowired
	SimpleSocialUsersDetailService userService;
	
	public SecurityConfig() {
		super(true);
	}
	    
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        SpringSocialConfigurer socialConfigurer = new SpringSocialConfigurer();
        socialConfigurer.addObjectPostProcessor(new ObjectPostProcessor<SocialAuthenticationFilter>() {

			@Override
			public <O extends SocialAuthenticationFilter> O postProcess(O saf) {
				saf.setAuthenticationSuccessHandler(jwtAuthHandler());
				return saf;
			}

		});
		http
		.anonymous().and()
        .csrf().disable()
        	.authorizeRequests()
	        	.antMatchers("/auth/**").permitAll()
	        	.antMatchers("/webjars/**").permitAll()
	        	.antMatchers("/login").permitAll()
	        	.anyRequest().authenticated()
        .and()
            .formLogin().loginPage("/login")
        .and()
            .apply(socialConfigurer
                .postLoginUrl("/")
                .alwaysUsePostLoginUrl(true))
        .and()
	        // Custom Token based authentication based on the header previously given to the client
	        .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService(), jwtAuthHandler()),
	                UsernamePasswordAuthenticationFilter.class);
    }
	 
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }
 
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public JwtAuthenticationSuccessHandler jwtAuthHandler() {
		return new JwtAuthenticationSuccessHandler();
	}
    
 
    @Bean
    public TokenAuthenticationService tokenAuthenticationService() {
        return new TokenAuthenticationService(tokenSecret, userService);
    }

}
