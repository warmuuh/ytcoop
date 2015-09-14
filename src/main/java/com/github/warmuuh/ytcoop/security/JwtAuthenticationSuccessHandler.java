package com.github.warmuuh.ytcoop.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.social.security.SocialUserDetails;

import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.social.SimpleSocialUsersDetailService;

import lombok.Getter;


public class JwtAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	
	@Getter
	RequestCache requestCache = new HttpSessionRequestCache();
	
	
	@Autowired
	TokenAuthenticationService tokenService;
	
	@Autowired
	SimpleSocialUsersDetailService userService;
	
	public JwtAuthenticationSuccessHandler() {
		this.setRequestCache(requestCache);
	}
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		SocialUserDetails userDetails = userService.loadUserByUserId(authentication.getName());
		tokenService.addAuthentication(response, new UserProfileAuthentication((UserProfile) userDetails));
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
