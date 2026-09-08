package com.ems.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ResumeTextExtractionService {

    // Extract text from a resume file
    public String extractText(String filePath, String fileType)
            throws IOException {

        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException(
                    "Resume file path is required"
            );
        }

        if (fileType == null || fileType.isBlank()) {
            throw new IllegalArgumentException(
                    "Resume file type is required"
            );
        }

        return switch (fileType.toLowerCase()) {

            case "pdf" -> extractPdfText(filePath);

            case "doc" -> extractDocText(filePath);

            case "docx" -> extractDocxText(filePath);

            default -> throw new IllegalArgumentException(
                    "Unsupported resume file type: " + fileType
            );
        };
    }

    // Extract text from PDF
    private String extractPdfText(String filePath)
            throws IOException {

        byte[] fileBytes = Files.readAllBytes(
                Path.of(filePath)
        );

        try (var document = Loader.loadPDF(fileBytes)) {

            PDFTextStripper textStripper =
                    new PDFTextStripper();

            return textStripper.getText(document);
        }
    }

    // Extract text from DOC
    private String extractDocText(String filePath)
            throws IOException {

        try (InputStream inputStream =
                     Files.newInputStream(Path.of(filePath));
             HWPFDocument document =
                     new HWPFDocument(inputStream);
             WordExtractor extractor =
                     new WordExtractor(document)) {

            return extractor.getText();
        }
    }

    // Extract text from DOCX
    private String extractDocxText(String filePath)
            throws IOException {

        try (InputStream inputStream =
                     Files.newInputStream(Path.of(filePath));
             XWPFDocument document =
                     new XWPFDocument(inputStream);
             XWPFWordExtractor extractor =
                     new XWPFWordExtractor(document)) {

            return extractor.getText();
        }
    }
}