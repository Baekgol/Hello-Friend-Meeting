package com.web.curation.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupChatting {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int gcno;
	
	private int gcgno;
	private int gcuno;
	private String gccontent;
	private String gcuname;
	
	@Column(insertable=false,updatable=false)
	private String gcdate;
	
}
