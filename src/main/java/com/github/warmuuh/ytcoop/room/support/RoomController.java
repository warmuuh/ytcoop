package com.github.warmuuh.ytcoop.room.support;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.github.warmuuh.ytcoop.provider.ProviderId;
import com.github.warmuuh.ytcoop.provider.support.ProviderService;
import com.github.warmuuh.ytcoop.room.ParticipantState;
import com.github.warmuuh.ytcoop.room.Room;
import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.security.AuthUtil;
import com.github.warmuuh.ytcoop.video.VideoDetails;

@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	RoomService service;
	
	@Autowired
	ProviderService videoProvider;
	
	@Value("${ytcoop.feedbackurl}")
	String feedbackUrl;
	
	
	/**
	 * just for debugging purposes
	 * 
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value="host/{roomId}", method=RequestMethod.GET)
	public ModelAndView hostRoom(@PathVariable("roomId") String roomId){
		ModelAndView mav = showRoom(roomId);
		mav.addObject("isHost", true);
		
		return mav;
	}
	
	/**
	 * just for debugging purposes
	 * 
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value="client/{roomId}", method=RequestMethod.GET)
	public ModelAndView joinRoom(@PathVariable("roomId") String roomId){
		ModelAndView mav = showRoom(roomId);
		mav.addObject("isHost", false);
		
		return mav;
	}
	
	@RequestMapping(value="/{roomId}", method=RequestMethod.GET)
	public ModelAndView showRoom(@PathVariable("roomId") String roomId){
		
		Room room = service.addCurrentUserAsParticipant(roomId);
		
		ModelAndView mav = new ModelAndView("room/view");
		mav.addObject("room", room);
		
		mav.addObject("profile", AuthUtil.getUserProfile());
		
		boolean isHost =  AuthUtil.getUserId().equals(room.getHostUserId());
		mav.addObject("isHost", isHost);
		
//		if (!isHost){
//			sendJoinNotification(roomId);
//		}
		mav.addObject("feedbackurl", feedbackUrl);
		return mav;
	}
	
	
	@RequestMapping(value="/new")
	public String getRoom(@RequestParam("id") String ytId, @RequestParam("provider") ProviderId provider){
		Optional<VideoDetails> details = videoProvider.loadDetails(ytId,provider);
		if (details.isPresent()){
			Room room = service.createNewRoom(details.get());
			return "redirect:/room/" + room.getId();
		}
		else {
			throw new RuntimeException("Video not found");
		}
	}
	
	
//	public void sendJoinNotification(String roomid){ 
//		ParticipantState state = new ParticipantState("JOINED");
//		SocialAuthenticationToken authentication = (SocialAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//		Connection<?> connection = authentication.getConnection();
//
//		UserProfile profile = new UserProfile();
//		profile.setDisplayName(connection.getDisplayName());
//		profile.setImageUrl(connection.getImageUrl());
//		profile.setUserId(connection.getKey().getProviderUserId());
//		state.setSender(profile);
//
//		messageTemplate.convertAndSend("/topic/room/"+roomid+"/participants", state);
//	}
	
}
