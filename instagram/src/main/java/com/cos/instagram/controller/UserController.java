package com.cos.instagram.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cos.instagram.model.Image;
import com.cos.instagram.model.User;
import com.cos.instagram.repository.FollowRepository;
import com.cos.instagram.repository.LikesRepository;
import com.cos.instagram.repository.UserRepository;
import com.cos.instagram.service.MyUserDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {
	
	@Value("${custom.path.upload-images}")
	private String fileRealPath;
	
	private BCryptPasswordEncoder encoder;
	
	private UserRepository mUserRepository;
	
	private FollowRepository mFollowRepository;
	
	private LikesRepository mLikesRepository;
	
	@Autowired
	public UserController
	(
			BCryptPasswordEncoder encoder, 
			UserRepository mUserRepository,
			FollowRepository mFollowRepository,
			LikesRepository mLikesRepository
	) {
		this.encoder = encoder;
		this.mUserRepository = mUserRepository;
		this.mFollowRepository = mFollowRepository;
		this.mLikesRepository = mLikesRepository;
	}

	@GetMapping("/auth/login")
	public String authLogin() {
		return "auth/login";
	}
	
	@GetMapping("/auth/join")
	public String authJoin() {
		return "auth/join";
	}
	
	@PostMapping("/auth/joinProc")
	public String authJoinProc(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		// com.cos.instagram.controller : debug
		log.debug("rawPassword : " + rawPassword);
		log.debug("encPassword : " + encPassword);
		
		mUserRepository.save(user);
		
		return "redirect:/auth/login";
	}
	
	@GetMapping("/user/{id}")
	public String profile
	(
			@AuthenticationPrincipal MyUserDetail userDetail,
			@PathVariable Long id,
			Model model
	) 
	{
		
		/**
		 *  1. imageCount
		 *  2. followerCount
		 *  3. followingCoung
		 *  4. User Object (Image(likeCount) 컬렉션)
		 *  5. followCheck(1:follow, 0:unFollow)
		 */		
		
		
		User user = mUserRepository.findById(id).get();
		
		// 1. imageCount
		int imageCount = user.getImages().size();
		model.addAttribute("imageCount", imageCount);
		
		// 2번 followCount 
		// (select count(*) from follow where fromUserId = 1)
		int followCount = 
				mFollowRepository.countByFromUserId(user.getId());
		model.addAttribute("followCount", followCount);
		
		// 3번 followerCount 
		// (select count(*) from follower where toUserId = 1)
		int followerCount = 
				mFollowRepository.countByToUserId(user.getId());
		model.addAttribute("followerCount", followerCount);
		
		// 4번 likeCount
		for(Image item: user.getImages()) {
			int likeCount = 
					mLikesRepository.countByImageId(item.getId());
			item.setLikeCount(likeCount);
		}


		model.addAttribute("user", user);
		
		// 5. followCheck
		User principal = userDetail.getUser();
		
		int followCheck = mFollowRepository.countByFromUserIdAndToUserId(principal.getId(), id);
		
		log.info("followCheck : " + (followCheck == 1 ? "팔로우" : "언팔로우"));
		model.addAttribute("followCheck",followCheck);
		
		return "user/profile";
	}
	
	@PostMapping("/user/profileUpload")
	public String userProfileUpload
	(
			@RequestParam("profileImage") MultipartFile file,
			@AuthenticationPrincipal MyUserDetail userDetail
	) throws IOException
	{
		User principal = userDetail.getUser();
		
		// 파일 처리
		UUID uuid = UUID.randomUUID();
		String uuidFilename = uuid+"_"+file.getOriginalFilename();
		Path filePath = Paths.get(fileRealPath+uuidFilename);
		Files.write(filePath, file.getBytes());

		// 영속화
		Optional<User> oUser = mUserRepository.findById(principal.getId());		
		User user = oUser.get();
		
		// 값 변경
		user.setProfileImage(uuidFilename);
		
		// 다시 영속화 및 저장
		mUserRepository.save(user);
		return "redirect:/user/"+principal.getId();
	}
		
	@GetMapping("/user/edit")
	public String userEdit(
			@AuthenticationPrincipal MyUserDetail userDetail,
			Model model) {
		
		Optional<User> oUser = mUserRepository.findById(userDetail.getUser().getId());
		User user = oUser.get();
		model.addAttribute("user", user);
		return "user/profile_edit";
	}
	

	@GetMapping("/user/edit/{id}")
	public String userEdit(@PathVariable Long id) {
		
		// 해당 ID로 select 해서 수정
		// findByUserInfo() 사용
		mUserRepository.findById(id);
		
		return "user/profile_edit";
	}
		
	@PutMapping("/user/editProc")
	public String userEditProc(
			User requestUser,
			@AuthenticationPrincipal MyUserDetail userDetail) {
		
		// 영속화
		Optional<User> oUser = mUserRepository.findById(userDetail.getUser().getId());
		User user = oUser.get();
		
		// 값 변경
		user.setName(requestUser.getName());
		user.setUserName(requestUser.getUserName());
		user.setWebsite(requestUser.getWebsite());
		user.setBio(requestUser.getBio());
		user.setEmail(requestUser.getEmail());
		user.setPhone(requestUser.getPhone());
		user.setGender(requestUser.getGender());
		
		// 다시 영속화 및 flush
		mUserRepository.save(user);
		
		return "redirect:/user/"+userDetail.getUser().getId();
	}
	
}
