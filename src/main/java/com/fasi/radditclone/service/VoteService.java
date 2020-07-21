package com.fasi.radditclone.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasi.radditclone.dto.VoteDto;
import com.fasi.radditclone.exception.PostNotFoundException;
import com.fasi.radditclone.exception.SpringRedditException;
import com.fasi.radditclone.model.Post;
import com.fasi.radditclone.model.Vote;
import com.fasi.radditclone.model.VoteType;
import com.fasi.radditclone.repository.PostRepository;
import com.fasi.radditclone.repository.VoteRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

;

@Service
@AllArgsConstructor
@Data
@Transactional
public class VoteService {

	private final AuthService authService;

	private final VoteRepository voteRepository;

	private final PostRepository postRepository;

	public void vote(VoteDto voteDto) {

		Post post = postRepository.findById(voteDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
		Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc = voteRepository
				.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

		System.out.println("Before if:"+voteDto.getType());
		if (findTopByPostAndUserOrderByVoteIdDesc.isPresent()
				&& findTopByPostAndUserOrderByVoteIdDesc.get().getVoteType().equals(voteDto.getType())) {
			System.out.println("Exception if");
			throw new SpringRedditException("You have already " + voteDto.getType() + "'d for this post");
		}

		if (VoteType.UPVOTE.equals(voteDto.getType())) {
			System.out.println("UpVote if");
			post.setVoteCount(post.getVoteCount() + 1);
		}
		if (VoteType.DOWNVOTE.equals(voteDto.getType())) {
			post.setVoteCount(post.getVoteCount() - 1);
		}

		voteRepository.save(mapToVote(post, voteDto));
		postRepository.save(post);
	}

	private Vote mapToVote(Post post, VoteDto voteDto) {
		return Vote.builder().voteType(voteDto.getType()).post(post).user(authService.getCurrentUser()).build();

	}

}