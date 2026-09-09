package com.ems.mapper;

import com.ems.dto.RoleRequestDTO;
import com.ems.dto.RoleResponseDTO;
import com.ems.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleRequestDTO dto) {

        Role role = new Role();

        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());

        if (dto.getActive() == null) {
            role.setActive(true);
        } else {
            role.setActive(dto.getActive());
        }

        return role;
    }

    public void updateEntity(Role role, RoleRequestDTO dto) {

        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());

        if (dto.getActive() != null) {
            role.setActive(dto.getActive());
        }
    }

    public RoleResponseDTO toResponseDTO(
            Role role,
            Long assignedUsers) {

        return new RoleResponseDTO(
                role.getRoleId(),
                role.getRoleName(),
                role.getDescription(),
                role.getActive(),
                assignedUsers
        );
    }
}