package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.FriendInfo;

public interface FriendInfoRepository  extends JpaRepository<FriendInfo, Integer>{
	public Optional<FriendInfo> findFriendInfoByMyIdAndFriendId(int myId, int friendId);
	public Optional<FriendInfo> findByMyIdAndFriendId(int myId,int friendId);
	public Optional<List<FriendInfo>> findAllByMyId(int myId);
}