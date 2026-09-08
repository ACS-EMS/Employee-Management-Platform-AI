package com.ems.service;

import org.springframework.stereotype.Service;

@Service
public class ResumeNlpProcessingService {

    // Process cleaned resume text
    public String processText(String cleanedText) {

        // Check if cleaned text is empty
        if (cleanedText == null || cleanedText.isBlank()) {
            return "";
        }

        // Convert text to a consistent format
        String processedText = cleanedText
                .trim()
                .replaceAll("\\s+", " ");

        return processedText;
    }
}