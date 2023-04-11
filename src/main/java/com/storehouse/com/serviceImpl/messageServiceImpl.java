package com.storehouse.com.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storehouse.com.dto.ConversationDto;
import com.storehouse.com.dto.MessageDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Conversation;
import com.storehouse.com.entity.Message;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.ConversationRepository;
import com.storehouse.com.repository.MessageRepository;
import com.storehouse.com.repository.UserRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.messageService;

@Service
public class messageServiceImpl implements messageService {

	@Autowired
	private ConversationRepository conversationRepository;
	@Autowired
	private ModelMapper modelmapper;

	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private ConversationRepository conversatioRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	public ConversationDto getConversionById(Long conversationId) {
		Conversation conversation = this.conversationRepository.findById(conversationId).orElseThrow(
				() -> new ResourceNotFoundException("Conversation", "conversation", conversationId.toString()));
		return this.modelmapper.map(conversation, ConversationDto.class);
	}

	@Override
	public ConversationDto createConversation(Long receiverId, Long senderId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		User receiver = userRepository.findById(receiverId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource", "resource Id", receiverId.toString()));
		User sender = account.getUser();
		Conversation conversation = conversatioRepository.findByUser(receiver, sender);
		if (conversation != null) {
			return new ConversationDto(conversation);

		} else {
			System.out.println("Your conversion is exists");
		}
		conversation.setUser1(sender);
		conversation.setUser2(receiver);

		return this.modelmapper.map(conversation, ConversationDto.class);

	}

	@Override
	public MessageDto createMessage(MessageDto messageDto, String token, Long conversationId) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		User user = account.getUser();
		Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(
				() -> new ResourceNotFoundException("Conversion", "conversionId", conversationId.toString()));
		Message newMessage = this.modelmapper.map(messageDto, Message.class);
		newMessage.setConversation(conversation);
		newMessage.setSender(user);
		newMessage.setReceiver(user);
		Message message = messageRepository.save(newMessage);
		return this.modelmapper.map(message, MessageDto.class);
	}

}
