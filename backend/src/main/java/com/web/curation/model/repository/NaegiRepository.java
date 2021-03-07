package com.web.curation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.model.entity.Naegi;

public interface NaegiRepository extends JpaRepository<Naegi,Integer> {
	public Optional<List<Naegi>> findAllByNgnoAndNenddateLessThanOrderByNenddateDesc(int ngno, String nenddate);
	public Optional<List<Naegi>> findAllByNgnoAndNenddateGreaterThanEqualOrderByNenddateAsc(int ngno, String nenddate);
}
