package com.ems.service;

import org.springframework.stereotype.Service;

@Service
public class ResumeTextCleaningService {

    // Clean extracted resume text
    public String cleanText(String extractedText) {

        // Check if extracted text is empty
        if (extractedText == null || extractedText.isBlank()) {
            return "";
        }

        // 1. Remove extra spaces
        String cleanedText = extractedText
                .replaceAll("\\s+", " ")
                .trim();

        // 2. Return cleaned resume text
        return cleanedText;
    }
}