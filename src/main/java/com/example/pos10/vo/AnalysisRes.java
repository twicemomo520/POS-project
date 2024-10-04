package com.example.pos10.vo;

import java.util.List;

public class AnalysisRes extends BasicRes{
	
	private AnalysisVo analysis;

	public AnalysisRes() {
		super();
	}

	public AnalysisRes(int code, String message, AnalysisVo analysis) {
		super(code, message);
		this.analysis = analysis;
	}

	public AnalysisVo getAnalysis() {
		return analysis;
	}

	public void setAnalysis(AnalysisVo analysis) {
		this.analysis = analysis;
	}


	

}
