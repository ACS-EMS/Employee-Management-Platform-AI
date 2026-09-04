package com.ems.ems.repository;

import com.ems.ems.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // Search jobs by title
    List<Job> findByTitleContainingIgnoreCase(String title);

    // Search jobs by location
    List<Job> findByLocationContainingIgnoreCase(String location);

    // Search jobs by title and location
    List<Job> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
            String title,
            String location
    );
}