package com.ems.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResumeExperienceExtractionService {

    // Extract experience information from processed resume text
    public String extractExperience(String processedText) {

        // Check if resume text is empty
        if (processedText == null || processedText.isBlank()) {
            return "";
        }

        // Pattern for years of experience
        Pattern yearsPattern = Pattern.compile(
                "(\\d+(?:\\.\\d+)?)\\s*\\+?\\s*(years?|yrs?)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = yearsPattern.matcher(processedText);

        if (matcher.find()) {
            return matcher.group(1) + " years";
        }

        // Pattern for months of experience
        Pattern monthsPattern = Pattern.compile(
                "(\\d+)\\s*\\+?\\s*(months?|mos?)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher monthsMatcher = monthsPattern.matcher(processedText);

        if (monthsMatcher.find()) {
            return monthsMatcher.group(1) + " months";
        }

        return "";
    }
}