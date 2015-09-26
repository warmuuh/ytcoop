package com.github.warmuuh.ytcoop.room.support;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.warmuuh.ytcoop.room.Room;
import com.github.warmuuh.ytcoop.room.support.RoomConnectionRepository.SessionProperties;

import lombok.Data;

@Service
public class RoomConnectionRepository {
	
	@Data
	public static class SessionProperties{
		final String sessionId;
		final String roomId;
		final String userId;
	}
	
	List<SessionProperties> sessionProperties;
	
	
	public synchronized void addConnection(String sessionId, String roomId, String userId){
		sessionProperties.add(new SessionProperties(sessionId, roomId, userId));
	}
	
	public synchronized Optional<SessionProperties> remove(String sessionId){
		Optional<SessionProperties> first = sessionProperties.stream().filter(p -> p.sessionId.equals(sessionId)).findFirst();
		first.ifPresent(p -> sessionProperties.remove(p));
		return first;
	}
	
	public synchronized boolean hasConnectionsToRoom(String userId, String roomid){
		return sessionProperties.stream()
				.anyMatch(p -> p.userId.equals(userId) && p.roomId.equals(roomid));
	}
	
	
}
