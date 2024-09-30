package com.example.pos10.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Announce;

@Repository
public interface AnnounceDao extends JpaRepository<Announce, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Announce (announce_title, announce_content, announce_picture_name, announce_starttime, announce_endtime) "
            + "VALUES (:title, :content, :pictureName, :startTime, :endTime)", nativeQuery = true)
    public void createAnnounce(@Param("title") String title,
            @Param("content") String content,
            @Param("pictureName") String pictureName,
            @Param("startTime") LocalDate startTime,
            @Param("endTime") LocalDate endTime);

    @Modifying
    @Transactional
    @Query("UPDATE Announce a SET a.announceTitle = :title, a.announceContent = :content, "
            + "a.announcePictureName = :pictureName, "
            + "a.announceStartTime = :startTime, a.announceEndTime = :endTime " + "WHERE a.id = :id")
    void updateAnnounce(@Param("id") Integer id, @Param("title") String title,
            @Param("content") String content,
            @Param("pictureName") String pictureName,
            @Param("startTime") LocalDate startTime,
            @Param("endTime") LocalDate endTime);

    @Modifying
    @Transactional
    @Query("DELETE FROM Announce a WHERE a.id = :id")
    void deleteAnnounce(@Param("id") Integer id);

    @Query("SELECT a FROM Announce a WHERE (:id IS NULL OR a.id = :id)")
    List<Announce> searchAnnounce(@Param("id") Integer id);
}
