package com.example.pos10.vo;

public class CheckoutDetailRes  extends BasicRes{

	private Object data;

	public CheckoutDetailRes() {
		super();
	}

	public CheckoutDetailRes(int code, String message,Object data) {
		super(code, message);
		this.data = data;
	}

	public CheckoutDetailRes(Object data) {
		super();
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	

}
