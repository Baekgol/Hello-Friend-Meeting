package com.web.curation.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupApply {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int gano;
	
	private int uno;
	private int gno;
	private boolean aisApply;
	//true : 신청한거, false : 초대받은거

}
