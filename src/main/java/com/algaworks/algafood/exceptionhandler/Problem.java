package com.algaworks.algafood.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(value = Include.NON_NULL)
public class Problem {

	private Integer status;
	private String type;
	private String title;
	private String detail;	
	private String userMessages;
	private LocalDateTime timestamp;
	
	private List<Field> fields;
	
	@Getter
	@Builder
	public static class Field {
		private String name;
		private String userMessage;
	}
	
}
