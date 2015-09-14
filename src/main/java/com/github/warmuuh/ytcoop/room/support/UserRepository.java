package com.github.warmuuh.ytcoop.room.support;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.github.warmuuh.ytcoop.room.UserProfile;

public interface UserRepository  extends Repository<UserProfile, String> {

	Optional<UserProfile> findOne(String id);
	Optional<UserProfile> findOneByUserId(String userId);
	
	UserProfile save(UserProfile user);
}
