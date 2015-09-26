package com.github.warmuuh.ytcoop.room.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Service;

import com.github.warmuuh.ytcoop.room.ParticipantState;
import com.github.warmuuh.ytcoop.room.Room;
import com.github.warmuuh.ytcoop.room.UserProfile;

@Service
public class ParticipantMessanger {

	
	@Autowired
	SimpMessagingTemplate messageTemplate;

	
	public void sendJoinNotification(UserProfile user, String roomId){
		ParticipantState state = new ParticipantState("JOINED");
		state.setSender(user);
		messageTemplate.convertAndSend("/topic/room/" + roomId + "/participants", state);
	}
	
	public void sendLeftNotification(UserProfile user, String roomId){
		ParticipantState state = new ParticipantState("LEFT");
		state.setSender(user);
		messageTemplate.convertAndSend("/topic/room/" + roomId + "/participants", state);
	}
}
