package com.github.warmuuh.ytcoop.room;

import java.util.Map;

import lombok.Data;

@Data
public class PlaybackCommand {
	String command;
	String senderId;
	Map<String, String> payload;
}
