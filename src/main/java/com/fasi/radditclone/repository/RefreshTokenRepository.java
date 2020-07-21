package com.fasi.radditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasi.radditclone.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}