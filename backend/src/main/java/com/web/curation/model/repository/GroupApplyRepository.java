package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.GroupApply;

public interface GroupApplyRepository extends JpaRepository<GroupApply, Integer> {
	public Optional<GroupApply> findByUnoAndGno(int uno,int gno);
	public Optional<List<GroupApply>> findAllByGno(int gno);
}
