package com.github.warmuuh.ytcoop.room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class RoomConnection {

	@Id
	@GeneratedValue
	String id;
	
	
	String sessionId;

	String userId;
}
