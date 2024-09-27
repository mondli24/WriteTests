package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.Student;
import com.mondlimqanya.WriteTests.repository.StudentRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        // Check if student already exists in the database
        if (studentRepository.findByEmailAddress(student.getEmailAddress()) != null) {
            throw new IllegalArgumentException("Student with this email already exists.");
        }

        // Hash the password before saving
        student.setPassword(BCrypt.hashpw(student.getPassword(), BCrypt.gensalt()));
        return studentRepository.save(student);
    }

    public Student findStudentByEmail(String emailAddress) {
        return studentRepository.findByEmailAddress(emailAddress);
    }

    public Student findById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    public void updateStudentPassword(Student student, String newPassword) {
        // Hash the new password before saving
        student.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        studentRepository.save(student);
    }
}
