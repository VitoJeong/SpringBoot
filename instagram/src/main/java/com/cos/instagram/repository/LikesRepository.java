package com.cos.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.instagram.model.Image;
import com.cos.instagram.model.Likes;
import com.cos.instagram.model.User;

public interface LikesRepository extends JpaRepository<Likes, Long>{

	// 내가 좋아요 누른 사진 찾기
	Likes findByUserIdAndImageId(Long userId, Long imageId);
	
	int countByImageId(Long imageId);
}
