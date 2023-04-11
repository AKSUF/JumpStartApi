package com.storehouse.com.dto;

import java.util.ArrayList;
import java.util.List;

import com.storehouse.com.entity.Conversation;

public class ConversationDto {
   
	
	private Long conversionId;
    private UserDto user1;
    private UserDto user2;
    private List<MessageDto> messages = new ArrayList<>();
    public ConversationDto(Conversation conversation) {
	this.conversionId=conversation.getConversionId();
	//this.user1=conversation.getUser1();
	}
    
    
    
}
