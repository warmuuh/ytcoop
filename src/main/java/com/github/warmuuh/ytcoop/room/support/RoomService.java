package com.github.warmuuh.ytcoop.room.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.warmuuh.ytcoop.room.Room;

@Service
public class RoomService {

	@Autowired
	private RoomRepository rooms;
	
	
	public Room createNewRoom(String name){
		Room newRoom = new Room();
		newRoom.setName(name);
		newRoom = rooms.save(newRoom);
		return newRoom;
	}


	public Room getRoom(String roomId) {
		return rooms.findOne(roomId)
				.orElseThrow(() -> new IllegalArgumentException("unknown roomid"));
	}
	
	
}
