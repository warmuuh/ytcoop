package com.github.warmuuh.ytcoop.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.social.security.SocialUserDetailsService;

import com.github.warmuuh.ytcoop.room.UserProfile;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public final class TokenHandler {
 
    private final String secret;
    private final SocialUserDetailsService userService;
 
    public TokenHandler(String secret, SocialUserDetailsService userService) {
        this.secret = secret;
        this.userService = userService;
    }
 
    public UserProfile parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return (UserProfile) userService.loadUserByUserId(username);
    }
 
    public String createTokenForUser(UserProfile user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}