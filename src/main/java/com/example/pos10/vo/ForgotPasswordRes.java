package com.example.pos10.vo;

import java.time.LocalDateTime;

public class ForgotPasswordRes extends BasicRes {
	
    private String verificationCode;
    
    private LocalDateTime verificationCodeExpiry;

    public ForgotPasswordRes() {
        super();
    }

    public ForgotPasswordRes(int code, String message, String verificationCode, LocalDateTime verificationCodeExpiry) {
        super(code, message);
        this.verificationCode = verificationCode;
        this.verificationCodeExpiry = verificationCodeExpiry;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getVerificationCodeExpiry() {
        return verificationCodeExpiry;
    }

    public void setVerificationCodeExpiry(LocalDateTime verificationCodeExpiry) {
        this.verificationCodeExpiry = verificationCodeExpiry;
    }
}
