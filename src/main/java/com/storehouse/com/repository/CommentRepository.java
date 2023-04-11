package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
