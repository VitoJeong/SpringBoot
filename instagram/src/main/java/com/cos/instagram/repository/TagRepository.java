package com.cos.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.instagram.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
