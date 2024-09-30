package com.example.pos10.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Member;


@Repository
public interface MemberDao extends JpaRepository<Member, Integer> {

	//確認電話是否註冊過
	@Query(value = "SELECT COUNT(*) FROM member WHERE phone = :phone", nativeQuery = true)
    public int phoneExists(@Param("phone") String phone);


	// 新增會員
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO member (pwd, name, phone, birthday,email, total_spending_amount, member_level) " +
                   "VALUES (:pwd, :name, :phone, :birthday,:email, :totalSpendingAmount, :memberLevel)", nativeQuery = true)
    public int insertMember(
            @Param("pwd") String pwd,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("birthday") LocalDate birthday,
            @Param("email") String email,
            @Param("totalSpendingAmount") Integer totalSpendingAmount,
            @Param("memberLevel") String memberLevel);
    
    //用電話抓密碼和ID 
    @Query(value = "SELECT member_id, pwd from member WHERE phone = :phone " , nativeQuery = true)
    public List<Object[]> CheckLogin(@Param("phone") String phone);

    //抓會員資料
    @Query(value = "SELECT * FROM member WHERE member_id = :memberId", nativeQuery = true)
    public Optional<Member> findByMemberId(@Param("memberId") int memberId);

    //產生驗證碼
    @Modifying
    @Transactional
    @Query( value = "UPDATE member  SET verification_code = :verificationCode, verification_code_expiry = :expiry WHERE phone = :phone",nativeQuery = true)
    public int updateVerificationCode(
        @Param("verificationCode") String verificationCode,
        @Param("expiry") LocalDateTime expiry,
        @Param("phone") String phone
    );
    
    ///更新密碼
    @Modifying
    @Transactional
    @Query( value = "UPDATE member  SET pwd = :pwd  WHERE phone = :phone",nativeQuery = true)
    public int retsetPassword(
        @Param("pwd") String pwd,
        @Param("phone") String phone
    );
    
    //確認輸入的手機和email是同一個人
    @Query(value = "SELECT COUNT(*) FROM member WHERE phone = :phone AND email = :email", nativeQuery = true)
    public int checkEmail(@Param("phone") String phone, @Param("email") String email);
    
    
    
    
    
    
}
