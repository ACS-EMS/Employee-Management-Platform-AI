package com.ems.repository;

import com.ems.entity.AIResumeScreening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIResumeScreeningRepository extends JpaRepository<AIResumeScreening, Long> {
}