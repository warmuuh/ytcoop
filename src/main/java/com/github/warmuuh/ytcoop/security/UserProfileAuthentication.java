package com.github.warmuuh.ytcoop.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.github.warmuuh.ytcoop.room.UserProfile;

public class UserProfileAuthentication implements Authentication {
 
    private final UserProfile user;
    private boolean authenticated = true;
 
    public UserProfileAuthentication(UserProfile user) {
        this.user = user;
    }
 
    @Override
    public String getName() {
        return user.getUsername();
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }
 
    @Override
    public Object getCredentials() {
        return user.getPassword();
    }
 
    @Override
    public UserProfile getDetails() {
        return user;
    }
 
    @Override
    public Object getPrincipal() {
        return user.getUsername();
    }
 
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
 
    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}