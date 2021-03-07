package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer>{
	public Optional<List<Schedule>> findAllBySgno(int sgno);

}
