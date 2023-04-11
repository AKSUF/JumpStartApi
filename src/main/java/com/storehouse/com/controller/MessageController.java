package com.storehouse.com.controller;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storehouse.com.dto.ConversationDto;
import com.storehouse.com.dto.MessageDto;
import com.storehouse.com.entity.Conversation;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.messageService;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
	@Autowired
	private messageService messageService;
	@Autowired
	private ModelMapper modelmapper;
	   @Autowired
	    private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private JwtUtils jwtUtils;
	  @PostMapping("/sendMessage/{conversationId}/{receiverId}")
	    public  MessageDto sendMessage(@PathVariable Long conversationId,@RequestBody MessageDto messageDto,Long senderId,@PathVariable Long receiverId,HttpServletRequest request) {
		
		  ConversationDto conversationDto=messageService.getConversionById(conversationId);
		  if(conversationDto==null) {
			  conversationDto=messageService.createConversation(receiverId,senderId,(jwtUtils.getJWTFromRequest(request)));
			 
		  }
		  
		  Conversation conversation=this.modelmapper.map(conversationDto, Conversation.class);
		  messageDto.setConversation(conversation);
		  MessageDto saveMessageDto=messageService.createMessage(messageDto,jwtUtils.getJWTFromRequest(request),conversationId);
		  simpMessagingTemplate.convertAndSend(conversationId.toString(), saveMessageDto);
			return saveMessageDto;
	       
	    }
	    

	

}
