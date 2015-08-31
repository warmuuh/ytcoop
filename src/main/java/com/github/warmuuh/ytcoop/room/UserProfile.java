package com.github.warmuuh.ytcoop.room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class UserProfile {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	String id;
	
	String userId;
	String imageUrl;
	String displayName;
}
