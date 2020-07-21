package com.fasi.radditclone.dto;

import com.fasi.radditclone.model.VoteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoteDto {
	
	private VoteType type;
	private Long postId;

}
