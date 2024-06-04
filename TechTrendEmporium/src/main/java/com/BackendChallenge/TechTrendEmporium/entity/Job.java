package com.BackendChallenge.TechTrendEmporium.entity;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table (name = "job") // Table name: job
public class Job {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id; // id should be unique to identify each job

    private String type; // type should be a string

    private String operation; // operation should be a string

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // user_id: this connects the job table with the user table

    private String action; // action should be a string
}

