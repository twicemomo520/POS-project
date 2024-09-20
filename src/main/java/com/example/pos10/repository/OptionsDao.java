package com.example.pos10.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entiey.Options;

@Repository
public interface OptionsDao extends JpaRepository<Options, Integer> {
	
	//新增資料
	@Modifying
	@Transactional
	@Query(value="insert into options "
			+ " (option_id, option_title, cg_id, option_content, extra_price) values "
			+ " (:optionId :optionTitle :categoryId :optionContent :extraPrice)", nativeQuery = true)
	public int insert(//
			@Param("optionId") int optionId, //
			@Param("optionTitle") String optionTitle, //
			@Param("categoryId") int categoryId, //
			@Param("optionContent") String optionContent, //
			@Param("extraPrice") int extraPrice);
	
}
