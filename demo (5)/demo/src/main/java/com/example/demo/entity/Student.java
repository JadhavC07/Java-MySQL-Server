package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Data
@Entity
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String studentName;
    private String fatherName;
    private String lastName;
    private LocalDate dob;
    private String course;
    private String bloodGroup;
    private String gender;
    private String email;
    private String password;
    private String errorMessage;
}
