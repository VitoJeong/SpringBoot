package com.cos.instagram.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

//	private int id;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String location;
	private String caption;
	private String postImage;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	@JsonIgnoreProperties({"password", "images"})
	private User user;
	
	// Tag List
	// 빌더 패턴을 통해서는 필드에 아무리 기본값을 명시해뒀더라도 기본값이 null아니면 0이 나오므로
	// @Builder.Default 기본값으로 생성
	@Builder.Default
	@OneToMany(mappedBy = "image")
	@JsonManagedReference
	private List<Tag> tags = new ArrayList<>();
	
	// Like List
	@Builder.Default
	@OneToMany(mappedBy = "image")
	private List<Likes> likes = new ArrayList<>();
	
	@Transient // DB에 영향을 끼치지 않음
	private int likeCount;
	
	@Transient
	private boolean heart;
	
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;
}
