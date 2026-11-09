package com.csit321g4.dreamteam.codedrill.service;

import com.csit321g4.dreamteam.codedrill.dto.LoginRequest;
import com.csit321g4.dreamteam.codedrill.dto.UserRequest;
import com.csit321g4.dreamteam.codedrill.dto.UserResponse;
import com.csit321g4.dreamteam.codedrill.model.User;
import com.csit321g4.dreamteam.codedrill.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private static final int MIN_PASSWORD_LENGTH = 6;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    @Transactional
    public UserResponse create(UserRequest request) {
        String email = normalizeEmail(request.email());
        checkPasswordLength(request.password());
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        User user = new User(
                request.fullName().trim(),
                email,
                passwordEncoder.encode(request.password()),
                request.program());
        return UserResponse.from(userRepository.save(user));
    }

    // READ (all)
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll(Sort.by("userId"))
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    // READ (one)
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return UserResponse.from(getOrThrow(id));
    }

    // UPDATE
    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = getOrThrow(id);
        String email = normalizeEmail(request.email());

        userRepository.findByEmail(email)
                .filter(other -> !other.getUserId().equals(id))
                .ifPresent(other -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
                });

        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setProgram(request.program());

        if (request.password() != null && !request.password().isBlank()) {
            checkPasswordLength(request.password());
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }
        return UserResponse.from(userRepository.save(user));
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    // LOGIN (used by the login page)
    @Transactional(readOnly = true)
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(normalizeEmail(request.email()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return UserResponse.from(user);
    }

    private User getOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id));
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private static void checkPasswordLength(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
    }
}
