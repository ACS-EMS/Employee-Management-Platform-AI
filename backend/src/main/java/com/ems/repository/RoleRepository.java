package com.ems.repository;

import com.ems.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleNameIgnoreCase(String roleName);

    List<Role> findByRoleNameContainingIgnoreCase(String roleName);

    boolean existsByRoleNameIgnoreCase(String roleName);
}