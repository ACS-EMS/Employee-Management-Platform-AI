package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.dto.ResumeRequestDto;
import com.ems.dto.ResumeResponseDto;
import com.ems.entity.Resume;
import com.ems.mapper.ResumeMapper;
import com.ems.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private ResumeTextExtractionService resumeTextExtractionService;

    @Autowired
    private ResumeTextCleaningService resumeTextCleaningService;

    @Autowired
    private ResumeNlpProcessingService resumeNlpProcessingService;

    @Autowired
    private ResumeSkillExtractionService resumeSkillExtractionService;

    @Autowired
    private ResumeExperienceExtractionService resumeExperienceExtractionService;

    @Autowired
    private ResumeEducationExtractionService resumeEducationExtractionService;

    @Autowired
    private StructuredCandidateProfileService structuredCandidateProfileService;

    private static final String UPLOAD_DIR = "uploads/resumes";


    // Upload and process resume
    public ResponseEntity<ApiResponse<ResumeResponseDto>> uploadResume(
            ResumeRequestDto dto,
            MultipartFile file) {

        try {

            // 1. Validate candidate ID
            if (dto == null || dto.getCandidateId() == null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(
                                false,
                                "Candidate ID is required",
                                null
                        ));
            }

            // 2. Validate file
            if (file == null || file.isEmpty()) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(
                                false,
                                "Resume file is required",
                                null
                        ));
            }

            // 3. Get original file name
            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null || originalFileName.isBlank()) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(
                                false,
                                "Invalid resume file name",
                                null
                        ));
            }

            // 4. Get safe file name
            String fileName = Paths.get(originalFileName)
                    .getFileName()
                    .toString();

            // 5. Get file extension
            String extension = "";

            int dotIndex = fileName.lastIndexOf('.');

            if (dotIndex > 0) {

                extension = fileName
                        .substring(dotIndex + 1)
                        .toLowerCase();
            }

            // 6. Validate supported formats
            if (!extension.equals("pdf")
                    && !extension.equals("doc")
                    && !extension.equals("docx")) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(
                                false,
                                "Only PDF, DOC, and DOCX files are supported",
                                null
                        ));
            }

            // 7. Create upload directory
            Path uploadDirectory = Paths.get(UPLOAD_DIR);

            Files.createDirectories(uploadDirectory);

            // 8. Generate unique file name
            String storedFileName =
                    UUID.randomUUID() + "." + extension;

            // 9. Create complete file path
            Path filePath =
                    uploadDirectory.resolve(storedFileName);

            // 10. Store original resume file
            Files.copy(file.getInputStream(), filePath);

            // 11. Extract text
            String extractedText =
                    resumeTextExtractionService.extractText(
                            filePath.toString(),
                            extension
                    );

            // 12. Clean extracted text
            String cleanedText =
                    resumeTextCleaningService.cleanText(
                            extractedText
                    );

            // 13. NLP processing
            String processedText =
                    resumeNlpProcessingService.processText(
                            cleanedText
                    );

            // 14. Extract skills
            String extractedSkills =
                    resumeSkillExtractionService.extractSkills(
                            processedText
                    );

            // 15. Extract experience
            String extractedExperience =
                    resumeExperienceExtractionService.extractExperience(
                            processedText
                    );

            // 16. Extract education
            String extractedEducation =
                    resumeEducationExtractionService.extractEducation(
                            processedText
                    );

            // 17. Create Resume entity
            Resume resume = resumeMapper.toEntity(dto);

            resume.setFileName(fileName);
            resume.setFileType(extension);
            resume.setFilePath(filePath.toString());

            resume.setExtractedText(processedText);
            resume.setExtractedSkills(extractedSkills);
            resume.setExtractedExperience(extractedExperience);
            resume.setExtractedEducation(extractedEducation);

            // 18. Save resume
            Resume savedResume =
                    resumeRepository.save(resume);

            // 19. Create structured candidate profile
            structuredCandidateProfileService.createProfile(
                    savedResume
            );

            // 20. Convert Entity to Response DTO
            ResumeResponseDto responseDto =
                    resumeMapper.toResponseDto(savedResume);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            true,
                            "Resume uploaded and processed successfully",
                            responseDto
                    ));

        } catch (IOException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to upload or process resume",
                            null
                    ));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to process resume",
                            null
                    ));
        }
    }


    // Create resume record
    public ResponseEntity<ApiResponse<ResumeResponseDto>> createResume(
            ResumeRequestDto dto) {

        try {

            Resume resume = resumeMapper.toEntity(dto);

            Resume savedResume =
                    resumeRepository.save(resume);

            ResumeResponseDto responseDto =
                    resumeMapper.toResponseDto(savedResume);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            true,
                            "Resume created successfully",
                            responseDto
                    ));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to create resume",
                            null
                    ));
        }
    }


    // Get all resumes
    public ResponseEntity<ApiResponse<List<ResumeResponseDto>>> getAllResumes() {

        try {

            List<ResumeResponseDto> resumes =
                    resumeRepository.findAll()
                            .stream()
                            .map(resumeMapper::toResponseDto)
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Resumes retrieved successfully",
                            resumes
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to retrieve resumes",
                            null
                    ));
        }
    }


    // Get resume by ID
    public ResponseEntity<ApiResponse<ResumeResponseDto>> getResumeById(
            Long id) {

        try {

            Resume resume =
                    resumeRepository.findById(id).orElse(null);

            if (resume == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(
                                false,
                                "Resume not found",
                                null
                        ));
            }

            ResumeResponseDto responseDto =
                    resumeMapper.toResponseDto(resume);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Resume retrieved successfully",
                            responseDto
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to retrieve resume",
                            null
                    ));
        }
    }


    // Update resume
    public ResponseEntity<ApiResponse<ResumeResponseDto>> updateResume(
            Long id,
            ResumeRequestDto dto) {

        try {

            Resume existingResume =
                    resumeRepository.findById(id).orElse(null);

            if (existingResume == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(
                                false,
                                "Resume not found",
                                null
                        ));
            }

            existingResume.setCandidateId(
                    dto.getCandidateId()
            );

            Resume updatedResume =
                    resumeRepository.save(existingResume);

            ResumeResponseDto responseDto =
                    resumeMapper.toResponseDto(updatedResume);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Resume updated successfully",
                            responseDto
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to update resume",
                            null
                    ));
        }
    }


    // Delete resume
    public ResponseEntity<ApiResponse<String>> deleteResume(Long id) {

        try {

            Resume resume =
                    resumeRepository.findById(id).orElse(null);

            if (resume == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(
                                false,
                                "Resume not found",
                                null
                        ));
            }

            resumeRepository.delete(resume);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Resume deleted successfully",
                            null
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to delete resume",
                            null
                    ));
        }
    }
}