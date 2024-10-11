package com.example.pos10.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Options;

@Repository
public interface OptionsDao extends JpaRepository<Options, Integer> {
	
	// 搜尋PK組合 (title、Id)
	@Query(value = "select count(1) from options where "
			+ " option_title = :optionTitle and "
			+ " cg_id = :categoryId", //
			nativeQuery = true)
	public int selectOption(@Param("optionTitle") String optionTitle, @Param("categoryId") int categoryId);
	
	// 搜尋PK組合 (title、id、content)
	@Query(value = "select count(1) from options where "
			+ " option_title = :optionTitle and "
			+ " cg_id = :categoryId and "
			+ " option_content = :optionContent", //
			nativeQuery = true)
	public int selectOptionThree(@Param("optionTitle") String optionTitle, @Param("categoryId") int categoryId, //
			@Param("optionContent") String optionContent);
	
	@Query(value = "SELECT * FROM options", nativeQuery = true)
	public List<Options> selectAll();
	
	//新增資料
	@Modifying
	@Transactional
	@Query(value="insert into options "
			+ " (option_title, cg_id, option_content, option_type, extra_price) values "
			+ " (:optionTitle, :categoryId, :optionContent, :optionType, :extraPrice)", nativeQuery = true)
	public int insert(//
			@Param("optionTitle") String optionTitle, //
			@Param("categoryId") int categoryId, //
			@Param("optionContent") String optionContent, //
			@Param("optionType") String optionType, //
			@Param("extraPrice") int extraPrice);
	
	// 編輯客製化選項價錢
	@Modifying
	@Transactional
	@Query(value = "update options "
			+ " set "
			+ " extra_price = :extraPrice, option_type = :optionType "
			+ " where "
			+ " option_title = :optionTitle and "
			+ " cg_id = :categoryId and "
			+ " option_content = :optionContent", nativeQuery = true)
	public int updateOptionPrice(//
			@Param("optionTitle") String optionTitle, //
			@Param("categoryId") int categoryId, //
			@Param("optionContent") String optionContent, //
			@Param("optionType") String optionType,//
			@Param("extraPrice") int extraPrice);
	
	// 刪除資料
	@Modifying
	@Transactional
	@Query(value = "delete from options where option_title = :optionTitle and cg_id = :categoryId", //
	nativeQuery = true)
	public int deleteOption(//
			@Param("optionTitle") String optionTitle, //
			@Param("categoryId") int categoryId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM options "
			+ " WHERE cg_id = :categoryId", nativeQuery = true)
	public int deleteOpByCgId(@Param("categoryId") int categoryId);
	
	
	
	
	
	
	
	
	
}
