package com.web.curation.model.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.Message;

public interface MessageRepository extends JpaRepository<Message,Integer> {
	public List<Message> findAllByMreceiver(int uno,Sort sort);
}
