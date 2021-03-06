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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cos.instagram.model.Image;
import com.cos.instagram.model.Likes;
import com.cos.instagram.model.Tag;
import com.cos.instagram.model.User;
import com.cos.instagram.repository.ImageRepository;
import com.cos.instagram.repository.LikesRepository;
import com.cos.instagram.repository.TagRepository;
import com.cos.instagram.service.MyUserDetail;
import com.cos.instagram.util.Utils;

import ch.qos.logback.classic.pattern.Util;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ImageController {

	@Value("${custom.path.upload-images}")
	private String fileRealPath;
	
	private ImageRepository mImageRepository;
	
	private TagRepository mTagRepository;
	
	private LikesRepository mLikesRepository;
	
	@Autowired
	public ImageController(ImageRepository mImageRepository, TagRepository mTagRepository, LikesRepository mLikesRepository) {
		this.mImageRepository = mImageRepository;
		this.mTagRepository = mTagRepository;
		this.mLikesRepository = mLikesRepository;
	}
	
	@GetMapping("/image/explore")
	public String imageExplore(Model model,
			@PageableDefault(size = 9, sort = "id", direction = Direction.DESC) Pageable pageable) {

		// ???????????? ( ??? ???????????? ???????????? ?????? ?????? ????????? ????????? ??? ??????)
		Page<Image> pImages = mImageRepository.findAll(pageable);
		List<Image> images = pImages.getContent();

		// 4??? likeCount
		for (Image item : images) {
			int likeCount = mLikesRepository.countByImageId(item.getId());
			item.setLikeCount(likeCount);
		}

		model.addAttribute("images", images);
		return "image/explore";
	}
	
	@PostMapping("/image/like/{id}")
	public @ResponseBody String imageLike
	(
		@PathVariable Long id,
		@AuthenticationPrincipal MyUserDetail userDetail
	)
	{
		
		User currentUser = userDetail.getUser();
		Image image = mImageRepository.findById(id).get();
		
		Likes oldLike = 
				mLikesRepository.findByUserIdAndImageId(currentUser.getId(), id);
		
		try {
			if(oldLike == null) // ????????? ???????????? (??????)
			{
				Likes newLike = Likes.builder()
						.user(currentUser)
						.image(image)
						.build();
				
				mLikesRepository.save(newLike);
			}
			else if(oldLike != null) // ?????? ????????? ???????????? (??????)
			{
				mLikesRepository.delete(oldLike);
			}
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "fail";
	}
	

	
	@GetMapping({"/","/image/feed"})
	public String imageFeed
	(
			@AuthenticationPrincipal MyUserDetail userDetail,
			@PageableDefault(size=3, sort="id", direction = Direction.DESC) Pageable pageable,
			Model model
	) 
	{
		
		log.info("userName : " + userDetail.getUsername());
		
		// 1??? ?????? ???????????? ??????????????????
		Page<Image> pageImages = 
				mImageRepository.findImage(userDetail.getUser().getId(), pageable);
		
		List<Image> images = pageImages.getContent();
		
		for(Image image : images) {
			Likes like = mLikesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), image.getId());
			if(like != null) {
				image.setHeart(true);
			}
					
		}
		
		model.addAttribute("images",images);
		
		return "image/feed";
	}
	
	// http://localhost:8080/image/feed/scroll?page=1;
	@GetMapping("/image/feed/scroll")
	public @ResponseBody List<Image> imageFeedScroll(@AuthenticationPrincipal MyUserDetail userDetail,
			@PageableDefault(size = 3, sort = "id", direction = Direction.DESC) Pageable pageable) {

		// ?????? ???????????? ???????????? ??????
		Page<Image> pageImages = mImageRepository.findImage(userDetail.getUser().getId(), pageable);
		
		List<Image> images = pageImages.getContent();
		
		for(Image image : images) {
			Likes like = mLikesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), image.getId());
			if(like != null) {
				image.setHeart(true);
			}
					
		}
		
		return images;
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
		// ????????? ?????????
		UUID uuid = UUID.randomUUID();
		String uuidFileName = uuid + "_" + file.getOriginalFilename();

		
		Path filePath = Paths.get(fileRealPath + uuidFileName);
		
		try {
			Files.write(filePath, file.getBytes()); // ??????????????? ??????
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
		
		// Tag ?????? ?????? ??? ????????? ??????
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
