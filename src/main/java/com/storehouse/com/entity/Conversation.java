package com.storehouse.com.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Conversation {
	
	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long conversionId;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user1_id")
	    private User user1;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user2_id")
	    private User user2;
	    
	    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Message> messages = new ArrayList<>();
	    
	
	    
	  

}
