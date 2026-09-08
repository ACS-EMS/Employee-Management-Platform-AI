package com.ems.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResumeEducationExtractionService {

    // Extract education information from processed resume text
    public String extractEducation(String processedText) {

        // Check if resume text is empty
        if (processedText == null || processedText.isBlank()) {
            return "";
        }

        // Education keywords
        Pattern educationPattern = Pattern.compile(
                "(bachelor|b\\.tech|btech|b\\.e|be|master|m\\.tech|mtech|m\\.e|me|mba|phd|doctorate|diploma)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = educationPattern.matcher(processedText);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}