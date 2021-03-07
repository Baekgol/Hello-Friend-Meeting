package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Integer>{
	public List<Comment> findAllByBno(int bno);
	public Optional<List<Comment>> findByCwriter(int cwriter, Sort sort);
}
