package com.github.warmuuh.ytcoop.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.security.SocialAuthenticationToken;

import com.github.warmuuh.ytcoop.room.UserProfile;

public class AuthUtil {

	public static String getUserId(){
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if (authentication instanceof SocialAuthenticationToken){
			 Connection<?> curUserCon = ((SocialAuthenticationToken) authentication).getConnection();
			 return curUserCon.getKey().getProviderUserId();
		 }
		
		 if (authentication instanceof UserProfileAuthentication){
			 return authentication.getName();
		 }
		 
		 throw new IllegalArgumentException("Unknown token class: " + authentication.getClass());
	}
	
	public static UserProfile getUserProfile(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if (authentication instanceof UserProfileAuthentication){
			 return ((UserProfileAuthentication)authentication).getDetails();
		 }
		 throw new IllegalArgumentException("Illegal token class: " + authentication.getClass());
	}
}
