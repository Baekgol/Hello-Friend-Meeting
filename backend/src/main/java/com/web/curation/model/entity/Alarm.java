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
public class Alarm {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ano;
	
	private String aurl;
	private int aurlNo;
	private String asummary;
	private int atype;//0:알림, 1:피드
	//알림 : 내 게시글에 달린 댓글, 친구요청, 그룹초대, 그룹 가입 신청
	//피드 : 새로생긴 그룹, 내가 소속된 그룹의 게시글, 친구가 새로 가입한 그룹, 속한 그룹의 새 일정
	//aurl에 각각 uno, gno, bno를 담도록.
	private int auser;
	@Column(insertable=false,updatable=false)
	private String adate;
	@Column(insertable=false)
	private boolean aisRead;
	private int createUser;
	//피드는 FeedDto로 리턴함

}
