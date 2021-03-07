package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.ScheduleParticipant;

public interface ScheduleParticipantRepository extends JpaRepository<ScheduleParticipant,Integer>{
	public Optional<List<ScheduleParticipant>> findAllBySno(int sno);
	public Optional<ScheduleParticipant> findBySnoAndUno(int sno, int uno);
}
