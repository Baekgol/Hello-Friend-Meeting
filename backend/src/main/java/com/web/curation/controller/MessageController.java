package com.web.curation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.entity.Message;
import com.web.curation.model.entity.UserInfo;
import com.web.curation.model.repository.MessageRepository;
import com.web.curation.model.repository.UserInfoRepository;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class MessageController {
	@Autowired
	MessageRepository messageRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@PostMapping("/sendMessage")
	public Object sendMessage(@RequestBody Map<String, String> map) {
		Map<String, Object> resultMap = new HashMap<>();
		
		Message message = new Message();
		message.setMsender(Integer.parseInt(map.get("myUno")));
		message.setMreceiver(Integer.parseInt(map.get("friendUno")));
		message.setMtitle(map.get("title"));
		message.setMcontent(map.get("content"));
		messageRepository.save(message);
		resultMap.put("is-success", true);
		
		return resultMap;
	}
	
	@PostMapping("/delMessage")
	public Object delMessage(@RequestParam int mno) {
		Map<String, Object> resultMap = new HashMap<>();
		
		messageRepository.deleteById(mno);
		resultMap.put("data", true);
		return resultMap;
	}
	
	@PostMapping("/getMessages")
	public Object getMessage(@RequestParam String email){
		Map<String,Object> resultMap=new HashMap<>();
		int uno=userInfoRepository.findByEmail(email).getUno();
		List<Message> list=messageRepository.findAllByMreceiver(uno,Sort.by("mno").descending());
		resultMap.put("messagesList",list);
		List<String> nameList=new ArrayList<>();
		for(Message m:list) 
			nameList.add(userInfoRepository.findById(m.getMsender()).get().getUname());
		
		resultMap.put("namesList",nameList);
		return resultMap;
	}
	
}
