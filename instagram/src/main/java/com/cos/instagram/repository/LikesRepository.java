package com.cos.instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.instagram.model.Image;
import com.cos.instagram.model.Likes;
import com.cos.instagram.model.User;

public interface LikesRepository extends JpaRepository<Likes, Long>{

	// 내가 좋아요 누른 사진 찾기
	Likes findByUserIdAndImageId(Long userId, Long imageId);
	
	int countByImageId(Long imageId);
	
	// 내 이미지를 좋아요 하는 알림 정보 (추가)
	@Query(value="SELECT * "
			+ "FROM likes "
			+ "WHERE imageId IN (SELECT id FROM image WHERE userId = ?1) "
			+ "ORDER BY id DESC LIMIT 5;", 
			nativeQuery = true)
	List<Likes> findLikeNotification(Long userId);


}
