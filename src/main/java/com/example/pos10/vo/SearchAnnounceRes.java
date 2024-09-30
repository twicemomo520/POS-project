package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.Announce;

public class SearchAnnounceRes extends BasicRes {

	private List<Announce> data;

	public SearchAnnounceRes() {
		super();
	}

	public SearchAnnounceRes(int code, String message) {
		super(code, message);
	}

	public SearchAnnounceRes(int code, String message,List<Announce> data) {
		super(code, message);
		this.data = data;
	}

	public List<Announce> getData() {
		return data;
	}

	public void setData(List<Announce> data) {
		this.data = data;
	}



}
