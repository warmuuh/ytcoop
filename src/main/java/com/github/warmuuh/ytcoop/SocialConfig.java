package com.github.warmuuh.ytcoop;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.github.warmuuh.ytcoop.social.AccountConnectionSignUpService;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

@Configuration
public class SocialConfig {

//	 @Bean
//	 public InMemoryUsersConnectionRepository userConnectionrepository(ConnectionFactoryLocator connectionFactoryLocator){
//		InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
//		repository.setConnectionSignUp(new AccountConnectionSignUpService());
//		return repository;
//	 }

	@Autowired
	InMemoryUsersConnectionRepository repo;

	@Bean
	public ProviderSignInUtils getProviderUtils(ConnectionFactoryLocator locator, UsersConnectionRepository repo){
		return new ProviderSignInUtils(locator, repo);
	}
	
	 @PostConstruct
	 public void setupUserConRepo(){
			repo.setConnectionSignUp(signupService());
	 }

	 
	@Bean
	public AccountConnectionSignUpService signupService() {
		return new AccountConnectionSignUpService();
	}

	 
	 @Bean
	 public YouTube youtube(){
		 YouTube yt = new YouTube.Builder(new ApacheHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
				public void initialize(HttpRequest req) throws IOException {}
			}).setApplicationName("ytCoop").build();
		 return yt;
	 }
}
