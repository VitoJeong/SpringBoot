package com.cos.instagram.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.instagram.model.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long>{

	// unFollow
	@Transactional
	int deleteByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

	// follow 유무
	int findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
