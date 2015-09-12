package com.github.warmuuh.ytcoop.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class UserProfile {

	@Id
	@GeneratedValue
	String id;
	
	@Column(name="USER_ID")
	String userId;
	String imageUrl;
	String displayName;
}
