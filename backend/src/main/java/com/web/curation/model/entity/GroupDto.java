package com.web.curation.model.entity;

import java.sql.Blob;

import javax.persistence.Column;

public class GroupDto {
private int gno;
	
	private int gmaster;
	private String gname;
	private int gcategory;
	private String guserList;
	
	private String gdate;
	
	private int gboundary;//0:비공개(초대만), 1:친구까지 공개, 2:친구의친구까지 공개
	private String gdesc;
	
	
	private byte[] gimg;


	public int getGno() {
		return gno;
	}


	public void setGno(int gno) {
		this.gno = gno;
	}


	public int getGmaster() {
		return gmaster;
	}


	public void setGmaster(int gmaster) {
		this.gmaster = gmaster;
	}


	public String getGname() {
		return gname;
	}


	public void setGname(String gname) {
		this.gname = gname;
	}


	public int getGcategory() {
		return gcategory;
	}


	public void setGcategory(int gcategory) {
		this.gcategory = gcategory;
	}


	public String getGuserList() {
		return guserList;
	}


	public void setGuserList(String guserList) {
		this.guserList = guserList;
	}


	public String getGdate() {
		return gdate;
	}


	public void setGdate(String gdate) {
		this.gdate = gdate;
	}


	public int getGboundary() {
		return gboundary;
	}


	public void setGboundary(int gboundary) {
		this.gboundary = gboundary;
	}


	public String getGdesc() {
		return gdesc;
	}


	public void setGdesc(String gdesc) {
		this.gdesc = gdesc;
	}


	public byte[] getGimg() {
		return gimg;
	}


	public void setGimg(byte[] gimg) {
		this.gimg = gimg;
	}
	
}
