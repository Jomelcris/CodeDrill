package com.csit321g4.dreamteam.codedrill.repository;

import com.csit321g4.dreamteam.codedrill.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
