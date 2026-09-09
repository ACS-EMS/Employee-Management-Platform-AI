package com.ems.repository;

import com.ems.entity.CandidateRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRankingRepository extends JpaRepository<CandidateRanking, Long> {

    List<CandidateRanking> findByJobIdOrderByRankAsc(Long jobId);
}