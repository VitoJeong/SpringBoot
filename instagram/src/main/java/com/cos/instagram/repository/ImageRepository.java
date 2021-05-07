package com.cos.instagram.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.instagram.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query
	(value = "SELECT * FROM image "
			+ "WHERE userId IN (SELECT toUserId FROM follow WHERE fromUserId = ?1)", 
			nativeQuery = true
	)
	Page<Image> findImage(Long userId, Pageable pageable);
}
