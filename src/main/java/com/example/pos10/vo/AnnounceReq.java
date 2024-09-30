package com.example.pos10.vo;

import java.time.LocalDate;

import com.example.pos10.entity.Announce;

public class AnnounceReq extends Announce{

	public AnnounceReq() {
		super();
	}

	public AnnounceReq(int announceId, String announceTitle, String announceContent,
			String announcePictureName, LocalDate announceStartTime, LocalDate announceEndTime) {
		super(announceId, announceTitle, announceContent, announcePictureName, announceStartTime,
				announceEndTime);
	}
	
	
}
