package com.fasi.radditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasi.radditclone.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}