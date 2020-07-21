package com.fasi.radditclone.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasi.radditclone.dto.SubredditDto;
import com.fasi.radditclone.service.SubRedditService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

	private final SubRedditService subRedditService;

	@PostMapping
	public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subRedditDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(subRedditService.save(subRedditDto));

	}

	@GetMapping
	public ResponseEntity<List<SubredditDto>> getAllSubReddit() {
		return ResponseEntity.status(HttpStatus.OK).body(subRedditService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SubredditDto> getPostsBySubreddit(@PathVariable Long id) {
		log.info("getPostsBySubreddit(Long id)",SubredditController.class,id);
		return ResponseEntity.status(HttpStatus.OK).body(subRedditService.getSubreddit(id));
	}
}
