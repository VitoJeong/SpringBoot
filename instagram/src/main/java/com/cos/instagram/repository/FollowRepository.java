package com.cos.instagram.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.instagram.model.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long>{

	// unFollow
	@Transactional
	int deleteByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

	// follow 유무
	int countByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
