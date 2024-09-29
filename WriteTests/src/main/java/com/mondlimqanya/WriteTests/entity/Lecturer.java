package com.mondlimqanya.WriteTests.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "lecturers")
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lecturerId;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String emailAddress;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Transient // Not stored in the database
    private String confirm_password;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();

    // One-to-many relationship with students
    @OneToMany(mappedBy = "lecturer")
    private List<Student> students;


    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test> tests = new ArrayList<>();
    // Constructors
    public Lecturer() {}

    public Lecturer(String firstName, String lastName, String emailAddress, String phone, String password, String confirm_password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    // Custom Methods
    public boolean passwordsMatch() {
        return this.password.equals(this.confirm_password);
    }

    // Getters and setters for other fields...
}

