package com.github.warmuuh.ytcoop.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SocialUserDetailsService;

import com.github.warmuuh.ytcoop.room.UserProfile;

public class TokenAuthenticationService {
 
	private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	private static final String AUTH_COOKIE_NAME = "AUTH-TOKEN";
	private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

	@Autowired
	private TokenHandler tokenHandler;
 
	
	public void addAuthentication(HttpServletResponse response, UserProfileAuthentication authentication) {
		final UserProfile user = authentication.getDetails();
		//user.setExpires(System.currentTimeMillis() + TEN_DAYS);
		final String token = tokenHandler.createTokenForUser(user);

		// Put the token into a cookie because the client can't capture response
		// headers of redirects / full page reloads.
		// (Its reloaded as a result of this response triggering a redirect back to "/")
		response.addCookie(createCookieForToken(token));
	}

	public UserProfileAuthentication getAuthentication(HttpServletRequest request) {
		// to prevent CSRF attacks we should use a custom HEADER, but for now, we just use the cookie again
		// (it is up to the client to read our previously set cookie and put it in the header)
		
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(AUTH_COOKIE_NAME)){
				String token = cookie.getValue();
				if (token != null) {
					final UserProfile user = tokenHandler.parseUserFromToken(token);
					if (user != null) {
						return new UserProfileAuthentication(user);
					}
				}
			}
		}
		
		return null;
	}

	private Cookie createCookieForToken(String token) {
		final Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, token);
		authCookie.setPath("/");
		return authCookie;
	}
}