package com.storehouse.com.service;

import com.storehouse.com.dto.ConversationDto;
import com.storehouse.com.dto.MessageDto;
import com.storehouse.com.entity.Conversation;

public interface messageService {

	ConversationDto getConversionById(Long conversationId);

	ConversationDto createConversation(Long receiverId, Long senderId, String token);

	MessageDto createMessage(MessageDto messageDto, String token,Long conversationId);


	


}
