package com.example.pos10.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Waitlist;

@Repository
public interface WaitlistDao extends JpaRepository <Waitlist, Integer> {

    // 1. 儲存候位
    @Override
    <S extends Waitlist> S save (S waitlist);
    
    // 1-1 查詢最大 waitlist_order 以方便排列
    @Query("SELECT COALESCE(MAX(w.waitlistOrder), 0) FROM Waitlist w")
    public int findMaxWaitlistOrder();

    // 2. 根據顧客電話號碼查詢候位
    @Query("SELECT w FROM Waitlist w WHERE w.customerPhoneNumber = :phoneNumber")
    List <Waitlist> findByCustomerPhoneNumber (@Param ("phoneNumber") String phoneNumber);
    
    @Query("SELECT w FROM Waitlist w WHERE w.customerPhoneNumber LIKE %:lastThreeDigits")
    List <Waitlist> findByCustomerPhoneNumberEndingWith (@Param ("lastThreeDigits") String lastThreeDigits);
    
    // 3. 查詢所有候位
    @Query("SELECT w FROM Waitlist w ORDER BY w.waitlistOrder ASC")
    List <Waitlist> findAllWaitlists ();
    
    // 4. 取消候位
    @Modifying
    @Query ("DELETE FROM Waitlist w WHERE w.waitlistId = :waitlistId")
    public void cancelWaitlist (@Param ("waitlistId") int waitlistId);
    
    // 5. 獲取所有候位的數量
    @Query ("SELECT COUNT(w) FROM Waitlist w")
    public int countWaitlists ();

    // 6. 手動報到更新候位狀態 (這部分可以根據需要來設計)
    @Modifying
    @Query ("UPDATE Waitlist w SET w.waitlistOrder = 0 WHERE w.waitlistId = :waitlistId") // 假設 0 表示已報到
    public int manualCheckIn (@Param ("waitlistId") int waitlistId);
}