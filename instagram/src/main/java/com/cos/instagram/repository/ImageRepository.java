package com.cos.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.instagram.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
