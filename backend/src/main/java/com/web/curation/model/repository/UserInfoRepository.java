package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer>{
	public UserInfo findByEmail(String email);
	public Optional<UserInfo> findByTel(String tel);
	public UserInfo findByEmailAndPassword(String email, String password);
	public void deleteByEmail(String email);
	public List<UserInfo> findAllByUnoIn(List<Integer> uno);
	public UserInfo findByUno(int uno);
}
