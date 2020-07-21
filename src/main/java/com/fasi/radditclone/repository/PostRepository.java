package com.fasi.radditclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasi.radditclone.model.Post;
import com.fasi.radditclone.model.Subreddit;
import com.fasi.radditclone.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}