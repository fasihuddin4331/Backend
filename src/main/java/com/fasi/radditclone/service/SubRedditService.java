package com.fasi.radditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasi.radditclone.dto.SubredditDto;
import com.fasi.radditclone.dto.SubredditDto.SubredditDtoBuilder;
import com.fasi.radditclone.exception.SpringRedditException;
import com.fasi.radditclone.mapper.SubredditMapper;
import com.fasi.radditclone.model.Subreddit;
import com.fasi.radditclone.model.Subreddit.SubredditBuilder;
import com.fasi.radditclone.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SubRedditService {

	private final SubredditRepository subredditRepository;

	/*@Autowired
	private final SubredditMapper subredditMapper;*/

	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		log.info("Entered getAll()", SubRedditService.class);
		return subredditRepository.findAll().stream().map(this::mapSubredditToDto)
				.collect(Collectors.toList());

	}

	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subreddit = subredditRepository.save(this.mapDtoToSubreddit((subredditDto)));
		subredditDto.setId(subreddit.getId());
		return subredditDto;
	}

	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
		return mapSubredditToDto(subreddit);
	}
	
	 public SubredditDto mapSubredditToDto(Subreddit subreddit) {
	        if ( subreddit == null ) {
	            return null;
	        }

	        SubredditDtoBuilder subredditDto = SubredditDto.builder();

	        subredditDto.id( subreddit.getId() );
	        subredditDto.name( subreddit.getName() );
	        subredditDto.description( subreddit.getDescription() );

	        subredditDto.numberOfPost(subreddit.getPosts().size());

	        return subredditDto.build();
	    }
	 
	 public Subreddit mapDtoToSubreddit(SubredditDto subreddit) {
	        if ( subreddit == null ) {
	            return null;
	        }

	        SubredditBuilder subreddit1 = Subreddit.builder();

	        subreddit1.id( subreddit.getId() );
	        subreddit1.name( subreddit.getName() );
	        subreddit1.description( subreddit.getDescription() );

	        return subreddit1.build();
	    }
	 
	 
}
