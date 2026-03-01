package com.groupwork.booking_system.service;

import com.groupwork.booking_system.exception.ResourceNotFoundException;
import com.groupwork.booking_system.model.User;
import com.groupwork.booking_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User getUserById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public Optional<User> getUserByEmail(String email) {
        return repo.findByEmail(email);
    }

    public User createUser(User user) {
        return repo.save(user);
    }

    public User registerUser(String name, String email, String password, String role) {
        if (repo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : "USER");
        return repo.save(user);
    }
}