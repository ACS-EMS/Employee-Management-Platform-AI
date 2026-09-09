package com.ems.service;

import com.ems.dto.RoleRequestDTO;
import com.ems.dto.RoleResponseDTO;
import com.ems.entity.Role;
import com.ems.mapper.RoleMapper;
import com.ems.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(
            RoleRepository roleRepository,
            RoleMapper roleMapper) {

        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    // Get all roles
    public List<RoleResponseDTO> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(role -> roleMapper.toResponseDTO(role, 0L))
                .toList();
    }

    // Get role by ID
    public RoleResponseDTO getRoleById(Long roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with ID: " + roleId)
                );

        return roleMapper.toResponseDTO(role, 0L);
    }

    // Create role
    public RoleResponseDTO createRole(RoleRequestDTO dto) {

        if (roleRepository.existsByRoleNameIgnoreCase(dto.getRoleName())) {
            throw new RuntimeException(
                    "Role already exists: " + dto.getRoleName()
            );
        }

        Role role = roleMapper.toEntity(dto);

        Role savedRole = roleRepository.save(role);

        return roleMapper.toResponseDTO(savedRole, 0L);
    }

    // Update role
    public RoleResponseDTO updateRole(
            Long roleId,
            RoleRequestDTO dto) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with ID: " + roleId)
                );

        if (!role.getRoleName().equalsIgnoreCase(dto.getRoleName())
                && roleRepository.existsByRoleNameIgnoreCase(dto.getRoleName())) {

            throw new RuntimeException(
                    "Role already exists: " + dto.getRoleName()
            );
        }

        roleMapper.updateEntity(role, dto);

        Role updatedRole = roleRepository.save(role);

        return roleMapper.toResponseDTO(updatedRole, 0L);
    }

    // Delete role
    public void deleteRole(Long roleId) {

        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException(
                    "Role not found with ID: " + roleId
            );
        }

        roleRepository.deleteById(roleId);
    }

    // Search roles
    public List<RoleResponseDTO> searchRoles(String keyword) {

        return roleRepository
                .findByRoleNameContainingIgnoreCase(keyword)
                .stream()
                .map(role -> roleMapper.toResponseDTO(role, 0L))
                .toList();
    }
}