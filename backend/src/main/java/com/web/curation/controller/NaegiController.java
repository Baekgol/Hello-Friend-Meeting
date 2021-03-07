package com.web.curation.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.entity.Naegi;
import com.web.curation.model.entity.NaegiParticipant;
import com.web.curation.model.entity.ScheduleParticipant;
import com.web.curation.model.entity.UserInfo;
import com.web.curation.model.repository.GroupInfoRepository;
import com.web.curation.model.repository.NaegiParticipantRepository;
import com.web.curation.model.repository.NaegiRepository;
import com.web.curation.model.repository.UserInfoRepository;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class NaegiController {
	
	@Autowired
	NaegiRepository naegiRepository;
	
	@Autowired
	NaegiParticipantRepository naegiParticipantRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	GroupInfoRepository groupInfoRepository;
	
	@PostMapping("/getNotFinishedNaegis")
	public Object getNotFinishedNaegis(@RequestParam int gno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		String nenddate=format.format(new Date());
		long[] each= {31,28,31,30,31,30,31,31,30,31,30,31};
		Optional<List<Naegi>> naegiList=naegiRepository.findAllByNgnoAndNenddateGreaterThanEqualOrderByNenddateAsc(gno,nenddate);
		if(naegiList.isPresent()) {
			resultMap.put("isPresent",true);
			resultMap.put("naegiList",naegiList.get());
			List<String> remainTime=new ArrayList<>();
			for(Naegi n:naegiList.get()) {
				String datetime=n.getNenddate();
				long year=Long.parseLong(datetime.substring(0, 4));
				long month=Long.parseLong(datetime.substring(5,7));
				long day=Long.parseLong(datetime.substring(8,10));
				long hour=Long.parseLong(datetime.substring(11,13));
				long minute=Long.parseLong(datetime.substring(14,16));
				

				long curYear=Long.parseLong(nenddate.substring(0, 4));
				long curMonth=Long.parseLong(nenddate.substring(5,7));
				long curDay=Long.parseLong(nenddate.substring(8,10));
				long curHour=Long.parseLong(nenddate.substring(11,13));
				long curMinute=Long.parseLong(nenddate.substring(14,16));
				

				StringBuilder sb =new StringBuilder();
				
				long time=0;//분까지 표현
				long curTime=0;//분까지 표현

				time+=year*365*24*60;
				time+=month*each[(int)month-1]*24*60;
				time+=day*24*60;
				time+=hour*60;
				time+=minute;
				
				curTime+=curYear*365*24*60;
				curTime+=curMonth*each[(int)curMonth-1]*24*60;
				curTime+=curDay*24*60;
				curTime+=curHour*60;
				curTime+=curMinute;
				
				long sub=time-curTime;
				long subYear=365*24*60;
				long subMonth=31*24*60;
				long subDay=24*60;
				long subHour=60;
				
				if(sub/subYear>0){
					sb.append("종료까지 ");
					sb.append(sub/subYear);
					sb.append("년 남았습니다.");
				}
				else if(sub/subMonth>0) {
					sb.append("종료까지 ");
					sb.append(sub/subMonth);
					sb.append("달 남았습니다.");
				}
				else if(sub/subDay>0) {
					sb.append("종료까지 ");
					sb.append(sub/subDay);
					sb.append("일 남았습니다.");
				}
				else if(sub/subHour>0) {
					sb.append("종료까지 ");
					sb.append(sub/subHour);
					sb.append("시간 남았습니다.");
				}
				else if(sub>0) {
					sb.append("종료까지 ");
					sb.append(sub);
					sb.append("분 남았습니다.");
				}
				else 
					sb.append("곧 종료됩니다!");
				
				remainTime.add(sb.toString());
			}
			resultMap.put("remainTime",remainTime);
		}
		else
			resultMap.put("isPresent",false);
		
		
		return resultMap;
	}
	
	@PostMapping("/getFinishedNaegis")
	public Object getFinishedNaegis(@RequestParam int gno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		String nenddate=format.format(new Date());
		
		Optional<List<Naegi>> naegiList=naegiRepository.findAllByNgnoAndNenddateLessThanOrderByNenddateDesc(gno,nenddate);
		if(naegiList.isPresent()) {
			resultMap.put("isPresent",true);
			resultMap.put("naegiList",naegiList.get());
		}
		else
			resultMap.put("isPresent",false);
		
		
		return resultMap;
	}
	
	@PostMapping("/addNaegi")
	public Object addNaegi(@RequestParam String email, @RequestParam String ntitle, @RequestParam String ncontent,
			@RequestParam String nreward, @RequestParam String nenddate, @RequestParam int ngno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		Naegi naegi=new Naegi();
		naegi.setNcontent(ncontent);
		naegi.setNenddate(nenddate);
		naegi.setNgno(ngno);
		naegi.setNmaster(userInfoRepository.findByEmail(email).getUno());
		naegi.setNreward(nreward);
		naegi.setNtitle(ntitle);
		
		naegi=naegiRepository.save(naegi);

		String[] userList=groupInfoRepository.findById(ngno).get().getGuserList().split(" ");
		
		for(String s:userList) {
			NaegiParticipant np=new NaegiParticipant();
			np.setNno(naegi.getNno());
			np.setUno(Integer.parseInt(s));
			naegiParticipantRepository.save(np);
		}
		
		
		resultMap.put("result","새 내기를 추가했습니다.");
		
		return resultMap;
	}

	@PostMapping("/getNaegiParticipants")
	public Object getNaegiParticipants(@RequestParam int nno) {
		Map<String, Object> resultMap = new HashMap<>();

		Optional<List<NaegiParticipant>> list = naegiParticipantRepository.findAllByNno(nno);
		if (list.isPresent()) {
			resultMap.put("isPresent", true);
			List<String> emailList = new ArrayList<>();
			List<String> nameList = new ArrayList<>();
			resultMap.put("list", list.get());
			for (NaegiParticipant np : list.get()) {
				UserInfo ui = userInfoRepository.findById(np.getUno()).get();
				emailList.add(ui.getEmail());
				nameList.add(ui.getUname());
			}
			resultMap.put("emailList", emailList);
			resultMap.put("nameList", nameList);
			resultMap.put("masterEmail",userInfoRepository.findById(naegiRepository.findById(nno).get().getNmaster()).get().getEmail());
		} else
			resultMap.put("isPresent", false);

		return resultMap;
	}
	
	@PostMapping("/changeNaegiAttendance")
	public Object changeNaegiAttendance(@RequestParam int nno, @RequestParam String email, @RequestParam int attendance) {
		Map<String,Object> resultMap=new HashMap<>();
		
		UserInfo myInfo=userInfoRepository.findByEmail(email);
		
		Optional<NaegiParticipant> naegiParticipant=naegiParticipantRepository.findByNnoAndUno(nno, myInfo.getUno());
		
		if(naegiParticipant.isPresent()) {
			resultMap.put("isPresent",true);
			
			NaegiParticipant np=naegiParticipant.get();
			np.setNattendance(attendance);
			naegiParticipantRepository.save(np);
			resultMap.put("data","성공적으로 투표가 완료되었습니다.");
		}
		else
			resultMap.put("isPresent",false);
		
		return resultMap;
	}
	
	@PostMapping("/changeNaegiResult")
	public Object changeNaegiResult(@RequestParam int nno,@RequestParam int result) {
		Map<String,Object> resultMap=new HashMap<>();
		
		Optional<Naegi> naegi=naegiRepository.findById(nno);
		if(naegi.isPresent()) {
			resultMap.put("isPresent",true);
			Naegi n=naegi.get();
			n.setNresult(result);
			SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
			
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.SECOND, -2);
			String nenddate=format.format(cal.getTime());
			n.setNenddate(nenddate);
			naegiRepository.save(n);
			resultMap.put("data","결과를 확인해볼까요?");
		}
		else
			resultMap.put("isPresent",false);
		
		return resultMap;
	}
	
	@PostMapping("/delNaegi")
	public Object delNaegi(@RequestParam int nno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		Optional<Naegi> naegi=naegiRepository.findById(nno);
		if(naegi.isPresent()) {
			naegiRepository.delete(naegi.get());
			resultMap.put("isPresent",true);
			resultMap.put("data","내기를 삭제했습니다.");
		}
		else
			resultMap.put("isPresent",false);
		
		return resultMap;
	}

}
