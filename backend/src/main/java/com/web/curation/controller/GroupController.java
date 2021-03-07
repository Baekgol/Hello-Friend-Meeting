package com.web.curation.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.entity.Alarm;
import com.web.curation.model.entity.Board;
import com.web.curation.model.entity.Category;
import com.web.curation.model.entity.Filetest;
import com.web.curation.model.entity.FriendInfo;
import com.web.curation.model.entity.GroupApply;
import com.web.curation.model.entity.GroupDto;
import com.web.curation.model.entity.GroupInfo;
import com.web.curation.model.entity.GroupParticipant;
import com.web.curation.model.entity.Naegi;
import com.web.curation.model.entity.NaegiParticipant;
import com.web.curation.model.entity.Schedule;
import com.web.curation.model.entity.ScheduleParticipant;
import com.web.curation.model.entity.Timeline;
import com.web.curation.model.entity.UserInfo;
import com.web.curation.model.repository.AlarmRepository;
import com.web.curation.model.repository.BoardRepository;
import com.web.curation.model.repository.CategoryRepository;
import com.web.curation.model.repository.FiletestRepository;
import com.web.curation.model.repository.FriendInfoRepository;
import com.web.curation.model.repository.GroupApplyRepository;
import com.web.curation.model.repository.GroupInfoRepository;
import com.web.curation.model.repository.GroupParticipantRepository;
import com.web.curation.model.repository.NaegiParticipantRepository;
import com.web.curation.model.repository.NaegiRepository;
import com.web.curation.model.repository.ScheduleParticipantRepository;
import com.web.curation.model.repository.ScheduleRepository;
import com.web.curation.model.repository.TimelineRepository;
import com.web.curation.model.repository.UserInfoRepository;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class GroupController {

	@Autowired
	UserInfoRepository userInfoRepository;

	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	GroupInfoRepository groupInfoRepository;

	@Autowired
	GroupParticipantRepository groupParticipantRepository;

	@Autowired
	AlarmRepository alarmRepository;

	@Autowired
	FriendInfoRepository friendInfoRepository;

	@Autowired
	GroupApplyRepository groupApplyRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	NaegiRepository naegiRepository;
	
	@Autowired
	NaegiParticipantRepository naegiParticipantRepository;
	
	@Autowired
	ScheduleRepository scheduleRepository;
	
	@Autowired
	ScheduleParticipantRepository scheduleParticipantRepository;
	
	@Autowired
	TimelineRepository timelineRepository;
	
	@Autowired
	FiletestRepository filetestRepository;
	
	@PostMapping("/findGnoByBno")
	public Object findGnoByBno(@RequestBody Map<String, String> map) {
		Map<String, Object> resultMap = new HashMap<>();
		
		Optional<Board> optBoard = boardRepository.findById(Integer.parseInt(map.get("bno")));
		
		if(optBoard.isPresent()) {
			resultMap.put("gno", optBoard.get().getBgno());
			resultMap.put("is-success", true);
		}
		else resultMap.put("is-success", false);
		
		return resultMap;
	}
	
	@PostMapping("/unoOfGmaster")
	public Object unoOfGmaster(@RequestParam int gno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		int gmaster=groupInfoRepository.findById(gno).get().getGmaster();
		resultMap.put("gmasterUno",gmaster);
		
		return resultMap;
	}
	
	@PostMapping("/isGroupMember")//0:미가입, 1:가입신청상태, 2:초대미수락상태, 3:그룹원, 4:그룹장
	public Object isGroupMember(@RequestParam String email,@RequestParam int gno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		if(!groupInfoRepository.findById(gno).isPresent()) {
			resultMap.put("isExist",false);
			return resultMap;
		}
		resultMap.put("isExist",true);
		
		UserInfo userInfo=userInfoRepository.findByEmail(email);
		
		if(groupApplyRepository.findByUnoAndGno(userInfo.getUno(), gno).isPresent()) {
			if(groupApplyRepository.findByUnoAndGno(userInfo.getUno(), gno).get().isAisApply())
				resultMap.put("memberStatus",1);
			else
				resultMap.put("memberStatus",2);
		}
		
		else if(groupInfoRepository.findByGnoAndGmaster(gno, userInfo.getUno()).isPresent())
			resultMap.put("memberStatus",4);
		else if(groupParticipantRepository.findByUnoAndGno(userInfo.getUno(), gno).isPresent())
			resultMap.put("memberStatus",3);
		else
			resultMap.put("memberStatus",0);
		
		
		return resultMap;
	}
	
	@PostMapping("/getGroupApplier")
	public Object getGroupApplier(@RequestParam int gno) {
		Map<String, Object> resultMap = new HashMap<>();

		Optional<List<GroupApply>> list = groupApplyRepository.findAllByGno(gno);
		if(list.isPresent()) {
			List<Integer> intList=new ArrayList<>();
			for(GroupApply ga:list.get()) 
				intList.add(ga.getUno());
			
			List<UserInfo> applierList=userInfoRepository.findAllByUnoIn(intList);
			
			resultMap.put("applierList", applierList);
		}

		return resultMap;
	}
	
	@GetMapping("/getCategory")
	public Object getCategory() {
		Map<String, Object> resultMap = new HashMap<>();
		
		List<Category> categories = categoryRepository.findAll();
		resultMap.put("categories", categories);
		
		return resultMap;
	}
	
	@PostMapping("/getUserListInGroup")
	public Object getUserListInGroup(@RequestParam int gno) {
		Map<String, Object> resultMap = new HashMap<>();

		List<GroupParticipant> list = groupParticipantRepository.findAllByGno(gno);
		List<Integer> unoList = new ArrayList<>();
		for (GroupParticipant gp : list)
			unoList.add(gp.getUno());

		List<UserInfo> userList = userInfoRepository.findAllByUnoIn(unoList);

		resultMap.put("userList", userList);

		return resultMap;
	}

	@PostMapping("/getGroupInfo")
	public Object getGroupInfo(@RequestParam int gno) throws SQLException {
		Map<String, Object> resultMap = new HashMap<>();
		
		GroupInfo groupInfo = groupInfoRepository.findById(gno).get();
		
		GroupDto gd=new GroupDto();
		gd.setGboundary(groupInfo.getGboundary());
		gd.setGcategory(groupInfo.getGcategory());
		gd.setGdate(groupInfo.getGdate());
		gd.setGdesc(groupInfo.getGdesc());
		gd.setGimg(groupInfo.getGimg().getBytes(1l, (int)groupInfo.getGimg().length()));
		gd.setGmaster(groupInfo.getGmaster());
		gd.setGname(groupInfo.getGname());
		gd.setGno(groupInfo.getGno());
		gd.setGuserList(groupInfo.getGuserList());
		
		resultMap.put("groupInfo", gd);
		
		return resultMap;
	}
	
	@PostMapping("/getGroupList")
	public Object getGroupList(@RequestBody Map<String, String> map) throws SQLException {
		Map<String, Object> resultMap = new HashMap<>();
		UserInfo userInfo = userInfoRepository.findByEmail(map.get("email"));
		int uno = userInfo.getUno();
		
		if(userInfo!=null) {
			List<GroupParticipant> list = groupParticipantRepository.findAllByUno(uno);
			List<Integer> gnoList = new ArrayList<>();
			
			if(list.size()!=0) {
				for (GroupParticipant gp : list) {
					gnoList.add(gp.getGno());
				}
				
				List<GroupInfo> groupList = groupInfoRepository.findAllByGnoIn(gnoList);
				
				if(groupList.size()!=0) {
					List<GroupInfo> myGroupList = new ArrayList<>();
					List<GroupInfo> otherGroupList = new ArrayList<>();
					
					for(GroupInfo groupInfo: groupList) {
						if(uno==groupInfo.getGmaster()) {
							myGroupList.add(groupInfo);
						}
						else {
							otherGroupList.add(groupInfo);
						}
					}
					
					groupList.clear();
					
					for(GroupInfo groupInfo: myGroupList) {
						groupList.add(groupInfo);
					}
					
					for(GroupInfo groupInfo: otherGroupList) {
						groupList.add(groupInfo);
					}
					
					List<GroupDto> groupDtoList=new ArrayList<>();
					for(GroupInfo groupInfo:groupList) {
						GroupDto gd=new GroupDto();
						gd.setGboundary(groupInfo.getGboundary());
						gd.setGcategory(groupInfo.getGcategory());
						gd.setGdate(groupInfo.getGdate());
						gd.setGdesc(groupInfo.getGdesc());
						gd.setGimg(groupInfo.getGimg().getBytes(1l, (int)groupInfo.getGimg().length()));
						gd.setGmaster(groupInfo.getGmaster());
						gd.setGname(groupInfo.getGname());
						gd.setGno(groupInfo.getGno());
						gd.setGuserList(groupInfo.getGuserList());
						groupDtoList.add(gd);
					}
					resultMap.put("groupList", groupDtoList);
					resultMap.put("is-success", true);
				}
				else {
					resultMap.put("is-success", false);
				}
			}
			else {
				resultMap.put("is-success", false);
			}
		}
		
		return resultMap;
	}
	
	@GetMapping("/getGroupList/{friendUno}")
	public Object getGroupList(@PathVariable int friendUno) throws SQLException {
		Map<String, Object> resultMap = new HashMap<>();
		
		List<GroupParticipant> list = groupParticipantRepository.findAllByUno(friendUno);
		List<Integer> gnoList = new ArrayList<>();
		
		if(list.size()!=0) {
			for (GroupParticipant gp : list)
				gnoList.add(gp.getGno());
			
			List<GroupInfo> groupList = groupInfoRepository.findAllByGnoIn(gnoList);
			
			if(groupList.size()!=0) {
				resultMap.put("groupList", groupList);
				
				List<GroupDto> groupDtoList=new ArrayList<>();
				for(GroupInfo groupInfo:groupList) {
					GroupDto gd=new GroupDto();
					gd.setGboundary(groupInfo.getGboundary());
					gd.setGcategory(groupInfo.getGcategory());
					gd.setGdate(groupInfo.getGdate());
					gd.setGdesc(groupInfo.getGdesc());
					gd.setGimg(groupInfo.getGimg().getBytes(1l, (int)groupInfo.getGimg().length()));
					gd.setGmaster(groupInfo.getGmaster());
					gd.setGname(groupInfo.getGname());
					gd.setGno(groupInfo.getGno());
					gd.setGuserList(groupInfo.getGuserList());
					groupDtoList.add(gd);
				}
				resultMap.put("groupList", groupDtoList);
				resultMap.put("is-success", true);
			}
			else {
				resultMap.put("is-success", false);
			}
		}
		else {
			resultMap.put("is-success", false);
		}
	
		return resultMap;
	}

	@PostMapping("/createGroup")
	public Object createGroup(@RequestBody Map<String, String> map) {
		Map<String, Object> resultMap = new HashMap<>();

		int gmaster = Integer.parseInt(map.get("uno"));
		int gcategory = Integer.parseInt(map.get("gcategory"));
		int gboundary = Integer.parseInt(map.get("gboundary"));
		String gname = map.get("gname");
		String gdesc = map.get("gdesc");
		Optional<UserInfo> optUserInfo = userInfoRepository.findById(gmaster);
		
		Timeline timeline=new Timeline();
		timeline.setTcontent("그룹생성");
		timeline.setTcontentSecond(gname);
		timeline.setUno(gmaster);
		timelineRepository.save(timeline);
		
		if(optUserInfo.isPresent()) {
			UserInfo user = optUserInfo.get();
			
			GroupInfo groupInfo = new GroupInfo();
			groupInfo.setGmaster(gmaster);
			groupInfo.setGname(gname);
			groupInfo.setGdesc(gdesc);
			groupInfo.setGcategory(gcategory);
			groupInfo.setGboundary(gboundary);
			groupInfo.setGuserList(Integer.toString(gmaster)+" ");
			groupInfo.setGimg(filetestRepository.findById(11).get().getFblob());
			groupInfo = groupInfoRepository.save(groupInfo);
			
			GroupParticipant groupParticipant = new GroupParticipant();
			groupParticipant.setGno(groupInfo.getGno());
			groupParticipant.setUno(gmaster);
			groupParticipantRepository.save(groupParticipant);
			
			if(gboundary!=0) {	// 비공개가 아닐 경우
				List<FriendInfo> friendList = getFriendList(gmaster);
				StringBuilder sb = new StringBuilder();
				sb.append("내 친구 ");
				sb.append(user.getUname());
				sb.append("님이 새로운 그룹, [");
				sb.append(gname);
				sb.append("]을 만들었어요.");
				
				for(FriendInfo friendInfo: friendList) {
					Alarm alarm=new Alarm();
					alarm.setAtype(1);
					alarm.setAurl("GroupMainPage");
					alarm.setAuser(friendInfo.getMyId());
					alarm.setCreateUser(gmaster);
					alarm.setAurlNo(groupInfo.getGno());
					alarm.setAsummary(sb.toString());
					alarmRepository.save(alarm);
				}
				
				if(gboundary==2) {	// 친구의 친구까지일 경우
					//임찬규 박봉현 님의 친구인 이기호님이 그룹 만들었습니다.
					//친구의 친구를 세트에 담는다
					//세트에서 내 친구들과 나 제외
					//친구의친구 수만큼 반복문
					//내 친구와 해당 친구의 친구의 공통 친구들을 찾는다
					List<Integer> friendFriendList = new ArrayList<>();
					List<Integer> friendIntList = new ArrayList<>();
					
					friendIntList.add(gmaster);
					
					for(FriendInfo friendInfo: friendList) {
						List<FriendInfo> toFindFriendFriend = getFriendList(friendInfo.getMyId());
						friendIntList.add(friendInfo.getMyId());
						
						for(FriendInfo friendFriend: toFindFriendFriend) {
							friendFriendList.add(friendFriend.getMyId());
						}
					}
					
					HashSet<Integer> tmp = new HashSet<>(friendFriendList);
					friendFriendList = new ArrayList<>(tmp);
					friendFriendList.removeAll(friendIntList);
					
					for(Integer ff: friendFriendList) {
						StringBuilder sb2 = new StringBuilder();
						
						sb2.append("친구 (");
						for(Integer f: friendIntList) {
							if(isFriendFriend(gmaster, f, ff)) {
								sb2.append(userInfoRepository.findById(f).get().getUname());
								sb2.append(" ");
							}
						}
						
						if(sb2.length()==0) continue;

						sb2.append(")의 친구 ");
						sb2.append(user.getUname());
						sb2.append("님이 새로운 그룹 [");
						sb2.append(gname);
						sb2.append("]을 만들었어요.");
						String asummary = sb2.toString();
						
						Alarm alarm=new Alarm();
						alarm.setAuser(ff);
						alarm.setAurl("GroupMainPage");
						alarm.setCreateUser(gmaster);
						alarm.setAurlNo(groupInfo.getGno());
						alarm.setAtype(1);
						alarm.setAsummary(asummary);
						
						alarmRepository.save(alarm);
					}
					resultMap.put("is-success", true);
				}
				else resultMap.put("is-success", true);
			}
			else resultMap.put("is-success", true);
		}
		else resultMap.put("is-success", false);
		
		return resultMap;
	}

	@PostMapping("/delGroup")
	public Object delGroup(@RequestParam int gno) {
		Map<String, Object> resultMap = new HashMap<>();

		GroupInfo groupInfo = groupInfoRepository.findById(gno).get();
		groupInfoRepository.delete(groupInfo);

		resultMap.put("data", "그룹 삭제에 성공했습니다.");

		return resultMap;
	}

	@PostMapping("/changeGroupMaster")
	public Object changeGroupMaster(@RequestParam int gno, @RequestParam int nextMaster) {
		Map<String, Object> resultMap = new HashMap<>();

		GroupInfo groupInfo = groupInfoRepository.findById(gno).get();
		groupInfo.setGmaster(nextMaster);
		groupInfoRepository.save(groupInfo);

		resultMap.put("data", "그룹장 인수인계 완료!");
		return resultMap;
	}

	@PostMapping("/inviteGroup")
	public Object inviteGroup(@RequestParam String email, @RequestParam int friendId, @RequestParam int gno) {
		Map<String, Object> resultMap = new HashMap<>();

		if(groupParticipantRepository.findByUnoAndGno(friendId, gno).isPresent()) {
			resultMap.put("result",false);
			resultMap.put("msg","이미 그룹원인 친구를 초대할 수는 없어요 ㅠ.ㅠ");
			return resultMap;
		}else if(groupApplyRepository.findByUnoAndGno(friendId, gno).isPresent()) {
			resultMap.put("result",false);
			resultMap.put("msg","이미 그룹에 신청하거나 초대받은 친구네요!");
			return resultMap;
		}
		UserInfo myInfo = userInfoRepository.findByEmail(email);
		GroupInfo groupInfo = groupInfoRepository.findById(gno).get();
		Alarm alarm = new Alarm();

		StringBuilder sb = new StringBuilder();
		sb.append("와! ");
		sb.append(myInfo.getUname());
		sb.append("님이 [");
		sb.append(groupInfo.getGname());
		sb.append("]로의 초대장을 보냈네요!");
		alarm.setAsummary(sb.toString());
		alarm.setAuser(friendId);
		alarm.setCreateUser(myInfo.getUno());
		alarm.setAurlNo(gno);
		alarm.setAtype(0);
		alarm.setAurl("GroupMainPage");
		alarmRepository.save(alarm);

		GroupApply groupApply = new GroupApply();
		groupApply.setAisApply(false);
		groupApply.setGno(groupInfo.getGno());
		groupApply.setUno(friendId);

		groupApplyRepository.save(groupApply);
		resultMap.put("result",true);
		return resultMap;
	}

	@PostMapping("/acceptInviteGroup")
	public Object acceptInviteGroup(@RequestParam String email, @RequestParam int gno) {
		Map<String, Object> resultMap = new HashMap<>();

		UserInfo myInfo = userInfoRepository.findByEmail(email);

		GroupApply groupApply = groupApplyRepository.findByUnoAndGno(myInfo.getUno(), gno).get();
		groupApplyRepository.delete(groupApply);

		GroupInfo groupInfo = groupInfoRepository.findById(gno).get();
		GroupParticipant groupParticipant = new GroupParticipant();
		groupParticipant.setGno(gno);
		groupParticipant.setUno(myInfo.getUno());
		groupParticipantRepository.save(groupParticipant);

		
		Timeline timeline=new Timeline();
		timeline.setTcontent("가입");
		timeline.setTcontentSecond(groupInfo.getGname());
		timeline.setUno(myInfo.getUno());
		timelineRepository.save(timeline);
		
		Optional<List<Schedule>> schedules=scheduleRepository.findAllBySgno(gno);
		if(schedules.isPresent()) {
			for(Schedule s:schedules.get()) {
				ScheduleParticipant sp=new ScheduleParticipant();
				sp.setSno(s.getSno());
				sp.setUno(myInfo.getUno());
				scheduleParticipantRepository.save(sp);
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		String nenddate=format.format(new Date());
		Optional<List<Naegi>> naegis=naegiRepository.findAllByNgnoAndNenddateGreaterThanEqualOrderByNenddateAsc(gno, nenddate);
		if(naegis.isPresent()) {
			for(Naegi n:naegis.get()) {
				NaegiParticipant np=new NaegiParticipant();
				np.setNno(n.getNno());
				np.setUno(myInfo.getUno());
				naegiParticipantRepository.save(np);
			}
		}
		
		List<GroupParticipant> list = groupParticipantRepository.findAllByGno(gno);
		StringBuilder sb = new StringBuilder();
		for (GroupParticipant gp : list) {
			sb.append(gp.getUno());
			sb.append(" ");
		}
		
		List<FriendInfo> friendList=getFriendList(myInfo.getUno());
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append("반가워요, ");
		sb2.append(myInfo.getUname());
		sb2.append("님도 이제 [");
		sb2.append(groupInfo.getGname());
		sb2.append("]의 멤버예요!");
		for(FriendInfo fi:friendList) {
			Alarm alarm = new Alarm();
			alarm.setAtype(1);
			alarm.setAurl("GroupMainPage");
			alarm.setAuser(fi.getMyId());
			alarm.setCreateUser(myInfo.getUno());
			alarm.setAurlNo(gno);
			alarm.setAsummary(sb2.toString());
			alarmRepository.save(alarm);
		}

		groupInfo.setGuserList(sb.toString());
		groupInfoRepository.save(groupInfo);
		resultMap.put("data", "그룹에 가입했습니다!");

		return resultMap;
	}
	
	@PostMapping("/denyInviteGroup")
	public Object denyInviteGroup(@RequestParam String email, @RequestParam int gno) {
		Map<String,Object> resultMap=new HashMap<>();
		int uno=userInfoRepository.findByEmail(email).getUno();
		Optional<GroupApply> groupApply=groupApplyRepository.findByUnoAndGno(uno, gno);
		if(groupApply.isPresent()) 
			groupApplyRepository.delete(groupApply.get());
		
		resultMap.put("data","초대를 거절했습니다.");
		
		return resultMap;
	}
	
	@PostMapping("/denyApplyGroup")
	public Object denyApplyGroup(@RequestParam int uno, @RequestParam int gno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		Optional<GroupApply> groupApply=groupApplyRepository.findByUnoAndGno(uno, gno);
		if(groupApply.isPresent()) 
			groupApplyRepository.delete(groupApply.get());
		
		resultMap.put("data","가입 신청을 거절했습니다.");
		
		return resultMap;
	}

	@PostMapping("/applyGroup")
	public Object applyGroup(@RequestBody Map<String, String> map) {
		Map<String, Object> resultMap = new HashMap<>();
		
		int uno = Integer.parseInt(map.get("uno"));
		int gno = Integer.parseInt(map.get("gno"));
		
		if(groupApplyRepository.findByUnoAndGno(uno, gno).isPresent()) {
			resultMap.put("is-success", 2);
			return resultMap;
		}
		else if(groupParticipantRepository.findByUnoAndGno(uno, gno).isPresent()) {
			resultMap.put("is-success", 3);
			return resultMap;
		}
		
		GroupApply groupApply = new GroupApply();
		groupApply.setAisApply(true);
		groupApply.setGno(gno);
		groupApply.setUno(uno);
		
		groupApplyRepository.save(groupApply);

		GroupInfo groupInfo = groupInfoRepository.findById(gno).get();
		Optional<UserInfo> user = userInfoRepository.findById(uno);
		
		if(user.isPresent()) {
			Alarm alarm = new Alarm();
			alarm.setAtype(0);
			alarm.setAurl("GroupMainPage");
			alarm.setAuser(groupInfo.getGmaster());
			alarm.setCreateUser(uno);
			alarm.setAurlNo(gno);
			StringBuilder sb = new StringBuilder();
			sb.append(user.get().getUname());
			sb.append("님이 [");
			sb.append(groupInfo.getGname());
			sb.append("]에 가입하고 싶어해요.");
			alarm.setAsummary(sb.toString());
			
			alarmRepository.save(alarm);
			
			resultMap.put("is-success", 1);
		}
		else resultMap.put("is-success", 0);

		return resultMap;
	}
	
	@PostMapping("/getGroupApplyList")
	public Object getGroupApplyList(@RequestParam int gno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		List<GroupApply> list=null;
		if(groupApplyRepository.findAllByGno(gno).isPresent()) 
			list=groupApplyRepository.findAllByGno(gno).get();
		
		resultMap.put("applyList",list);
		
		return resultMap;
	}

	@PostMapping("/acceptApplyGroup")
	public Object acceptJoinoinGroup(@RequestParam int uno, @RequestParam int gno) {
		Map<String, Object> resultMap = new HashMap<>();

		GroupApply groupApply = groupApplyRepository.findByUnoAndGno(uno, gno).get();
		groupApplyRepository.delete(groupApply);

		GroupInfo groupInfo = groupInfoRepository.findById(gno).get();

		GroupParticipant groupParticipant = new GroupParticipant();
		groupParticipant.setGno(gno);
		groupParticipant.setUno(uno);
		groupParticipantRepository.save(groupParticipant);
		
		
		Timeline timeline=new Timeline();
		timeline.setTcontent("가입");
		timeline.setTcontentSecond(groupInfo.getGname());
		timeline.setUno(uno);
		timelineRepository.save(timeline);
				
		
		Optional<List<Schedule>> schedules=scheduleRepository.findAllBySgno(gno);
		if(schedules.isPresent()) {
			for(Schedule s:schedules.get()) {
				ScheduleParticipant sp=new ScheduleParticipant();
				sp.setSno(s.getSno());
				sp.setUno(uno);
				scheduleParticipantRepository.save(sp);
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		String nenddate=format.format(new Date());
		Optional<List<Naegi>> naegis=naegiRepository.findAllByNgnoAndNenddateGreaterThanEqualOrderByNenddateAsc(gno, nenddate);
		if(naegis.isPresent()) {
			for(Naegi n:naegis.get()) {
				NaegiParticipant np=new NaegiParticipant();
				np.setNno(n.getNno());
				np.setUno(uno);
				naegiParticipantRepository.save(np);
			}
		}
		UserInfo myInfo=userInfoRepository.findById(uno).get();
List<FriendInfo> friendList=getFriendList(myInfo.getUno());
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append("반가워요, ");
		sb2.append(myInfo.getUname());
		sb2.append("님도 이제 [");
		sb2.append(groupInfo.getGname());
		sb2.append("]의 멤버예요!");
		for(FriendInfo fi:friendList) {
			Alarm alarm = new Alarm();
			alarm.setAtype(1);
			alarm.setAurl("GroupMainPage");
			alarm.setAuser(fi.getMyId());
			alarm.setCreateUser(myInfo.getUno());
			alarm.setAurlNo(gno);
			alarm.setAsummary(sb2.toString());
			alarmRepository.save(alarm);
		}

		List<GroupParticipant> gpList = groupParticipantRepository.findAllByGno(gno);

		StringBuilder sb = new StringBuilder();
		for (GroupParticipant gp : gpList) {
			sb.append(gp.getUno());
			sb.append(" ");
		}
		groupInfo.setGuserList(sb.toString());
		groupInfoRepository.save(groupInfo);

		resultMap.put("data", "가입 요청을 승인했습니다. 반가워~");

		return resultMap;
	}

	@PostMapping("/getoutGroup")
	public Object getoutGroup(@RequestParam int gno, @RequestParam String email) {
		Map<String, Object> resultMap = new HashMap<>();

		UserInfo myInfo = userInfoRepository.findByEmail(email);
		GroupInfo groupInfo = groupInfoRepository.findById(gno).get();

		GroupParticipant groupParticipant = groupParticipantRepository.findByUnoAndGno(myInfo.getUno(), gno).get();
		groupParticipantRepository.delete(groupParticipant);

		List<GroupParticipant> gpList = groupParticipantRepository.findAllByGno(gno);

		StringBuilder sb = new StringBuilder();
		for (GroupParticipant gp : gpList) {
			sb.append(gp.getUno());
			sb.append(" ");
		}
		groupInfo.setGuserList(sb.toString());
		groupInfoRepository.save(groupInfo);

		resultMap.put("data", "그룹에서 탈퇴했습니다. 안녕~");

		return resultMap;
	}

	@PostMapping("/banishMember")
	public Object banishMember(@RequestParam int gno, @RequestParam int uno) {
		Map<String, Object> resultMap = new HashMap<>();

		GroupInfo groupInfo = groupInfoRepository.findById(gno).get();

		GroupParticipant groupParticipant = groupParticipantRepository.findByUnoAndGno(uno, gno).get();
		groupParticipantRepository.delete(groupParticipant);

		List<GroupParticipant> gpList = groupParticipantRepository.findAllByGno(gno);

		StringBuilder sb = new StringBuilder();
		for (GroupParticipant gp : gpList) {
			sb.append(gp.getUno());
			sb.append(" ");
		}
		groupInfo.setGuserList(sb.toString());
		groupInfoRepository.save(groupInfo);

		resultMap.put("data", "그룹에서 추방했습니다. 잘가!");

		return resultMap;
	}

	public List<FriendInfo> getFriendList(int uno){
		Optional<List<FriendInfo>> friendList = friendInfoRepository.findAllByMyId(uno);

		List<FriendInfo> toReturnFriendList=new ArrayList<>();
		if (friendList.isPresent()) {
			for (FriendInfo friendInfo : friendList.get()) {
				Optional<FriendInfo> fi = friendInfoRepository.findByMyIdAndFriendId(friendInfo.getFriendId(),
						uno);
				if (fi.isPresent()) 
					toReturnFriendList.add(fi.get());
				
			}
		}
		
		return toReturnFriendList;
	}

	public boolean isFriendFriend(int id1, int bridge, int id2) {
		if (friendInfoRepository.findByMyIdAndFriendId(id1, bridge).isPresent()
				&& friendInfoRepository.findByMyIdAndFriendId(bridge, id1).isPresent()
				&& friendInfoRepository.findByMyIdAndFriendId(id2, bridge).isPresent()
				&& friendInfoRepository.findByMyIdAndFriendId(bridge, id2).isPresent())
			return true;
		return false;

	}	
}
