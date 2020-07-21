package com.fasi.radditclone.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasi.radditclone.dto.CommentsDto;
import com.fasi.radditclone.exception.PostNotFoundException;
import com.fasi.radditclone.model.Comment;
import com.fasi.radditclone.model.NotificationEmail;
import com.fasi.radditclone.model.Post;
import com.fasi.radditclone.model.User;
import com.fasi.radditclone.repository.CommentRepository;
import com.fasi.radditclone.repository.PostRepository;
import com.fasi.radditclone.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@AllArgsConstructor
public class CommentService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired 
	private MailContentBuilder mailContentBuilder;
	
	@Autowired
	private MailService mailService;
	
	private static final String POST_URL = "";

	public CommentsDto createComment(CommentsDto commentsDto) {
		Post post = postRepository.findById(commentsDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException("Post Not Found By This Id : " + commentsDto.getPostId()));
		
		String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
		return convertCommentToDto(commentRepository.save(convertDtoToComment(commentsDto, post)));
	}

	private void sendCommentNotification(String message, User user) {
		 mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
		
	}

	private Comment convertDtoToComment(CommentsDto commentsDto, Post post) {
		Comment comment = new Comment();
		comment.setCreatedDate(Instant.now());
		comment.setText(commentsDto.getText());
		comment.setPost(post);
		comment.setUser(authService.getCurrentUser());
		return comment;
	}

	private CommentsDto convertCommentToDto(Comment comment) {
		CommentsDto commentDto = new CommentsDto();
		commentDto.setId(comment.getId());
		commentDto.setCreatedDate(comment.getCreatedDate());
		commentDto.setText(comment.getText());
		commentDto.setPostId(comment.getPost().getPostId());
		commentDto.setUserName(comment.getUser().getUsername());
		return commentDto;

	}

	public List<CommentsDto> getCommentByPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));

		return commentRepository.findByPost(post).stream().map(this::convertCommentToDto).collect(Collectors.toList());

	}

	public List<CommentsDto> getCommentsByUser(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new PostNotFoundException(username));
		return commentRepository.findAllByUser(user).stream().map(this::convertCommentToDto).collect(Collectors.toList());

	}
}
