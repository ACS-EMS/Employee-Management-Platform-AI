package com.ems.repository;

import com.ems.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    List<User> findByRole(String role);

    List<User> findByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String userName,
            String email
    );

    List<User> findByRoleIgnoreCase(String role);

    List<User> findByActive(Boolean active);
}