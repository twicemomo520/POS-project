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
	
    // �P�_��ƬO�_�s�b��SQL�y�k
	// �bmenu_items�̭����J�W�ٻP��Ʈw�{����meal_name�W�٬ۦP����ơA�Y���^��(1)�A�̫�Acount�^�ǴX��(1)
    @Query(value = "SELECT COUNT(1) FROM menu_items WHERE meal_name = :mealName", nativeQuery = true)
    public int existsByMealName(@Param("mealName") String mealName);
	
	// �s�W��ƪ�SQL�y�k
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
