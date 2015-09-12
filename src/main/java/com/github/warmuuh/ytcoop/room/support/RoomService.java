package com.github.warmuuh.ytcoop.room.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.stereotype.Service;

import com.github.warmuuh.ytcoop.room.Room;
import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.video.VideoDetails;

@Service
public class RoomService {

	@Autowired
	private RoomRepository rooms;
	
	
	
	public Room createNewRoom(VideoDetails video){
		SocialAuthenticationToken authentication = (SocialAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		Connection<?> curUserCon = authentication.getConnection();
		
		UserProfile profile = new UserProfile();
		profile.setDisplayName(curUserCon.getDisplayName());
		profile.setImageUrl(curUserCon.getImageUrl());
		profile.setUserId(curUserCon.getKey().getProviderUserId());
		
		Room newRoom = new Room();
		newRoom.setName(video.getTitle());
		newRoom.getParticipants().add(profile);
		newRoom.setHostUserId(profile.getUserId());
		newRoom.setVideo(video);
		newRoom = rooms.save(newRoom);
		
		return newRoom;
	}


	public Room getRoom(String roomId) {
		return rooms.findOne(roomId)
				.orElseThrow(() -> new IllegalArgumentException("unknown roomid: " + roomId));
	}
	
	
}
