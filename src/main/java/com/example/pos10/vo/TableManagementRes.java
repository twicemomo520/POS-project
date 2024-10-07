package com.example.POS.project.vo;

import java.util.List;

import com.example.POS.project.entity.TableManagement;

public class TableManagementRes {
	
	private int code;
	
	private String message;
	
	 private List <TableManagement> tables; // 保存桌位列表

	public TableManagementRes () {
		super ();
	}
	
	public TableManagementRes (int code, String message) {
        this.code = code;
        this.message = message;
    }

	public TableManagementRes (int code, String message, List<TableManagement> tables) {
		super();
		this.code = code;
		this.message = message;
		this.tables = tables;
	}

	public int getCode () {
		return code;
	}

	public void setCode (int code) {
		this.code = code;
	}

	public String getMessage () {
		return message;
	}

	public void setMessage (String message) {
		this.message = message;
	}

	public List <TableManagement> getTables() {
		return tables;
	}

	public void setTables (List <TableManagement> tables) {
		this.tables = tables;
	}
}
