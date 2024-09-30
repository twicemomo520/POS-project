package com.example.pos10.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "announce")
public class Announce {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "announce_id", nullable = false)
	private int announceId;

	@NotBlank(message = "Announce title cannot be null or empty!!!")
	@Column(name = "announce_title")
	private String announceTitle;

	@NotBlank(message = "Announce content cannot be null or empty!!!")
	@Column(name = "announce_content")
	private String announceContent;

	@Column(name = "announce_picture_name")
	private String announcePictureName;

	@FutureOrPresent(message = "Announce start time must be in the present or future !!!")
	@NotNull(message = "Announce start time cannot be null !!!")
	@Column(name = "announce_starttime")
	private LocalDate announceStartTime;

	@FutureOrPresent(message = "Announce end time must be in the present or future !!!")
	@NotNull(message = "Announce end time cannot be null !!!")
	@Column(name = "announce_endtime")
	private LocalDate announceEndTime;

	public Announce() {
		super();
	}

	public Announce(int announceId, String announceTitle, String announceContent, String announcePictureName, //
			LocalDate announceStartTime, LocalDate announceEndTime) {
		super();
		this.announceId = announceId;
		this.announceTitle = announceTitle;
		this.announceContent = announceContent;
		this.announcePictureName = announcePictureName;
		this.announceStartTime = announceStartTime;
		this.announceEndTime = announceEndTime;
	}

	public Announce(String announceTitle, String announceContent, String announcePictureName,
			LocalDate announceStartTime, LocalDate announceEndTime) {
		this.announceTitle = announceTitle;
		this.announceContent = announceContent;
		this.announcePictureName = announcePictureName;
		this.announceStartTime = announceStartTime;
		this.announceEndTime = announceEndTime;
	}

	public int getAnnounceId() {
		return announceId;
	}

	public void setAnnounceId(int announceId) {
		this.announceId = announceId;
	}

	public String getAnnounceTitle() {
		return announceTitle;
	}

	public void setAnnounceTitle(String announceTitle) {
		this.announceTitle = announceTitle;
	}

	public String getAnnounceContent() {
		return announceContent;
	}

	public void setAnnounceContent(String announceContent) {
		this.announceContent = announceContent;
	}

	public String getAnnouncePictureName() {
		return announcePictureName;
	}

	public void setAnnouncePictureName(String announcePictureName) {
		this.announcePictureName = announcePictureName;
	}

	public LocalDate getAnnounceStartTime() {
		return announceStartTime;
	}

	public void setAnnounceStartTime(LocalDate announceStartTime) {
		this.announceStartTime = announceStartTime;
	}

	public LocalDate getAnnounceEndTime() {
		return announceEndTime;
	}

	public void setAnnounceEndTime(LocalDate announceEndTime) {
		this.announceEndTime = announceEndTime;
	}

}