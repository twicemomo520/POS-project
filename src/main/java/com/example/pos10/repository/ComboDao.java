package com.example.pos10.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.ComboItems;


@Repository
public interface ComboDao extends JpaRepository<ComboItems, String> {
	
	
    @Query(value = "select count(*) from combo_items as c where c.combo_name = :inputComboName", nativeQuery = true)
    public long countByComboName(
    		@Param("inputComboName") String comboName);
	
	
    @Modifying
    @Transactional
	@Query(value= "insert into combo_items (combo_name, combo_detail, discount_amount, category_id) "
			+ " values (:inputComboName, :inputComboDetail, :inputDiscountAmount, :inputCategoryId) ", nativeQuery=true)
	public void createCombo(
			@Param("inputComboName") String comboName,
			@Param("inputComboDetail") String comboDetail,
			@Param("inputDiscountAmount") int discountAmount,
			@Param("inputCategoryId") int categoryId
			);
    
    
    @Modifying
    @Transactional
    @Query(value= "update combo_items "
    		+ " set combo_name = :inputComboName, "
    		+ " combo_detail = :inputComboDetail, "
    		+ " discount_amount = :inputDiscountAmount "
    		+ " where combo_name = :inputOldComboName",  nativeQuery=true)
    public void updateCombo(
    		@Param("inputOldComboName") String oldComboName,
    		@Param("inputComboName") String comboName,
    		@Param("inputComboDetail") String comboDetail,
    		@Param("inputDiscountAmount") int discountAmount
    		);
    
    
    @Modifying
    @Transactional
    @Query(value= "delete from combo_items where combo_name = :inputComboName", nativeQuery=true)
    public void deleteCombo(
    		@Param("inputComboName") String comboName
    		);
    
    
    @Query(value= "select * from combo_items as c"
    		+ " where (:inputComboName is null or :inputComboName = '' or c.combo_name = :inputComboName)", nativeQuery=true)
    public List<ComboItems> searchCombo(
    		@Param("inputComboName") String comboName
    		);
    
	@Query( value = " SELECT * FROM combo_items " ,nativeQuery = true)
	public List<ComboItems> selectAll();
	
	@Query(value = "SELECT COUNT(*) AS combo_name "
			+ "FROM combo_items", nativeQuery = true)
	public int countData();
	
}
