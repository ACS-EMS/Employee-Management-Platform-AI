package com.ems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AICandidateMatching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long candidateId;

    private Long jobId;

    private Double skillMatch;

    private Double experienceMatch;

    private Double educationMatch;

    private Double certificationMatch;

    private Double locationMatch;

    private Double roleMatch;

    private Double overallMatchScore;

    @Column(columnDefinition = "TEXT")
    private String explanation;
}