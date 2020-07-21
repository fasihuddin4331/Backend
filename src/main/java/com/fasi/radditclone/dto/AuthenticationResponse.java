package com.fasi.radditclone.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class AuthenticationResponse {

	private String authenticationToken;
	private String username;
	private String refreshToken;
	private Instant expiredAt;
	
	
}
