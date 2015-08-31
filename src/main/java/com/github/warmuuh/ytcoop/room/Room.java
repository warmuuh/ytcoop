package com.github.warmuuh.ytcoop.room;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Room {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String name;
	
	private String hostUserId;
	
	@OneToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	private List<UserProfile> participants = new ArrayList<>(); 
}
