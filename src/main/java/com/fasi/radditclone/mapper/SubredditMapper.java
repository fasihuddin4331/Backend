package com.fasi.radditclone.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fasi.radditclone.dto.SubredditDto;
import com.fasi.radditclone.model.Post;
import com.fasi.radditclone.model.Subreddit;


@Mapper(componentModel="spring")
public interface SubredditMapper {

	@Mapping(target="numberOfPost",expression="java(mapPosts(subreddit.getPosts()))")
	SubredditDto mapSubredditToDto(Subreddit subreddit);
	
	default Integer mapPosts(List<Post> posts) {
		return posts.size();
	}
	@InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subreddit);
}
