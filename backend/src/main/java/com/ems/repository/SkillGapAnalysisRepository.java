package com.ems.repository;

import com.ems.entity.SkillGapAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillGapAnalysisRepository
        extends JpaRepository<SkillGapAnalysis, Long> {

    List<SkillGapAnalysis> findByJobId(Long jobId);

    List<SkillGapAnalysis> findByCandidateId(Long candidateId);
}