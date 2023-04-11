package com.storehouse.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.storehouse.com.entity.Conversation;
import com.storehouse.com.entity.User;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {



	@Query("SELECT c FROM Conversation c WHERE c.user1 = :receiver OR c.user2 = :sender")
    Conversation findByUser(@Param("receiver") User receiver, @Param("sender") User sender);



}
