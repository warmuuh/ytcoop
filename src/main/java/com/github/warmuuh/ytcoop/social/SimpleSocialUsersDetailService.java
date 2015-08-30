package com.github.warmuuh.ytcoop.social;

import java.util.Arrays;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SimpleSocialUsersDetailService implements SocialUserDetailsService {

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
		return new SocialUser(userId, "", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	}

}