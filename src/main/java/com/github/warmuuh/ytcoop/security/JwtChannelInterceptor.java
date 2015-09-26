package com.github.warmuuh.ytcoop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.warmuuh.ytcoop.room.UserProfile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor, ExecutorChannelInterceptor{
	@Autowired
	TokenHandler handler;
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		
		log.info("ws jwt filter");
		MessageHeaders headers = message.getHeaders();
		MessageHeaderAccessor mutableAccessor = MessageHeaderAccessor.getMutableAccessor(message);
		if (mutableAccessor.getHeader("nativeHeaders") == null || ((org.springframework.util.LinkedMultiValueMap)mutableAccessor.getHeader("nativeHeaders")).get("auth") == null)
			return message;
		String authJwtToken = (String) ((org.springframework.util.LinkedMultiValueMap)mutableAccessor.getHeader("nativeHeaders")).get("auth").get(0);
		UserProfile user = handler.parseUserFromToken(authJwtToken);
		SecurityContextHolder.getContext().setAuthentication(new UserProfileAuthentication(user));
		return message;
	}
	
	@Override
	public boolean preReceive(MessageChannel channel) {
		return false;
	}
	
	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		
	}
	
	@Override
	public Message<?> postReceive(Message<?> message, MessageChannel channel) {
		return null;
	}
	
	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		
	}
	
	@Override
	public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
		
	}

	@Override
	public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
		return preSend(message, channel);
	}

	@Override
	public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
		// TODO Auto-generated method stub
		
	}

}
