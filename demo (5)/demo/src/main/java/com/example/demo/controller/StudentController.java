package com.example.demo.controller;


import com.example.demo.entity.Student;
import com.example.demo.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentRepository repository;

    @PostMapping("/submitForm")
    public ResponseEntity<Student> handleSubmit(@RequestBody Student student) {

        if (repository.existsByEmail(student.getEmail())) {

            Student errorStudent = new Student();
            errorStudent.setEmail(student.getEmail());
            errorStudent.setStudentName(student.getStudentName());
            errorStudent.setErrorMessage("Duplicate Email Buddy");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorStudent);
        } else {
            Student student1 = repository.save(student);
            return ResponseEntity.ok(student1);

        }

    }

    @GetMapping("/getAllStudents")
    public List<Student> getAllStudent() {

        return repository.findAll();
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Student loginRequest) {
        List<Student> students = repository.findAllByEmail(loginRequest.getEmail());

        if (!students.isEmpty()) {
            boolean validCredentials = students.stream()
                    .anyMatch(student -> student.getPassword().equals(loginRequest.getPassword()));

            if (validCredentials) {
                Student loggedInStudent = students.get(0);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login Successful");
                response.put("userId", loggedInStudent.getId());
                response.put("Student Name ", loggedInStudent.getStudentName());
                response.put("Father Name", loggedInStudent.getFatherName());
                response.put("Last Name", loggedInStudent.getLastName());
                response.put("Gender", loggedInStudent.getGender());
                response.put("Blood Group", loggedInStudent.getBloodGroup());
                response.put("Course", loggedInStudent.getCourse());
                response.put("Password", loggedInStudent.getPassword());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Invalid Password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "User Not Found"));
        }
    }

    @PostMapping("/addStudent")
    public ResponseEntity<?> addStudents(@RequestBody List<Student> students) {
        try {
            List<String> existingEmails = new ArrayList<>();
            List<Student> savedStudents = new ArrayList<>();

            for (Student student : students) {

                if (repository.existsByEmail(student.getEmail())) {
                    existingEmails.add(student.getEmail());
                } else {
                    savedStudents.add(repository.save(student));
                }
            }
            if (!existingEmails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Emails already exist: " + existingEmails);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedStudents);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the students: " + e.getMessage());
        }
    }


}