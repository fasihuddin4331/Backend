package com.fasi.radditclone.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasi.radditclone.dto.AuthenticationResponse;
import com.fasi.radditclone.dto.LoginRequest;
import com.fasi.radditclone.dto.RefreshTokenRequest;
import com.fasi.radditclone.dto.RegisterUser;
import com.fasi.radditclone.service.AuthService;
import com.fasi.radditclone.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody RegisterUser registerUser) {
		authService.signUp(registerUser);
		return new ResponseEntity<>("User Registered SuccessFully", HttpStatus.OK);
	}

	@GetMapping("/accountVarification/{token}")
	public ResponseEntity<String> verifyToken(@PathVariable String token) {
		authService.verifyToken(token);
		return new ResponseEntity<String>("Account Activated Successfull", HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
		AuthenticationResponse authenticationResponse = authService.login(loginRequest);
		return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
	}
	
	@PostMapping("refresh/token")
	public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
	return authService.refreshToken(refreshTokenRequest);
	}
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
	refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
	return ResponseEntity.status(HttpStatus.OK).body("Logged out Successfully!!");
	}
}
