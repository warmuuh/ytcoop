package com.github.warmuuh.ytcoop.room;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import lombok.Data;

@Data
@Entity
public class UserProfile implements SocialUserDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	String id;
	
	@Column(name="USER_ID")
	String userId;
	
	String imageUrl;
	String displayName;
	
	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
	@Override
	@Transient
	public String getPassword() {
		return "";
	}
	@Override
	@Transient
	public String getUsername() {
		return userId;
	}
	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	@Transient
	public boolean isEnabled() {
		return true;
	}
}
