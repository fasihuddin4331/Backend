package com.fasi.radditclone.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasi.radditclone.dto.PostRequest;
import com.fasi.radditclone.dto.PostResponse;
import com.fasi.radditclone.exception.PostNotFoundException;
import com.fasi.radditclone.exception.SubredditNotFoundException;
import com.fasi.radditclone.model.Post;
import com.fasi.radditclone.model.Subreddit;
import com.fasi.radditclone.model.User;
import com.fasi.radditclone.repository.CommentRepository;
import com.fasi.radditclone.repository.PostRepository;
import com.fasi.radditclone.repository.SubredditRepository;
import com.fasi.radditclone.repository.UserRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;





@Service
@Data
@AllArgsConstructor
@Transactional
public class PostService {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private  SubredditRepository subredditRepository;
	
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final CommentRepository commentReository;
	
	
	
	@Autowired
	private final AuthService authService;
	// private final PostMapper postMapper;

	public PostResponse save(PostRequest postRequest) {
		Post post = null;
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
				.orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
		post = convertToPost(postRequest);
		post.setSubreddit(subreddit);
		post.setUser(authService.getCurrentUser());
		return convertFromPost(postRepository.save(post));
	}

	@Transactional(readOnly = true)
	public PostResponse getPost(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
		return convertFromPost(post);
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getAllPosts() {
		return postRepository.findAll().stream().map(this::convertFromPost).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsBySubreddit(Long subredditId) {
		Subreddit subreddit = subredditRepository.findById(subredditId)
				.orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
		List<Post> posts = postRepository.findAllBySubreddit(subreddit);
		return posts.stream().map(this::convertFromPost).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		return postRepository.findByUser(user).stream().map(this::convertFromPost).collect(Collectors.toList());
	}

	private Post convertToPost(PostRequest postRequest) {
		Post post = new Post();
		post.setCreatedDate(Instant.now());
		post.setDescription(postRequest.getDescription());
		post.setPostName(postRequest.getPostName());
		post.setUrl(postRequest.getUrl());
		post.setVoteCount(0);
		return post;
	}
	
	private PostResponse convertFromPost(Post post) {
		PostResponse postRes = new PostResponse();
		postRes.setId(post.getPostId());
		postRes.setDescription(post.getDescription());
		postRes.setPostName(post.getPostName());
		postRes.setUrl(post.getUrl());
		postRes.setUserName(post.getUser().getUsername());
		postRes.setSubredditName(post.getSubreddit().getName());
		postRes.setCommentCount(commentReository.findByPost(post).size());
		postRes.setVoteCount(post.getVoteCount());
		postRes.setDuration(TimeAgo.using(post.getCreatedDate().toEpochMilli()));
		return postRes;
	}
	
}
