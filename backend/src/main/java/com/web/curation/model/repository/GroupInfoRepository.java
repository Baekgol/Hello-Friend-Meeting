package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.GroupInfo;

public interface GroupInfoRepository extends JpaRepository<GroupInfo,Integer>{
	public List<GroupInfo> findAllByGnoIn(List<Integer> gnos);
	public Optional<GroupInfo> findByGnoAndGmaster(int gno,int gmaster);
}
