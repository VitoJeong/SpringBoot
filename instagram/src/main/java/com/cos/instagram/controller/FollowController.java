package com.cos.instagram.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.instagram.model.Follow;
import com.cos.instagram.model.User;
import com.cos.instagram.repository.FollowRepository;
import com.cos.instagram.repository.UserRepository;
import com.cos.instagram.service.MyUserDetail;

@Controller
public class FollowController {

	private UserRepository userRepository;
	
	private FollowRepository followRepository;

	public FollowController(UserRepository userRepository, FollowRepository followRepository) {
		this.userRepository = userRepository;
		this.followRepository = followRepository;
	}

	@PostMapping("/follow/{id}")
	public @ResponseBody String follow
	(
			@AuthenticationPrincipal MyUserDetail userDetail,
			@PathVariable Long id
	) 
	{
		
		User fromUser = userDetail.getUser();
		
		Optional<User> optionalToUser = userRepository.findById(id);
		User toUser = optionalToUser.get();
		
		Follow follow = new Follow();
		follow.setFromUser(fromUser);
		follow.setToUser(toUser);
		
		followRepository.save(follow);
		
		return "ok";
	}
	
	@DeleteMapping("/follow/{id}")
	public @ResponseBody String unFollow
	(
			@AuthenticationPrincipal MyUserDetail userDetail,
			@PathVariable Long id
			) 
	{
		
		User fromUser = userDetail.getUser();
		
		Optional<User> optionalToUser = userRepository.findById(id);
		User toUser = optionalToUser.get();
		
		followRepository.deleteByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());
		
		List<Follow> follows = followRepository.findAll();
		return "ok"; // ResponseEntity
	}
	
	@GetMapping("/follow/follower/{id}")
	public String followFollower(@PathVariable Long id, Model model) {
		
		// 팔로워 리스트
		List<Follow> follows = followRepository.findByToUserId(id);
		model.addAttribute("follower", follows);
		return "follow/follow";
	}
	
	@GetMapping("/follow/follow/{id}")
	public String followFollow
	(
			@PathVariable Long id, 
			@AuthenticationPrincipal MyUserDetail userDetail
	) 
	{
		
		// 팔로잉 리스트
		List<Follow> follows = followRepository.findByFromUserId(id);
		
		// 내 팔로잉 리스트
		List<Follow> principalFollows = followRepository.findByFromUserId(userDetail.getUser().getId());
		
		for(Follow f1 : follows) {
			for(Follow f2 : principalFollows) {
				if(f1.getToUser().equals(f2.getToUser())) {
					f1.setFollowState(true);
				}
			}
		}
		
		return "follow/follow";
	}
}
