package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.Student;
import com.mondlimqanya.WriteTests.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    // Using a Set to store unique students
    private final Set<Student> uniqueStudents = new HashSet<>();

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        // Initialize the set with existing students from the database
        uniqueStudents.addAll(studentRepository.findAll());
    }

    public Student saveStudent(Student student) {
        // Check if student already exists in the set
        if (uniqueStudents.contains(student)) {
            throw new IllegalArgumentException("Student with this email already exists.");
        }

        // Add the student to the set and the database
        uniqueStudents.add(student);
        return studentRepository.save(student);
    }

    public Student findStudentByEmail(String emailAddress) {
        // Use stream to search for student by email
        return uniqueStudents.stream()
                .filter(student -> student.getEmailAddress().equals(emailAddress))
                .findFirst()
                .orElse(null);
    }
}

