package com.github.warmuuh.ytcoop;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.security.JwtChannelInterceptor;
import com.github.warmuuh.ytcoop.security.TokenHandler;
import com.github.warmuuh.ytcoop.security.UserProfileAuthentication;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
				// users cannot send to these broker destinations, only the application can
//		 		.simpMessageDestMatchers("/topic/chat.login", "/topic/chat.logout", "/topic/chat.message").denyAll()
				.anyMessage().authenticated();
	}

	@Autowired
	@Qualifier("clientInboundChannel")
	AbstractSubscribableChannel chan;
	
	@PostConstruct
	public void setup(){
		chan.addInterceptor(jwtInterceptor());
	}
	
	@Override
	protected void customizeClientInboundChannel(ChannelRegistration registration) {
		try {
			Method getInterceptors = registration.getClass().getDeclaredMethod("getInterceptors");
			getInterceptors.setAccessible(true);
			List<ChannelInterceptor> interceptors = (List<ChannelInterceptor>) getInterceptors.invoke(registration);
			interceptors.add(1, jwtInterceptor());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	@Bean
	public ChannelInterceptor jwtInterceptor(){
		return new JwtChannelInterceptor();
	}
	@Override
	protected boolean sameOriginDisabled() {
		//disable CSRF for websockets for now...
		return true;
	}
}