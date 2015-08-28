package com.github.warmuuh.ytcoop.site.support;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuController {

	@RequestMapping("/")
	public String getMenu(){
		return "index";
	}
}
