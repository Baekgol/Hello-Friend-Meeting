package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm,Integer>{

	public Optional<List<Alarm>> findByAuserAndAtype(int auser,int atype,Sort sort);
	public Optional<Alarm> findByAno(int ano);
	public Long countByAuserAndAisReadAndAtype(int auser, boolean aisRead,int atype);
}
