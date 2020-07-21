/*package com.fasi.radditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fasi.radditclone.dto.PostRequest;
import com.fasi.radditclone.dto.PostResponse;
import com.fasi.radditclone.model.Post;
import com.fasi.radditclone.model.Subreddit;
import com.fasi.radditclone.model.User;

@Mapper(componentModel="spring")
public interface PostMapper {

	   


	    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	    @Mapping(target="description",expression="postRequest.description")
	    public Post map(PostRequest postRequest, Subreddit subreddit, User user);

	    @Mapping(target = "id", source = "postId")
	    @Mapping(target = "subredditName", source = "subreddit.name")
	    @Mapping(target = "userName", source = "user.username")
	    public  PostResponse mapToDto(Post post);

	   
}
*/