package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline,Integer> {
	Optional<List<Timeline>> findAllByUno(int uno);
}
