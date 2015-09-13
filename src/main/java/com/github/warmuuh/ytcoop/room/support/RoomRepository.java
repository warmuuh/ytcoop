package com.github.warmuuh.ytcoop.room.support;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.github.warmuuh.ytcoop.room.Room;

public interface RoomRepository extends Repository<Room, String> {

	Optional<Room> findOne(String id);
	Optional<Room> findRoomByName(String name);
	

	@Query("select r from Room r join r.connections c where c.sessionId = ?1")
	Optional<Room> findRoomBySessionId(String sessionId);
	
	Room save(Room room);
	
	void flush();
}
