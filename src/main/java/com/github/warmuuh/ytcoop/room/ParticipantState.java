package com.github.warmuuh.ytcoop.room;

import java.util.Map;

import lombok.Data;

@Data
public class ParticipantState {
	final String state;
	UserProfile sender;
}
