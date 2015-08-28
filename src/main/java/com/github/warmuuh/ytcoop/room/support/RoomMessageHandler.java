package com.github.warmuuh.ytcoop.room.support;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RoomMessageHandler {

	
	@MessageMapping("/command")
	@SendTo("/topic/command")
	public PlaybackCommand spreadHostCommand(PlaybackCommand command){
		return command;
	}
	
}
