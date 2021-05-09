package com.cos.instagram.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Likes {

//	private int id;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	@JsonIgnoreProperties({"images", "password", "name", "website", "bio", "email", "phone", "gender", "creatDate", "updateDate", })
	private User user; // id, username, profileImage
	
	@ManyToOne
	@JoinColumn(name = "imageId")
	@JsonIgnoreProperties({"user", "likes", "tags"})
	private Image image; // 기본 : 'image_id'
	
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;
	
}
