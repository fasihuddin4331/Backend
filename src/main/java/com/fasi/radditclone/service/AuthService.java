package com.fasi.radditclone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasi.radditclone.dto.AuthenticationResponse;
import com.fasi.radditclone.dto.LoginRequest;
import com.fasi.radditclone.dto.RefreshTokenRequest;
import com.fasi.radditclone.dto.RegisterUser;
import com.fasi.radditclone.exception.SpringRedditException;
import com.fasi.radditclone.model.NotificationEmail;
import com.fasi.radditclone.model.User;
import com.fasi.radditclone.model.VerificationToken;
import com.fasi.radditclone.repository.UserRepository;
import com.fasi.radditclone.repository.VerificationTokenRepository;
import com.fasi.radditclone.security.JwtProvider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

	private final PasswordEncoder passwordencoder;
	
	private final UserRepository userRepository;
	
	private final VerificationTokenRepository verificationTokenrepository;
	
	private final MailService mailSrvice;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtProvider jwtProvider;
	
	private final RefreshTokenService refreshTokenService;
	
	@Transactional
	public void signUp(RegisterUser registerUser) {
		log.info("Entered in signUp(RegisterUser registerUser): ",AuthService.class);
		User user=new User();
		user.setEmail(registerUser.getEmail());
		user.setUsername(registerUser.getUsername());
		user.setPassword(passwordencoder.encode(registerUser.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		userRepository.save(user);
		log.info("User saved in signUp(RegisterUser registerUser): ",AuthService.class);
		String token = generateVerificationToken(user);
		mailSrvice.sendMail(new NotificationEmail(
				"Please Activate your account",
				user.getEmail(),
				"Thanks for Signup Activate your account by clicking below link :"
				+ "http://localhost:9090/api/auth/accountVarification/"+token
				));
		log.info("Executed Succesfully signUp(RegisterUser registerUser): ",AuthService.class);
	}

	private String generateVerificationToken(User user) {
		// TODO Auto-generated method stub
		String verificationToken=UUID.randomUUID().toString();
		VerificationToken token=new VerificationToken();
		token.setToken(verificationToken);
		token.setUser(user);
		verificationTokenrepository.save(token);
		return verificationToken;
		
	}
	
	@Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

	public void verifyToken(String token) {
		
		Optional<VerificationToken> token2 = verificationTokenrepository.findByToken(token);
		
		token2.orElseThrow(()->new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(token2.get());
		
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		Long userId = verificationToken.getUser().getUserId();
		User user = userRepository.findById(userId).orElseThrow(()->new SpringRedditException("User Not found"));
		user.setEnabled(true);
		userRepository.save(user);
		
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(),
				loginRequest.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate);
		/*return new AuthenticationResponse(token, loginRequest.getUsername(),);*/
		
		return AuthenticationResponse.builder()
									.expiredAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
									.authenticationToken(token)
									.refreshToken(refreshTokenService.generateRefreshToken().getToken())
									.username(loginRequest.getUsername())
									.build();
	}

	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenByUserName(refreshTokenRequest.getUserName());
		return AuthenticationResponse.builder()
				.expiredAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.authenticationToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.username(refreshTokenRequest.getUserName())
				.build();
	}
}
