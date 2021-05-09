package com.cos.instagram.repository;

import java.util.List;

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
	
	// following list
	List<Follow> findByFromUserId(Long fromUserId);
	
	// follower list
	List<Follow> findByToUserId(Long toUserId);
	
	// following count
	int countByFromUserId(Long fromUserId);

	// follower count
	int countByToUserId(Long toUserId);
}
