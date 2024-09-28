package com.example.pos10.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos10.entity.ComboItems;

public interface ComboDao extends JpaRepository<ComboItems, String> {
	
	
	
}
