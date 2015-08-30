package com.github.warmuuh.ytcoop.social;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

public class AccountConnectionSignUpService implements ConnectionSignUp {

    public String execute(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();
        return profile.getUsername();
    }

}