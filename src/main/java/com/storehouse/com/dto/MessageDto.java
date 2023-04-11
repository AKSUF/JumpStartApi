package com.storehouse.com.dto;

import java.time.LocalDateTime;

import com.storehouse.com.entity.Conversation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class MessageDto {
	private Long messageId;
	private UserDto sender;
	private UserDto receiver;
	private String content;
	private LocalDateTime sentTime;
	private Conversation conversation;

}
