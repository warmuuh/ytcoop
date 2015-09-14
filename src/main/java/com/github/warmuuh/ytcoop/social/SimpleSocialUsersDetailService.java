package com.github.warmuuh.ytcoop.social;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.room.support.UserRepository;

@Service
public class SimpleSocialUsersDetailService implements SocialUserDetailsService, UserDetailsService{


	@Autowired
	UserRepository users;
	
    @Override
    public final SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
    	Optional<UserProfile> user = users.findOneByUserId(userId);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("user not found");
        }
        return user.get();
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return loadUserByUserId(username);
	}

}