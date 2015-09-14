package com.github.warmuuh.ytcoop.site.support;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.warmuuh.ytcoop.security.UserProfileAuthentication;

@Controller
public class MenuController {

	@RequestMapping("/")
	public ModelAndView getMenu(){
		UserProfileAuthentication auth = (UserProfileAuthentication) SecurityContextHolder.getContext().getAuthentication();
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("user",  auth.getDetails().getDisplayName());
		return mav;
	}
	
	@RequestMapping("youtube.com/watch")
	public ModelAndView hostYoutube(@RequestParam("v") String videoId){
		ModelAndView mav = new ModelAndView("forward:/room/new?provider=YOUTUBE&id="+videoId);
//		mav.addObject("provider", "YOUTUBE");
//		mav.addObject("id",videoId);
		return mav;
	}
}
