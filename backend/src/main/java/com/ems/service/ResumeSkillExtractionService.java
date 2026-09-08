package com.ems.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeSkillExtractionService {

    // Extract skills from processed resume text
    public String extractSkills(String processedText) {

        // Check if resume text is empty
        if (processedText == null || processedText.isBlank()) {
            return "";
        }

        // List of skills to look for
        List<String> knownSkills = List.of(
                "java",
                "python",
                "javascript",
                "typescript",
                "spring",
                "spring boot",
                "react",
                "angular",
                "node.js",
                "sql",
                "postgresql",
                "mysql",
                "mongodb",
                "docker",
                "kubernetes",
                "git",
                "github",
                "aws",
                "azure",
                "machine learning",
                "artificial intelligence",
                "nlp"
        );

        List<String> extractedSkills = new ArrayList<>();

        String text = processedText.toLowerCase();

        // Check each known skill
        for (String skill : knownSkills) {

            if (text.contains(skill.toLowerCase())) {
                extractedSkills.add(skill);
            }
        }

        // Return skills as comma-separated text
        return String.join(", ", extractedSkills);
    }
}