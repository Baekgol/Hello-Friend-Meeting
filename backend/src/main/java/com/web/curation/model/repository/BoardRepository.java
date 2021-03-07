package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.Board;

public interface BoardRepository extends JpaRepository<Board,Integer>{
	public List<Board> findAllByBgnoAndBisNotice(int bgno,boolean bisNotice,Sort sort);
	public List<Board> findByBgnoAndBisNotice(int bgno,boolean bisNotice,Sort sort);
	public Optional<List<Board>> findByBwriter(int bwriter, Sort sort);
}
