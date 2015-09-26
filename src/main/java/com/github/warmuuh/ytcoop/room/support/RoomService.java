package com.github.warmuuh.ytcoop.room.support;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.warmuuh.ytcoop.room.Room;
import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.room.support.RoomConnectionRepository.SessionProperties;
import com.github.warmuuh.ytcoop.security.AuthUtil;
import com.github.warmuuh.ytcoop.video.VideoDetails;

@Service
public class RoomService {

	@Autowired
	private RoomRepository rooms;
	
	@Autowired
	RoomConnectionRepository connections;
	
	@Autowired
	ParticipantMessanger participants;
	
	
	public Room createNewRoom(VideoDetails video){
		Room newRoom = new Room();
		newRoom.setName(video.getTitle());
		newRoom.setHostUserId(AuthUtil.getUserId());
		newRoom.setVideo(video);
		newRoom = rooms.save(newRoom);
		
		return newRoom;
	}


	public Room getRoom(String roomId) {
		return rooms.findOne(roomId)
				.orElseThrow(() -> new IllegalArgumentException("unknown roomid: " + roomId));
	}
	
	
	public Room addCurrentUserAsParticipant(String roomId){
		UserProfile profile = AuthUtil.getUserProfile();
		
		Room room = getRoom(roomId);
		if (!room.getParticipants().stream()
				.anyMatch(u -> u.getUserId().equals(profile.getUserId())))
		{
			room.getParticipants().add(profile);
			return rooms.save(room);
		}
		
		return room;
	}
	
	public void addNewConnection(String roomId, String sessionId, String userId){
		
		Room room = getRoom(roomId);
		boolean isFirstConnection = !connections.hasConnectionsToRoom(userId, roomId);
		
		connections.addConnection(sessionId, roomId, userId);

		if (isFirstConnection){
			UserProfile user = room.getParticipants().stream()
					.filter(u -> u.getUserId().equals(userId))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("User should be participant"));
			participants.sendJoinNotification(user, roomId);
		}
	}
	
	/**
	 * 
	 * @param sessionId
	 * @return user, if the user was removed from the room
	 */
	public void removeConnection(String sessionId){
		//TODO: remove users, connections and also the room itself, if empty
		 Optional<SessionProperties> props = connections.remove(sessionId);
		 props.ifPresent(p -> removeParticipantIfInactive(p.getUserId(), p.getRoomId()));
	}
	
	public Optional<UserProfile> removeParticipantIfInactive(String userId, String roomId){
		if (!connections.hasConnectionsToRoom(userId, roomId)){
			Room room = getRoom(roomId);
			return room.getParticipants().stream()
					.filter(u -> u.getUserId().equals(userId))
					.findFirst()
					.map(u -> {
						room.getParticipants().remove(u);
						rooms.save(room);
						participants.sendLeftNotification(u, roomId);
						return u;
					});
		}
		return Optional.empty();
	}
}
