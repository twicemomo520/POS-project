package com.example.pos10.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entiey.Categories;

@Repository
public interface CategoriesDao extends JpaRepository<Categories, Integer> {

	// select categoryId�w�s�b�ADB���W�٬Ocategory_id�Ajava�ݪ��W�٬�categoryId
	@Query(value = "SELECT COUNT(1) FROM categories WHERE category_id = :categoryId", nativeQuery = true)
	public int optionExists(@Param("categoryId") int categoryId);
	
	// select �����W�٬O�_�s�b�A�^�Ƿj�M���G�ۦP���ƶq
	@Query(value = "select count(1) from categories where category = :categoryName", nativeQuery = true)
	public int cgNameExists(@Param("categoryName") String categoryName);

	// �s�W���
	@Modifying
	@Transactional
	@Query(value = "insert into categories (category) values (:category)", nativeQuery = true)
	public int insert(//
			@Param("category") String category);

}
