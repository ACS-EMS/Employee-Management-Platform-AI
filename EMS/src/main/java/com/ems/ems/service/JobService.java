package com.ems.ems.service;

import com.ems.ems.entity.Job;
import com.ems.ems.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // Create a new job
    public Job createJob(Job job) {

        if (job.getStatus() == null || job.getStatus().isBlank()) {
            job.setStatus("OPEN");
        }

        return jobRepository.save(job);
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Get one job by ID
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    // Update a job
    public Job updateJob(Long id, Job updatedJob) {

        Job existingJob = jobRepository.findById(id).orElse(null);

        if (existingJob == null) {
            return null;
        }

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDepartment(updatedJob.getDepartment());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setEmploymentType(updatedJob.getEmploymentType());

        existingJob.setExperienceMin(updatedJob.getExperienceMin());
        existingJob.setExperienceMax(updatedJob.getExperienceMax());

        existingJob.setSalaryMin(updatedJob.getSalaryMin());
        existingJob.setSalaryMax(updatedJob.getSalaryMax());

        existingJob.setRequiredSkills(updatedJob.getRequiredSkills());
        existingJob.setPreferredSkills(updatedJob.getPreferredSkills());

        existingJob.setEducation(updatedJob.getEducation());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setResponsibilities(updatedJob.getResponsibilities());

        existingJob.setOpenings(updatedJob.getOpenings());
        existingJob.setStatus(updatedJob.getStatus());

        existingJob.setClosingDate(updatedJob.getClosingDate());

        return jobRepository.save(existingJob);
    }

    // Close a job
    public Job closeJob(Long id) {

        Job existingJob = jobRepository.findById(id).orElse(null);

        if (existingJob == null) {
            return null;
        }

        existingJob.setStatus("CLOSED");

        return jobRepository.save(existingJob);
    }

    // Delete a job
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    // Search jobs
    public List<Job> searchJobs(String title, String location) {

        if (title != null && !title.isBlank()
                && location != null && !location.isBlank()) {

            return jobRepository
                    .findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
                            title,
                            location
                    );
        }

        if (title != null && !title.isBlank()) {
            return jobRepository.findByTitleContainingIgnoreCase(title);
        }

        if (location != null && !location.isBlank()) {
            return jobRepository.findByLocationContainingIgnoreCase(location);
        }

        return jobRepository.findAll();
    }
}