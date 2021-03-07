package com.web.curation.model.entity;

import javax.persistence.Column;

public class FeedDto {
	private int ano;
	private String aurl;
	private String asummary;
	private String adate;
	private int createUser;
	private String createUserName;
	private String profileImg;
	private int aurlNo;
	public int getAurlNo() {
		return aurlNo;
	}
	public void setAurlNo(int aurlNo) {
		this.aurlNo = aurlNo;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public String getAurl() {
		return aurl;
	}
	public void setAurl(String aurl) {
		this.aurl = aurl;
	}
	public String getAsummary() {
		return asummary;
	}
	public void setAsummary(String asummary) {
		this.asummary = asummary;
	}
	public String getAdate() {
		return adate;
	}
	public void setAdate(String adate) {
		this.adate = adate;
	}
	public int getCreateUser() {
		return createUser;
	}
	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	
	@Override
	public String toString() {
		return "FeedDto [ano=" + ano + ", aurl=" + aurl + ", asummary=" + asummary + ", adate=" + adate
				+ ", createUser=" + createUser + ", createUserName=" + createUserName + ", profileImg=" + profileImg
				+  "]";
	}

	
	

}
