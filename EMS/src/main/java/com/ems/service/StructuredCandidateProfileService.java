package com.ems.service;

import com.ems.entity.Resume;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class StructuredCandidateProfileService {

    // Create structured candidate profile
    public Map<String, Object> createProfile(Resume resume) {

        Map<String, Object> profile = new LinkedHashMap<>();

        profile.put("candidateId", resume.getCandidateId());
        profile.put("resumeId", resume.getId());
        profile.put("skills", resume.getExtractedSkills());
        profile.put("experience", resume.getExtractedExperience());
        profile.put("education", resume.getExtractedEducation());

        return profile;
    }
}