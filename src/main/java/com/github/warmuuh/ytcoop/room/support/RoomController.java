package com.github.warmuuh.ytcoop.room.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.warmuuh.ytcoop.room.Room;

@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	RoomService service;
	
	
	@RequestMapping(value="host/{roomId}", method=RequestMethod.GET)
	public ModelAndView hostRoom(@PathVariable("roomId") String roomId){
		
		Room room = service.getRoom(roomId);
		ModelAndView mav = new ModelAndView("room/host");
		mav.addObject("room", room);
		
	      
		return mav;
	}
	
	@RequestMapping(value="/{roomId}", method=RequestMethod.GET)
	public ModelAndView joinRoom(@PathVariable("roomId") String roomId){
		Room room = service.getRoom(roomId);
		ModelAndView mav = new ModelAndView("room/client");
		mav.addObject("room", room);	
		return mav;
	}
	
	
	@RequestMapping(value="/new", method=RequestMethod.POST)
	public String getRoom(@RequestParam("name") String name){
		Room room = service.createNewRoom(name);
		return "redirect:/room/host/" + room.getId();
	}
	
}
