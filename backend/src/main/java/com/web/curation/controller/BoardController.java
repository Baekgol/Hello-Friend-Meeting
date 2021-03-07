package com.web.curation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.entity.Alarm;
import com.web.curation.model.entity.Board;
import com.web.curation.model.entity.GroupInfo;
import com.web.curation.model.entity.UserInfo;
import com.web.curation.model.repository.AlarmRepository;
import com.web.curation.model.repository.BoardRepository;
import com.web.curation.model.repository.GroupInfoRepository;
import com.web.curation.model.repository.UserInfoRepository;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class BoardController {

	@Autowired
	BoardRepository boardRepository;

	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	AlarmRepository alarmRepository;
	
	@Autowired
	GroupInfoRepository groupInfoRepository;

	@PostMapping("/getBoardList")
	public Object getBoardList(@RequestParam int bgno) {
		Map<String, Object> resultMap = new HashMap<>();

		List<Board> notice = boardRepository.findAllByBgnoAndBisNotice(bgno, true, Sort.by("bno").descending());
		List<Board> notNotice = boardRepository.findAllByBgnoAndBisNotice(bgno, false, Sort.by("bno").descending());
		
		resultMap.put("notice", notice);
		if(notice.size()>0)
			resultMap.put("noticeWriter",userInfoRepository.findById(notice.get(0).getBwriter()));
		
		resultMap.put("notNotice", notNotice);
		List<String> notNoticeWriter=new ArrayList<>();
		for(Board board:notNotice) 
			notNoticeWriter.add(userInfoRepository.findById(board.getBwriter()).get().getUname());
		resultMap.put("notNoticeWriter",notNoticeWriter);
		
		return resultMap;
	}

	@PostMapping("/boardDetail")
	public Object boardDetail(@RequestParam int bno,@RequestParam String email) {
		Map<String,Object> resultMap=new HashMap<>();
		if(!boardRepository.findById(bno).isPresent()) {
			resultMap.put("isExist",false);
			return resultMap;
		}
		resultMap.put("isExist",true);
		Board board = boardRepository.findById(bno).get();
		resultMap.put("curBoard",board);
		if(userInfoRepository.findById(board.getBwriter()).isPresent()) {
			resultMap.put("writer", userInfoRepository.findById(board.getBwriter()));
			if(email.equals(userInfoRepository.findById(board.getBwriter()).get().getEmail()))
				resultMap.put("isWriter",true);
			else
				resultMap.put("isWriter",false);
		}
		else {
			UserInfo ui=new UserInfo();
			ui.setUname("(알수없음)");
			resultMap.put("writer",ui);
			resultMap.put("isWriter",false);
		}
		return resultMap;
	}
	
	@PostMapping("/writeBoard")
	public Object writeBoard(@RequestParam String email, @RequestParam int bgno, @RequestParam String title,
			@RequestParam String content, @RequestParam boolean bisNotice) {
		Map<String, Object> resultMap = new HashMap<>();

		UserInfo myInfo=userInfoRepository.findByEmail(email);
		
		Board board = new Board();
		board.setBwriter(myInfo.getUno());
		board.setBgno(bgno);
		board.setBtitle(title);
		board.setBcontent(content);
		board.setBisNotice(bisNotice);

		board=boardRepository.save(board);
		
		GroupInfo groupInfo=groupInfoRepository.findById(bgno).get();
		String guserList=groupInfo.getGuserList();
		String[] eachUser=guserList.split(" ");
		
		StringBuilder sb=new StringBuilder();
		sb.append(myInfo.getUname());
		sb.append("님이 [");
		sb.append(groupInfo.getGname());
		sb.append("]에 '");
		
		sb.append(board.getBtitle());
		sb.append("'라는 글을 썼어요.");
		
		String asummary=sb.toString();
		
		for(String uno:eachUser) {
			if(Integer.parseInt(uno)==myInfo.getUno())
				continue;
			int curUno=Integer.parseInt(uno);
			Alarm alarm=new Alarm();
			alarm.setAtype(1);
			alarm.setCreateUser(myInfo.getUno());
			alarm.setAurlNo(board.getBno());
			alarm.setAurl("BoardDetail");
			alarm.setAuser(curUno);
			alarm.setAsummary(asummary);
			
			alarmRepository.save(alarm);
		}

		resultMap.put("data", "글 작성에 성공했습니다.");
		return resultMap;
	}

	@PostMapping("/modifyBoard")
	public Object modifyBoard(@RequestParam int bno, @RequestParam String title, @RequestParam String content) {
		Map<String, Object> resultMap = new HashMap<>();

		Board board = boardRepository.findById(bno).get();
		board.setBtitle(title);
		board.setBcontent(content);

		boardRepository.save(board);

		resultMap.put("data", "글 수정에 성공했습니다.");
		return resultMap;
	}
	
	@PostMapping("/delBoard")
	public Object delBoard(@RequestParam int bno) {
		Map<String,Object> resultMap=new HashMap<>();
		Board board = boardRepository.findById(bno).get();
		boardRepository.delete(board);
		resultMap.put("data","글을 삭제했습니다.");
		
		return resultMap;
	}

}
