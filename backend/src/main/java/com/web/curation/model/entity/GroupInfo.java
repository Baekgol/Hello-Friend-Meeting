package com.web.curation.model.entity;

import java.sql.Blob;

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
public class GroupInfo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int gno;
	
	private int gmaster;
	private String gname;
	private int gcategory;
	private String guserList;
	
	@Column(insertable=false,updatable=false)
	private String gdate;
	
	private int gboundary;//0:비공개(초대만), 1:친구까지 공개, 2:친구의친구까지 공개
	private String gdesc;
	
	
	private Blob gimg;
}
