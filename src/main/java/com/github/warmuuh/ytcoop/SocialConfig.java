package com.github.warmuuh.ytcoop;

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
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.github.warmuuh.ytcoop.social.AccountConnectionSignUpService;

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
	 
	 @PostConstruct
	 public void setupUserConRepo(){
			repo.setConnectionSignUp(new AccountConnectionSignUpService());
	 }

}
