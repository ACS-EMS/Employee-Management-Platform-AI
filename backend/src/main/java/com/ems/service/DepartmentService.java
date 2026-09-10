package com.ems.service;

import com.ems.entity.Department;
import com.ems.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Create a department
    public Department createDepartment(Department department) {

        if (department.getStatus() == null
                || department.getStatus().isBlank()) {
            department.setStatus("ACTIVE");
        }

        return departmentRepository.save(department);
    }

    // Get all departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get department by ID
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    // Update department
    public Department updateDepartment(
            Long id,
            Department updatedDepartment) {

        Department existingDepartment =
                departmentRepository.findById(id).orElse(null);

        if (existingDepartment == null) {
            return null;
        }

        existingDepartment.setName(updatedDepartment.getName());
        existingDepartment.setManager(updatedDepartment.getManager());
        existingDepartment.setStatus(updatedDepartment.getStatus());

        return departmentRepository.save(existingDepartment);
    }

    // Delete department
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    // Search departments by name
    public List<Department> searchDepartments(String name) {

        if (name != null && !name.isBlank()) {
            return departmentRepository
                    .findByNameContainingIgnoreCase(name);
        }

        return departmentRepository.findAll();
    }
}