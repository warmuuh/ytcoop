package com.github.warmuuh.ytcoop.room.support;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.stereotype.Service;

import com.github.warmuuh.ytcoop.room.Room;
import com.github.warmuuh.ytcoop.room.RoomConnection;
import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.security.AuthUtil;
import com.github.warmuuh.ytcoop.video.VideoDetails;

@Service
public class RoomService {

	@Autowired
	private RoomRepository rooms;
	
	
	
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
	
	public Room addNewConnection(String roomId, String sessionId, String userId){
		Room room = getRoom(roomId);
		RoomConnection con = new RoomConnection();
		con.setSessionId(sessionId);
		con.setUserId(userId);
		room.getConnections().add(con);
		return rooms.save(room);
	}
	
	public Room removeConnection(String sessionId){
		Room room = rooms.findRoomBySessionId(sessionId)
				.orElseThrow(() -> new IllegalArgumentException("cannot find room for sessionId: " + sessionId));
		RoomConnection con = room.getConnections().stream()
			.filter(c -> c.getSessionId().equals(sessionId))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("multiple sessions found for connection: " + sessionId));
		
		room.getConnections().remove(con);
		
		//TODO: remove users, connections and also the room itself, if empty
		
		return rooms.save(room);
	}
	
	public Optional<UserProfile> removeParticipantIfInactive(String userId, String roomId){
		Room room = getRoom(roomId);
		boolean inactive = room.getConnections().stream()
					.noneMatch(c -> c.getUserId().equals(userId));
		if (inactive){
			Optional<UserProfile> user = room.getParticipants().stream().filter(u -> u.getUserId().equals(userId)).findFirst();
			user.ifPresent(u -> {
				room.getParticipants().remove(u);
				rooms.save(room);
				});
			return user;
		}
		return Optional.empty();
	}
}
