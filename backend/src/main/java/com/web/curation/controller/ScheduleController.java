package com.web.curation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.entity.Alarm;
import com.web.curation.model.entity.GroupInfo;
import com.web.curation.model.entity.Schedule;
import com.web.curation.model.entity.ScheduleParticipant;
import com.web.curation.model.entity.UserInfo;
import com.web.curation.model.repository.AlarmRepository;
import com.web.curation.model.repository.GroupInfoRepository;
import com.web.curation.model.repository.ScheduleParticipantRepository;
import com.web.curation.model.repository.ScheduleRepository;
import com.web.curation.model.repository.UserInfoRepository;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class ScheduleController {

	@Autowired
	ScheduleRepository scheduleRepository;
	
	@Autowired
	ScheduleParticipantRepository scheduleParticipantRepository;
	
	@Autowired
	GroupInfoRepository groupInfoRepository;
	
	@Autowired
	AlarmRepository alarmRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@PostMapping("/addSchedule")
	public Object addSchedule(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String email,
			@RequestParam int gno, @RequestParam String title, @RequestParam String content ) {
		Map<String,Object> resultMap=new HashMap<>();
		
		Schedule schedule=new Schedule();
		schedule.setStitle(title);
		schedule.setScontent(content);
		schedule.setSstartdate(startDate);
		schedule.setSenddate(endDate);
		schedule.setSgno(gno);
		schedule.setSmaster(userInfoRepository.findByEmail(email).getUno());
		
		System.out.println(schedule);
		
		schedule=scheduleRepository.save(schedule);
		GroupInfo groupInfo=groupInfoRepository.findById(schedule.getSgno()).get();
		
		String members=groupInfo.getGuserList();
		String[] memberList=members.split(" ");
		
		for(String s:memberList) {
			ScheduleParticipant scheduleParticipant=new ScheduleParticipant();
			scheduleParticipant.setSno(schedule.getSno());
			scheduleParticipant.setUno(Integer.parseInt(s));
			
			scheduleParticipantRepository.save(scheduleParticipant);
			
			if(Integer.parseInt(s)==schedule.getSmaster())
				continue;
			Alarm alarm=new Alarm();
			alarm.setAtype(1);
			alarm.setAurl("GroupMainPage");
			alarm.setAurlNo(schedule.getSgno());
			alarm.setAuser(Integer.parseInt(s));
			alarm.setCreateUser(schedule.getSmaster());
			
			StringBuilder sb=new StringBuilder();
			sb.append("새 일정 '");
			sb.append(schedule.getStitle());
			sb.append("'이 [");
			sb.append(groupInfo.getGname());
			sb.append("]에 등록됐어요.");
			
			alarm.setAsummary(sb.toString());
			
			alarmRepository.save(alarm);
		}
		schedule.setSstartdate(schedule.getSstartdate().substring(0, schedule.getSstartdate().length()-3));
		schedule.setSenddate(schedule.getSenddate().substring(0, schedule.getSenddate().length()-3));
		
		resultMap.put("createdSchedule",schedule);
		resultMap.put("userEmail",email);
		
		return resultMap;
	}
	
	@PostMapping("/getSchedulesList")
	public Object getSchedulesList(@RequestParam int gno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		Optional<List<Schedule>> list=scheduleRepository.findAllBySgno(gno);
		if(list.isPresent()) {
			resultMap.put("isPresent",true);
			List<String> emailList=new ArrayList<>();
			for(Schedule s:list.get()) {
				s.setSstartdate(s.getSstartdate().substring(0, s.getSstartdate().length()-3));
				s.setSenddate(s.getSenddate().substring(0, s.getSenddate().length()-3));
				emailList.add(userInfoRepository.findById(s.getSmaster()).get().getEmail());
			}
			resultMap.put("list",list.get());
			resultMap.put("emailList",emailList);
		}
		else 
			resultMap.put("isPresent",false);
		
		return resultMap;
	}
	
	@PostMapping("/delSchedule")
	public Object delSchedule(@RequestParam int sno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		Optional<Schedule> schedule=scheduleRepository.findById(sno);
		if(schedule.isPresent()) {
			resultMap.put("isPresent",true);
			scheduleRepository.delete(schedule.get());
		}
		else
			resultMap.put("isPresent",false);
		
		return resultMap;
	}
	
	@PostMapping("/getScheduleParticipants")
	public Object getScheduleParticipants(@RequestParam int sno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		Optional<List<ScheduleParticipant>> list=scheduleParticipantRepository.findAllBySno(sno);
		if(list.isPresent()) {
			resultMap.put("isPresent",true);
			List<String> emailList=new ArrayList<>();
			List<String> nameList=new ArrayList<>();
			resultMap.put("list",list.get());
			for(ScheduleParticipant sp:list.get()) {
				UserInfo ui=userInfoRepository.findById(sp.getUno()).get();
				emailList.add(ui.getEmail());
				nameList.add(ui.getUname());
			}
			resultMap.put("emailList",emailList);
			resultMap.put("nameList",nameList);
		}
		else 
			resultMap.put("isPresent",false);
		
		return resultMap;
	}
	
	@PostMapping("/changeScheduleAttendance")
	public Object changeScheduleAttendance(@RequestParam int sno, @RequestParam String email, @RequestParam int attendance) {
		Map<String,Object> resultMap=new HashMap<>();
		
		UserInfo myInfo=userInfoRepository.findByEmail(email);
		
		Optional<ScheduleParticipant> scheduleParticipant=scheduleParticipantRepository.findBySnoAndUno(sno, myInfo.getUno());
		
		if(scheduleParticipant.isPresent()) {
			resultMap.put("isPresent",true);
			
			ScheduleParticipant sp=scheduleParticipant.get();
			sp.setAttendance(attendance);
			scheduleParticipantRepository.save(sp);
			resultMap.put("data","성공적으로 투표가 완료되었습니다.");
		}
		else
			resultMap.put("isPresent",false);
		
		return resultMap;
	}
}
