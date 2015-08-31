package com.github.warmuuh.ytcoop.room.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.github.warmuuh.ytcoop.room.PlaybackCommand;
import com.github.warmuuh.ytcoop.room.Room;
import com.github.warmuuh.ytcoop.room.UserProfile;

@Controller
public class RoomMessageHandler {
	
	@Autowired
	RoomService service;
	
	
	@SubscribeMapping("/room/{roomid}/participants")
	public List<UserProfile> sendParticipantList(@DestinationVariable("roomid") String roomid){
		Room room = service.getRoom(roomid);
		return room.getParticipants();
	}
	
	@MessageMapping("/room/{roomid}/command")
	@SendTo("/topic/room/{roomid}/command")
	public PlaybackCommand spreadHostCommand(@DestinationVariable("roomid") String roomid, PlaybackCommand command){
		SocialAuthenticationToken authentication = (SocialAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		Connection<?> connection = authentication.getConnection();
		command.setSenderId(connection.getKey().getProviderUserId());
		return command;
	}
	
}
