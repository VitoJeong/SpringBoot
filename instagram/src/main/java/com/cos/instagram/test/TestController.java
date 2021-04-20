package com.cos.instagram.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.instagram.model.Follow;
import com.cos.instagram.model.Image;
import com.cos.instagram.model.Likes;
import com.cos.instagram.model.User;


@Controller
public class TestController {

	@GetMapping("/test/home")
	public String testHome() {
		return "home";
	}
	
	@GetMapping("/test/user")
	public @ResponseBody User getUser() {
		User user = new User();
		user.setId(1l);
		user.setUserName("cos");
		user.setName("홍길동");
		user.setEmail("cos@nate.com");
		user.setProfileImage("my.jpg");
		
		Image img1 = new Image();
		img1.setId(1l);
		img1.setCaption("음식 사진");
		img1.setLocation("food.jpg");
		img1.setLocation("부산 서면");
		img1.setUser(user);
		
		Image img2 = new Image();
		img2.setId(2l);
		img2.setCaption("장난감 사진");
		img2.setLocation("game.jpg");
		img2.setLocation("서울 강남");
		img2.setUser(user);
		
		List<Image> images = new ArrayList<>();
		images.add(img1);
		images.add(img2);
		user.setImages(images);
		
		return user;
	}
	
	@GetMapping("/test/image")
	public @ResponseBody Image getImage() {
		User user = new User();
		user.setId(1l);
		user.setUserName("cos");
		user.setName("홍길동");
		user.setEmail("cos@nate.com");
		user.setProfileImage("my.jpg");
		
		Image img1 = new Image();
		img1.setId(1l);
		img1.setCaption("음식 사진");
		img1.setLocation("food.jpg");
		img1.setLocation("부산 서면");
		img1.setUser(user);
		
		return img1;
	}
	
	@GetMapping("/test/images")
	public @ResponseBody List<Image> getImages(){
		User user = new User();
		user.setId(1l);
		user.setUserName("cos");
		user.setName("홍길동");
		user.setEmail("cos@nate.com");
		user.setProfileImage("my.jpg");
		
		Image img1 = new Image();
		img1.setId(1l);
		img1.setCaption("음식 사진");
		img1.setLocation("food.jpg");
		img1.setLocation("부산 서면");
		img1.setUser(user);
		
		Image img2 = new Image();
		img2.setId(2l);
		img2.setCaption("장난감 사진");
		img2.setLocation("game.jpg");
		img2.setLocation("서울 강남");
		img2.setUser(user);
		
		List<Image> images = new ArrayList<>();
		images.add(img1);
		images.add(img2);
		user.setImages(images);
		
		return images;
	}
	
	@GetMapping("/test/like")
	public @ResponseBody Likes getLike() {
		User user = new User();
		user.setId(1l);
		user.setUserName("cos");
		user.setName("홍길동");
		user.setEmail("cos@nate.com");
		user.setProfileImage("my.jpg");
		
		Image img1 = new Image();
		img1.setId(1l);
		img1.setCaption("음식 사진");
		img1.setLocation("food.jpg");
		img1.setLocation("부산 서면");
		img1.setUser(user);
		
		Likes like = new Likes();
		like.setId(1l);
		like.setUser(user);
		like.setImage(img1);
		
		return like;
	}
	
	@GetMapping("/test/follow")
	public @ResponseBody List<Follow> getFollows() {
		User user1 = new User();
		user1.setId(1l);
		user1.setUserName("cos");
		user1.setName("홍길동");
		user1.setEmail("cos@nate.com");
		user1.setProfileImage("my.jpg");
		
		User user2 = new User();
		user2.setId(2l);
		user2.setUserName("ssar");
		user2.setName("장동건");
		user2.setEmail("ssar@nate.com");
		user2.setProfileImage("you.jpg");
		
		User user3 = new User();
		user3.setId(3l);
		user3.setUserName("love");
		user3.setName("유해진");
		user3.setEmail("love@naver.com");
		user3.setProfileImage("love.jpg");
		
		Follow follow1 = new Follow();
		follow1.setId(1l);
		follow1.setFromUser(user1);
		follow1.setToUser(user2);
		
		Follow follow2 = new Follow();
		follow2.setId(2l);
		follow2.setFromUser(user1);
		follow2.setToUser(user3);
		
		Follow follow3 = new Follow();
		follow3.setId(3l);
		follow3.setFromUser(user2);
		follow3.setToUser(user1);
		
		List<Follow> follows = new ArrayList<Follow>();
		follows.add(follow1);
		follows.add(follow2);
		follows.add(follow3);
		
		return follows;
	}
	
	@GetMapping("/test/login")
	public String testLogin() {
		return "/auth/login";
	}
	
	@GetMapping("/test/join")
	public String testJoin() {
		return "/auth/join";
	}
}
