package com.example.pos10.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Categories;

@Repository
public interface CategoriesDao extends JpaRepository<Categories, Integer> {

	// select categoryId已存在，DB的名稱是category_id，java端的名稱為categoryId
	@Query(value = "SELECT COUNT(1) FROM categories WHERE category_id = :categoryId", nativeQuery = true)
	public int optionExists(@Param("categoryId") int categoryId);

	// select 分類名稱是否存在，回傳搜尋結果相同的數量
	@Query(value = "select count(1) from categories where category = :categoryName", nativeQuery = true)
	public int cgNameExists(@Param("categoryName") String categoryName);
	
	@Query(value = "select * from categories", nativeQuery = true)
	public List<Categories> selectAll();

	// 新增資料
	@Modifying
	@Transactional
	@Query(value = "insert into categories (category) values (:category)", nativeQuery = true)
	public int insert(@Param("category") String category);

	// 編輯資料(update)
	@Modifying
	@Transactional
	@Query(value = "UPDATE categories SET category = :category WHERE category_id = :categoryId", nativeQuery = true)
	public int updateCg(//
			@Param("categoryId") int categoryId, //
			@Param("category") String category);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE categories "
			+ " SET workstation_id = :workstationId "
			+ " WHERE category_id = :categoryId", nativeQuery = true)
	public int updateWorkIdFromCg(//
			@Param("categoryId") int categoryId, //
			@Param("workstationId") int workstationId);

	// 刪除資料(delete)
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM categories WHERE category_id = :categoryId", nativeQuery = true)
	public int deleteCgById(@Param("categoryId") int categoryId);

}
