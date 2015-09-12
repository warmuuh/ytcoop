package com.github.warmuuh.ytcoop.room;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.github.warmuuh.ytcoop.video.VideoDetails;

import lombok.Data;

@Data
@Entity
public class Room {
	
	@Id
	@GenericGenerator(name="uuid-generator", strategy="org.hibernate.id.UUIDGenerator")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="uuid-generator")
	private String id;
	
	private String name;
	
	private String hostUserId;
	
	@Embedded
	private VideoDetails video;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn( referencedColumnName = "id")
	private List<UserProfile> participants = new ArrayList<>(); 
	
	@OneToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	private List<RoomConnection> connections = new ArrayList<>(); 
	
}
