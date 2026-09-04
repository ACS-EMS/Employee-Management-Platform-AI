package com.ems.ems.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String location;

    private String education;
    private String experience;
    private String skills;

    private String resume;
    private String linkedin;
    private String portfolio;

    private Double expectedSalary;
    private String noticePeriod;

    private String source;

    private LocalDateTime applicationDate;

    private String status;
}