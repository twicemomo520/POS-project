package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.Workstation;

public class SearchWorkRes extends BasicRes {

	private List<Workstation> data;

	public SearchWorkRes() {
		super();
	}

	public SearchWorkRes(int code, String message) {
		super(code, message);
	}

	public SearchWorkRes(int code, String message,List<Workstation> data) {
		super(code, message);
		this.data = data;
	}

	public List<Workstation> getData() {
		return data;
	}

	public void setData(List<Workstation> data) {
		this.data = data;
	}

	
}
