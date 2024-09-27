package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.Lecturer;
import com.mondlimqanya.WriteTests.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;
import java.util.HashMap;
import java.util.Map;

@Service
public class LecturerService {
    public Lecturer findById(Long id) {
        return lecturerRepository.findById(id)
                .orElse(null); // Return null if not found or handle as needed
    }
    private final LecturerRepository lecturerRepository;

    // HashMap to store lecturer credentials (for demonstration purposes)
    private final Map<String, String> lecturerCredentials = new HashMap<>();

    @Autowired
    public LecturerService(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
        for (Lecturer lecturer : lecturerRepository.findAll()) {
            lecturerCredentials.put(lecturer.getEmailAddress(), lecturer.getPassword());
        }
    }

    public void saveLecturer(Lecturer lecturer) {
        // Save lecturer in the repository and update the map
        String hashedPassword = BCrypt.hashpw(lecturer.getPassword(), BCrypt.gensalt());
        lecturer.setPassword(hashedPassword);
        Lecturer savedLecturer = lecturerRepository.save(lecturer);
        lecturerCredentials.put(savedLecturer.getEmailAddress(), savedLecturer.getPassword());
    }

    public Lecturer findLecturerByEmail(String emailAddress) {
        return lecturerRepository.findByEmailAddress(emailAddress);
    }

    public boolean authenticateLecturer(String emailAddress, String password) {
        // Verify credentials from the map
        Lecturer lecturer = findLecturerByEmail(emailAddress);
        if (lecturer == null) {
            return false; // Lecturer not found
        }
        return BCrypt.checkpw(password, lecturer.getPassword());

    }
}

