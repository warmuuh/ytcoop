package com.github.warmuuh.ytcoop.room.support;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.github.warmuuh.ytcoop.room.Room;

public interface RoomRepository extends Repository<Room, String> {

	Optional<Room> findOne(String id);
	Optional<Room> findRoomByName(String name);
	
	Room save(Room room);
}
