package com.cos.instagram.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cos.instagram.model.Image;
import com.cos.instagram.model.Tag;
import com.cos.instagram.model.User;
import com.cos.instagram.repository.ImageRepository;
import com.cos.instagram.repository.TagRepository;
import com.cos.instagram.service.MyUserDetail;
import com.cos.instagram.util.Utils;

import ch.qos.logback.classic.pattern.Util;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ImageController {

	@Value("${file.path}")
	private String fileRealPath;
	
	private ImageRepository mImageRepository;
	
	private TagRepository mTagRepository;
	
	@Autowired
	public ImageController(ImageRepository mImageRepository, TagRepository mTagRepository) {
		this.mImageRepository = mImageRepository;
		this.mTagRepository = mTagRepository;
	}

	
	@GetMapping({"/","/image/feed"})
	public String imageFeed(@AuthenticationPrincipal MyUserDetail userDetail) {
		
		log.info("userName : " + userDetail.getUsername());
		return "image/feed";
	}
	
	@GetMapping("/image/upload")
	public String imageUpload() {
		return "image/image_upload";
	}
	
	@PostMapping("/image/uploadProc")
	public String imageUploadProc
	(
		@AuthenticationPrincipal MyUserDetail userDetail,
		@RequestParam("file") MultipartFile file,
		@RequestParam("caption") String caption,
		@RequestParam("location") String location,
		@RequestParam("tags") String tags
	) 
	{
		// 이미지 업로드
		UUID uuid = UUID.randomUUID();
		String uuidFileName = uuid + "_" + file.getOriginalFilename();

		
		Path filePath = Paths.get(fileRealPath + uuidFileName);
		
		try {
			Files.write(filePath, file.getBytes()); // 하드디스크 기록
			log.info("File Save : " + uuidFileName);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		User principal = userDetail.getUser();
		
		Image image = Image.builder()
				.caption(caption)
				.location(location)
				.user(principal)
				.postImage(uuidFileName)
				.build();
		
		mImageRepository.save(image);
		
		// Tag 객체 생성 및 데이터 삽입
		List<String> tagList = Utils.tagParser(tags);
		
		Tag t = null;
		for(String tag : tagList) {
			t = new Tag();
			t.setName(tag);
			t.setImage(image);
			mTagRepository.save(t);
			image.getTags().add(t);
		}
		
		
		return "redirect:/";
	}
}
