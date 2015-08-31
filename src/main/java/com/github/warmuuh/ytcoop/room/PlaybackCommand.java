package com.github.warmuuh.ytcoop.room;

import java.util.Map;

import lombok.Data;

@Data
public class PlaybackCommand {
	String command;
	UserProfile sender;
	Map<String, String> payload;
}
