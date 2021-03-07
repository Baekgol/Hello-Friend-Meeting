package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.NaegiParticipant;

public interface NaegiParticipantRepository extends JpaRepository<NaegiParticipant,Integer> {
	Optional<List<NaegiParticipant>> findAllByNno(int nno);
	Optional<NaegiParticipant> findByNnoAndUno(int nno,int uno);
}
