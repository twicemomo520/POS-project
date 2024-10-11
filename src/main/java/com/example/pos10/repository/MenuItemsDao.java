package com.example.pos10.repository;

import java.util.List;

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
	
    @Query(value = "select * from menu_items", nativeQuery = true)
    public List<MenuItems> selectAll();
    
	// 新增資料的SQL語法
	@Modifying
	@Transactional
	@Query(value = "insert into menu_items "
			+ " (meal_name, category_id, workstation_id, price, available, picture_name) values"
			+ " (:mealName, :categoryId, :workstationId, :price, :available, :pictureName)", //
			nativeQuery = true)
	public int insert(//
			@Param("mealName") String mealName, //
			@Param("categoryId") int categoryId, //
			@Param("workstationId") int workstationId, //
			@Param("price") int price, //
			@Param("available") boolean available, //
			@Param("pictureName") String pictureName);
	
	// 編輯資料
	@Modifying
	@Transactional
	@Query(value = "update menu_items "
			+ " set "
			+ " category_id = :categoryId, "
			+ " workstation_id = :workstationId, "
			+ " price = :price, "
			+ " available = :available, "
			+ " picture_name = :pictureName "
			+ " where "
			+ " meal_name = :mealName", nativeQuery = true)
	public int updateMenu(//
			@Param("mealName") String mealName, //
			@Param("categoryId") int categoryId, //
			@Param("workstationId") int workstationId, //
			@Param("price") int price, //
			@Param("available") boolean available, //
			@Param("pictureName") String pictureName);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE menu_items "
			+ " SET "
			+ " workstation_id = :workstationId"
			+ " WHERE "
			+ " category_id = :categoryId", nativeQuery = true)
	public int updateMenuWorkStation(@Param("workstationId") int workstationId, //
			@Param("categoryId") int categoryId);

	// 刪除資料
	@Modifying
	@Transactional
	@Query(value = "delete from menu_items where meal_name = :mealName", nativeQuery = true)
	public int deleteMenuByMealName(@Param("mealName") String mealName);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM menu_items "
			+ " WHERE category_id = :categoryId", nativeQuery = true)
	public int deleteMenuByCgId(@Param("categoryId") int categoryId);
	
	
	
	
	
	
	
	
	
	
	
	
	
}
