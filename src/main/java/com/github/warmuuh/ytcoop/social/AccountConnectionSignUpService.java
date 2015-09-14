package com.github.warmuuh.ytcoop.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import com.github.warmuuh.ytcoop.room.UserProfile;
import com.github.warmuuh.ytcoop.room.support.UserRepository;

public class AccountConnectionSignUpService implements ConnectionSignUp {

	
	@Autowired
	UserRepository users;
	
	
    public String execute(Connection<?> connection) {
//        UserProfile profile = connection.fetchUserProfile();
    	UserProfile profile = new UserProfile();
    	profile.setDisplayName(connection.getDisplayName());
    	profile.setUserId(connection.getKey().getProviderUserId());
    	profile.setImageUrl(connection.getImageUrl());
    	profile = users.save(profile);
        return profile.getUserId();
    }

}