package com.fasi.radditclone.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDto {

	private Long id;
	private String name;
	private String description;
	private Integer numberOfPost;
}
