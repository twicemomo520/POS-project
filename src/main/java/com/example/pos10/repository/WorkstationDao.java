package com.example.pos10.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Workstation;

@Repository
public interface WorkstationDao extends JpaRepository<Workstation, Integer> {

    @Query(value = "SELECT COUNT(*) FROM Workstation WHERE workstation_name = :name", nativeQuery = true)
    public int countByWorkstationName(@Param("name") String name);
    
    @Query(value = "SELECT COUNT(1) FROM workstation WHERE workstation_id = :id", nativeQuery = true)
    public int countByWorkstationId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Workstation (workstation_name) VALUES (:name)", nativeQuery = true)
    public void createWork(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Workstation SET workstation_name = :name WHERE workstation_id = :id", nativeQuery = true)
    public void updateWork(@Param("name") String name, @Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Workstation WHERE workstation_id = :id")
    public void deleteWork(@Param("id") Integer id);

    @Query("SELECT w FROM Workstation w WHERE (:id IS NULL OR w.id = :id)")
    public List<Workstation> searchWorkstation(@Param("id") Integer id);
}
