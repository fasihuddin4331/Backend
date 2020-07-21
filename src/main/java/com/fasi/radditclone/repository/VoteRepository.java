package com.fasi.radditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasi.radditclone.model.Post;
import com.fasi.radditclone.model.User;
import com.fasi.radditclone.model.Vote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}