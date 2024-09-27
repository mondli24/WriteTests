package com.mondlimqanya.WriteTests.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @NotEmpty(message = "First name is required")
    private String firstName;

    @Setter
    @Getter
    @NotEmpty(message = "Last name is required")
    private String lastName;

    @Setter
    @Getter
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String emailAddress;

    @Setter
    @Getter
    @NotEmpty(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String phone;

    @Setter
    @Getter
    private String password;

    // A student can enroll in multiple courses
    @ManyToMany(mappedBy = "students")
    private Set<Course> courses = new HashSet<>();

    // Constructors
    public Student() {}

    public Student(String firstName, String lastName, String emailAddress, String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.password = password;
    }
}
