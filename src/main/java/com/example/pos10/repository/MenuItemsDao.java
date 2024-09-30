package com.example.pos10.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.MenuItems;

@Repository
public interface MenuItemsDao extends JpaRepository<MenuItems, String> {
	
    // 判斷資料是否存在的SQL語法
	// 在menu_items裡面找輸入名稱與資料庫現有的meal_name名稱相同的資料，若找到回傳(1)，最後再count回傳幾次(1)
    @Query(value = "SELECT COUNT(1) FROM menu_items WHERE meal_name = :mealName", nativeQuery = true)
    public int existsByMealName(@Param("mealName") String mealName);
	
	// 新增資料的SQL語法
	@Modifying
	@Transactional
	@Query(value = "insert into menu_items "
			+ " (meal_name, mea_description, category_id, workstation_id, price) values"
			+ " (:mealName :mealDescription :categoryId :workstationId :price)", //
			nativeQuery = true)
	public int insert(//
			@Param("mealName") String mealName, //
			@Param("mealDescription") String mealDescription, //
			@Param("categoryId") int categoryId, //
			@Param("workstationId") int workstationId, //
			@Param("price") int price);

}
