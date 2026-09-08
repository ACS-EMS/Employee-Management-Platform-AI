package com.ems.repository;

import com.ems.entity.AICandidateMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AICandidateMatchingRepository extends JpaRepository<AICandidateMatching, Long> {
}