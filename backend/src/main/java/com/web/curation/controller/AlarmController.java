package com.web.curation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.BasicResponse;
import com.web.curation.model.entity.Alarm;
import com.web.curation.model.repository.AlarmRepository;
import com.web.curation.model.repository.UserInfoRepository;

@RestController
public class AlarmController {
	@Autowired
	AlarmRepository alarmRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@PostMapping("/getAlarms")
	public Object getAlarms(@RequestParam(required=true) final String email){
		Map<String,Object> resultMap=new HashMap<>();
		
		if(userInfoRepository.findByEmail(email)==null) 
			return null;
	
		int uid=userInfoRepository.findByEmail(email).getUno();
		
		
		Optional<List<Alarm>> list=alarmRepository.findByAuserAndAtype(uid,0,Sort.by("ano").descending());
		
		long notRead=alarmRepository.countByAuserAndAisReadAndAtype(uid, false,0);//되는지 확인 필요함.
		if(list.isPresent())
			resultMap.put("alarms",list.get());
		else
			resultMap.put("alarms",null);
		resultMap.put("notReadAlarm",notRead);
		
		return resultMap;
	}
	
	@PostMapping("/delAlarm")
	public Object delAlarm(@RequestParam(required=true) final int ano) {
		System.out.println(ano);
		
		final BasicResponse result = new BasicResponse();

		result.status=true;
		
		Alarm alarm=alarmRepository.findByAno(ano).get();
		alarmRepository.delete(alarm);
		result.data="해당 알림을 삭제했습니다.";
		
		ResponseEntity response = null;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}
	
	@PostMapping("readAlarm")
	public void readAlarm(@RequestParam(required=true) final int ano) {
		
		Alarm alarm=alarmRepository.findByAno(ano).get();
		if(alarm.isAisRead())
			return;
		alarm.setAisRead(true);
		alarmRepository.save(alarm);
	}

}
