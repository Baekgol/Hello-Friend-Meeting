package com.web.curation.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.curation.model.entity.GroupInfo;
import com.web.curation.model.repository.FiletestRepository;
import com.web.curation.model.repository.GroupInfoRepository;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class FiletestController {

	@Autowired
	FiletestRepository filetestRepository;
	
	@Autowired
	GroupInfoRepository groupInfoRepository;
	
	@PostMapping("/fileUpload")
	public Object fileUpload(@RequestParam("file") MultipartFile file,@RequestParam("gno") int gno) {
		Map<String,Object> resultMap=new HashMap<>();
		
		
		
		byte[] bytes;
		GroupInfo gi=groupInfoRepository.findById(gno).get();
		
		resultMap.put("result",false);
		try {
			bytes=file.getBytes();
			
			try {
				Blob blob=new javax.sql.rowset.serial.SerialBlob(bytes);
				
				gi.setGimg(blob);
				groupInfoRepository.save(gi);
				resultMap.clear();
				resultMap.put("result",true);
			}catch(SerialException e1) {
				e1.printStackTrace();
			}catch(SQLException e1) {
				e1.printStackTrace();
			}
		}catch(IOException e2) {
			e2.printStackTrace();
		}
		
		return resultMap;
	}
	
}
