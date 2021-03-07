package com.web.curation.model.entity;

import javax.persistence.Column;
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
public class Naegi {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int nno;
	
	private String ntitle;
	private int ngno;
	private int nmaster;
	private String ncontent;
	private String nreward;
	private String nenddate;
	@Column(insertable=false)
	private int nresult;//0:아직결과안나옴,1:성공,2:실패,3:무승부(취소)

}
