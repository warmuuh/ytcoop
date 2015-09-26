package com.github.warmuuh.ytcoop.room.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.github.warmuuh.ytcoop.room.PlaybackCommand;
import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.security.AuthUtil;

@Controller
public class RoomMessageHandler {
	
	@Autowired
	RoomService service;
	
	
	@MessageMapping("/room/{roomid}/command")
	@SendTo("/topic/room/{roomid}/command")
	public PlaybackCommand spreadHostCommand(@DestinationVariable("roomid") String roomid, PlaybackCommand command){
		
		command.setSender(AuthUtil.getUserProfile());
		
		return command;
	}
	
}
