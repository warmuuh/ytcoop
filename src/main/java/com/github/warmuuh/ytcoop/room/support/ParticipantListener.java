package com.github.warmuuh.ytcoop.room.support;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.github.warmuuh.ytcoop.room.ParticipantState;
import com.github.warmuuh.ytcoop.room.Room;
import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.security.AuthUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ParticipantListener {
	@Autowired
	SimpMessagingTemplate messageTemplate;

	@Autowired
	RoomService roomService;
	
	
	
	Pattern roomChan = Pattern.compile("/topic/room/(.*)/participants");

	@EventListener
	public void onJoin(SessionSubscribeEvent evt) {
		Message<byte[]> message = evt.getMessage();
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		StompCommand command = accessor.getCommand();
		if (command.equals(StompCommand.SUBSCRIBE)) {
			String destination = accessor.getDestination();
			Matcher match = roomChan.matcher(destination);
			if (match.matches()) {
				String roomId = match.group(1);
				log.info("Session joined:" + accessor.getSessionId());
				sendJoinNotification(roomId, accessor.getSessionId());
			}
		}
	}

	@EventListener
	public void onLeave(SessionDisconnectEvent evt) {
		Message<byte[]> message = evt.getMessage();
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		//for some reason, the user is gone from evt.getUser(), so we need to look into the message itself
		roomService.removeConnection(accessor.getSessionId());
		log.warn("Session Disconnected:" + accessor.getSessionId());
	}

	public void sendJoinNotification(String roomid, String sessionId) {
		roomService.addNewConnection(roomid, sessionId, AuthUtil.getUserId());
	}
}
