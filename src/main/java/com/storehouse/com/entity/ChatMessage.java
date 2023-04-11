package com.storehouse.com.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.storehouse.com.status.MessageType;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table
@Getter
@Setter
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private MessageType type;
    private String content;
    private String sender;

}
