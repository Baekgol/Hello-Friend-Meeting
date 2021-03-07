package com.web.curation.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.GroupChatting;

public interface GroupChattingRepository extends JpaRepository<GroupChatting, Integer>{
	public List<GroupChatting> findAllByGcgno(int gcgno);
	public GroupChatting findByGcno(int gcno);
}
