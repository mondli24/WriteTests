package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.User;
import com.mondlimqanya.WriteTests.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    // HashMap to store user credentials (for demonstration purposes)
    private final Map<String, String> userCredentials = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        // Initialize the map with existing users from the database
        for (User user : userRepository.findAll()) {
            userCredentials.put(user.getEmailAddress(), user.getPassword());
        }
    }

    public User saveUser(User user) {
        // Save user in the repository and update the map
        User savedUser = userRepository.save(user);
        userCredentials.put(savedUser.getEmailAddress(), savedUser.getPassword());
        return savedUser;
    }

    public User findUserByEmail(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress);
    }

    public boolean authenticateUser(String emailAddress, String password) {
        // Verify credentials from the map
        String storedPassword = userCredentials.get(emailAddress);
        return storedPassword != null && storedPassword.equals(password);
    }
}

