package com.example.pos10.repository;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Staff;

@Repository
public interface StaffDao extends JpaRepository<Staff, String> {

	// �T�{�q�ܬO�_���U�L
	@Query(value = "SELECT COUNT(*) FROM staff WHERE phone = :phone", nativeQuery = true)
	public int phoneExists(@Param("phone") String phone);
	
	
	// �s�W���u
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO staff (staff_number,pwd, name, phone, authorization) " +
                   "VALUES (:staffNumber,:pwd, :name, :phone, :authorization)", nativeQuery = true)
    public int insertStaff(
    		@Param("staffNumber")  String staffNumber,
            @Param("pwd") String pwd,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("authorization") String authorization);

    //��̷s�����u�s��
    @Query(value = "SELECT staff_number FROM staff ORDER BY staff_number DESC LIMIT 1", nativeQuery = true)
    public String findLastStaffNumber();

    //��s���u���
    @Modifying
    @Transactional
    @Query( value = "UPDATE staff  SET name = :name , phone = :phone , authorization = :authorization "
    		+ "WHERE staff_number = :staffNumber"  , nativeQuery = true)
    public int updateStaff(
    		@Param("name") String name,
    		@Param("phone") String phone,
    		@Param("authorization") String authorization,
    		@Param("staffNumber") String staffNumber);
    
    
    
    
    
    

}
