package com.example.pos10.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pos10.entity.Authorization;

public interface AuthorizationDao extends JpaRepository<Authorization, Integer> {

	// 確認有沒有重複
	@Query(value = "SELECT COUNT(*) FROM authorization WHERE authorization_name = :authorizationName", nativeQuery = true)
	public int countByAuthorizationName(@Param("authorizationName") String authorizationName);

	// 新增
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO authorization (authorization_name, authorization_item) "
			+ "VALUES (:authorizationName, :authorizationItem)", nativeQuery = true)
	public int insertAuthorization(@Param("authorizationName") String authorizationName,
			@Param("authorizationItem") String authorizationItem);

	@Query(value = "SELECT * FROM authorization", nativeQuery = true)
	public List<Authorization> findAll();

	@Modifying
	@Transactional
	@Query(value = "UPDATE authorization SET authorization_name = :authorizationName, "
			+ "authorization_item = :authorizationItem WHERE authorization_id = :id", nativeQuery = true)
	public int updateAuthorization(@Param("id") int id, @Param("authorizationName") String authorizationName,
			@Param("authorizationItem") String authorizationItem);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM authorization WHERE authorization_id = :id", nativeQuery = true)
	public int deleteAuthorization(@Param("id") int id);

	@Query("SELECT COUNT(*) FROM Authorization WHERE authorizationName = :authorizationName AND id != :id")
	int countByAuthorizationNameAndIdNot(@Param("authorizationName") String authorizationName, @Param("id") int id);


}
