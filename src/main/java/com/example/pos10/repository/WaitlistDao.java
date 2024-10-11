//package com.example.pos10.repository;
//
//import com.example.pos10.entity.Waitlist;
//
//import java.time.LocalDateTime;
//
//import javax.transaction.Transactional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface WaitlistDao extends JpaRepository <Waitlist, Integer> {
//
//    // 1. 加入等待列表: 插入客戶的姓名、聯繫方式和狀態到等待列表
//    @Modifying
//    @Transactional
//    @Query (value = "INSERT INTO waitlist (customer_name, contact_info, joined_time, status) " +
//                   "VALUES (?1, ?2, ?3, 'waiting')", nativeQuery = true)
//    public int addToWaitlist (String customerName, String contactInfo, LocalDateTime joinedTime);
//
//    // 2. 檢查是否已在等待列表中: 避免重複將同一位客戶加入等待列表
//    @Query ("SELECT COUNT(w) > 0 FROM Waitlist w WHERE w.customerName = :customerName AND w.contactInfo = :contactInfo")
//    boolean isCustomerInWaitlist (@Param ("customerName") String customerName, @Param ("contactInfo") String contactInfo);
//}