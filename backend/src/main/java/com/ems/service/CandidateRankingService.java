package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.dto.CandidateRankingRequestDto;
import com.ems.dto.CandidateRankingResponseDto;
import com.ems.entity.AICandidateMatching;
import com.ems.entity.CandidateRanking;
import com.ems.mapper.CandidateRankingMapper;
import com.ems.repository.AICandidateMatchingRepository;
import com.ems.repository.CandidateRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CandidateRankingService {

    @Autowired
    private CandidateRankingRepository rankingRepository;

    @Autowired
    private AICandidateMatchingRepository matchingRepository;

    @Autowired
    private CandidateRankingMapper rankingMapper;


    public ResponseEntity<ApiResponse<List<CandidateRankingResponseDto>>> rankCandidates(
            CandidateRankingRequestDto requestDto) {

        try {

            List<AICandidateMatching> matchingResults =
                    matchingRepository.findAll()
                            .stream()
                            .filter(matching ->
                                    matching.getJobId() != null
                                            && matching.getJobId()
                                            .equals(requestDto.getJobId()))
                            .collect(Collectors.toList());

            if (matchingResults.isEmpty()) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(
                                false,
                                "No candidate matching results found for job ID: "
                                        + requestDto.getJobId(),
                                null
                        ));
            }


            /*
             * Keep only one matching result for each candidate.
             * If multiple matching records exist for the same candidate,
             * the record with the highest match score is kept.
             */
            Map<Long, AICandidateMatching> uniqueCandidates =
                    new LinkedHashMap<>();

            for (AICandidateMatching matching : matchingResults) {

                Long candidateId = matching.getCandidateId();

                if (candidateId == null) {
                    continue;
                }

                if (!uniqueCandidates.containsKey(candidateId)) {

                    uniqueCandidates.put(candidateId, matching);

                } else {

                    AICandidateMatching existing =
                            uniqueCandidates.get(candidateId);

                    double existingScore =
                            existing.getOverallMatchScore() != null
                                    ? existing.getOverallMatchScore()
                                    : 0.0;

                    double newScore =
                            matching.getOverallMatchScore() != null
                                    ? matching.getOverallMatchScore()
                                    : 0.0;

                    if (newScore > existingScore) {
                        uniqueCandidates.put(candidateId, matching);
                    }
                }
            }


            /*
             * Sort candidates from highest match score
             * to lowest match score.
             */
            List<AICandidateMatching> sortedCandidates =
                    new ArrayList<>(uniqueCandidates.values());

            sortedCandidates.sort(
                    Comparator.comparing(
                            AICandidateMatching::getOverallMatchScore,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );


            /*
             * Remove old rankings for this job before
             * creating the latest ranking.
             */
            rankingRepository.deleteAll(
                    rankingRepository.findByJobIdOrderByRankAsc(
                            requestDto.getJobId()
                    )
            );


            List<CandidateRanking> rankings =
                    new ArrayList<>();

            int rank = 1;

            for (AICandidateMatching matching : sortedCandidates) {

                CandidateRanking ranking =
                        new CandidateRanking();

                ranking.setCandidateId(
                        matching.getCandidateId()
                );

                ranking.setJobId(
                        matching.getJobId()
                );

                ranking.setMatchScore(
                        matching.getOverallMatchScore()
                );

                ranking.setRank(rank);

                rankings.add(ranking);

                rank++;
            }


            List<CandidateRanking> savedRankings =
                    rankingRepository.saveAll(rankings);


            List<CandidateRankingResponseDto> response =
                    savedRankings.stream()
                            .map(rankingMapper::toResponseDto)
                            .collect(Collectors.toList());


            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            true,
                            "Candidates ranked successfully",
                            response
                    ));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to rank candidates: "
                                    + e.getMessage(),
                            null
                    ));
        }
    }


    public ResponseEntity<ApiResponse<List<CandidateRankingResponseDto>>> getRankingsByJob(
            Long jobId) {

        try {

            List<CandidateRankingResponseDto> rankings =
                    rankingRepository
                            .findByJobIdOrderByRankAsc(jobId)
                            .stream()
                            .map(rankingMapper::toResponseDto)
                            .collect(Collectors.toList());

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Candidate rankings retrieved successfully",
                            rankings
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to retrieve candidate rankings: "
                                    + e.getMessage(),
                            null
                    ));
        }
    }


    public ResponseEntity<ApiResponse<CandidateRankingResponseDto>> getRankingById(
            Long id) {

        try {

            CandidateRanking ranking =
                    rankingRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Candidate ranking not found with ID: "
                                                    + id
                                    )
                            );

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Candidate ranking retrieved successfully",
                            rankingMapper.toResponseDto(ranking)
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Failed to retrieve candidate ranking: "
                                    + e.getMessage(),
                            null
                    ));
        }
    }
}