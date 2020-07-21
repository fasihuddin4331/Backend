package com.fasi.radditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasi.radditclone.model.Comment;
import com.fasi.radditclone.model.Post;
import com.fasi.radditclone.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);

}