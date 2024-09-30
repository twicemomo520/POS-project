package com.example.pos10.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @NotNull
    @Column(name = "pwd", length = 100, nullable = false)
    private String pwd;

    @NotNull
    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @NotNull
    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @NotNull
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @NotNull
    @Column(name = "total_spending_amount", nullable = false)
    private Integer totalSpendingAmount;

    @NotNull
    @Column(name = "member_level", length = 20, nullable = false)
    private String memberLevel;

    @Column(name = "verification_code", length = 6)
    private String verificationCode;

    @Column(name = "verification_code_expiry")
    private LocalDateTime verificationCodeExpiry;

    @Column(name = "email")
    private String email;

    // 礚把计篶硑ㄧ计
    public Member() {
    }

    // 把计篶硑ㄧ计
    public Member(Integer memberId, String pwd, String name, String phone, LocalDate birthday,
                  Integer totalSpendingAmount, String memberLevel, String verificationCode,
                  LocalDateTime verificationCodeExpiry, String email) {
        this.memberId = memberId;
        this.pwd = pwd;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.totalSpendingAmount = totalSpendingAmount;
        this.memberLevel = memberLevel;
        this.verificationCode = verificationCode;
        this.verificationCodeExpiry = verificationCodeExpiry;
        this.email = email;
    }

    // Getters and Setters
    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Integer getTotalSpendingAmount() {
        return totalSpendingAmount;
    }

    public void setTotalSpendingAmount(Integer totalSpendingAmount) {
        this.totalSpendingAmount = totalSpendingAmount;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
