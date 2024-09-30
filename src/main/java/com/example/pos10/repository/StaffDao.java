package com.example.pos10.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Staff;

@Repository
public interface StaffDao extends JpaRepository<Staff, String> {

	// 確認電話是否註冊過
	@Query(value = "SELECT COUNT(*) FROM staff WHERE phone = :phone", nativeQuery = true)
	public int phoneExists(@Param("phone") String phone);
	
	// 確認員工編號是否註冊過
		@Query(value = "SELECT COUNT(*) FROM staff WHERE staff_number = :staffNumber", nativeQuery = true)
		public int staffNumberExists(@Param("staffNumber") String staffNumber);
		
		 //用員工編號抓密碼
	    @Query(value = "SELECT  pwd from staff WHERE staff_number = :staffNumber " , nativeQuery = true)
	    public String CheckLogin(@Param("staffNumber") String staffNumber);

	// 回傳全部員工
	@Query(value = "SELECT * FROM staff ", nativeQuery = true)
	public List<Staff> allStaffInfo();

	// 新增員工
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO staff (staff_number,pwd, name, phone, authorization , email) "
			+ "VALUES (:staffNumber,:pwd, :name, :phone, :authorization, :email)", nativeQuery = true)
	public int insertStaff(@Param("staffNumber") String staffNumber, @Param("pwd") String pwd,
			@Param("name") String name, @Param("phone") String phone, @Param("authorization") String authorization,
			@Param("email") String email);

	// 抓最新的員工編號
	@Query(value = "SELECT staff_number FROM staff ORDER BY staff_number DESC LIMIT 1", nativeQuery = true)
	public String findLastStaffNumber();

	// 更新員工資料
	@Modifying
	@Transactional
	@Query(value = "UPDATE staff  SET name = :name , phone = :phone , authorization = :authorization , email = :email "
			+ "WHERE staff_number = :staffNumber", nativeQuery = true)
	public int updateStaff(@Param("name") String name, @Param("phone") String phone,
			@Param("authorization") String authorization, @Param("email") String email,
			@Param("staffNumber") String staffNumber);

	//刪除員工資料
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM staff WHERE staff_number = :staffNumber", nativeQuery = true)
	public int deleteStaff(@Param("staffNumber") String staffNumber);
	
	
	//抓員工資料
    @Query(value = "SELECT * FROM staff WHERE staff_number = :staffNumber", nativeQuery = true)
    public Optional<Staff> findByStaffNumber(@Param("staffNumber") String staffNumber);
	
	
	
	
	

}
