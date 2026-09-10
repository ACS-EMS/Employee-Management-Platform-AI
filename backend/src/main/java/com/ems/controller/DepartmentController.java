package com.ems.controller;

import com.ems.entity.Department;
import com.ems.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // Create department
    @PostMapping("/create")
    public ResponseEntity<Department> createDepartment(
            @RequestBody Department department) {

        Department createdDepartment =
                departmentService.createDepartment(department);

        return ResponseEntity.ok(createdDepartment);
    }

    // Get all departments
    @GetMapping("/all")
    public ResponseEntity<?> getAllDepartments() {
        return ResponseEntity.ok(
                departmentService.getAllDepartments()
        );
    }

    // Search departments
    @GetMapping("/search")
    public ResponseEntity<?> searchDepartments(
            @RequestParam(required = false) String name) {

        return ResponseEntity.ok(
                departmentService.searchDepartments(name)
        );
    }

    // Get department by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(
            @PathVariable Long id) {

        Department department =
                departmentService.getDepartmentById(id);

        if (department == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(department);
    }

    // Update department
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(
            @PathVariable Long id,
            @RequestBody Department department) {

        Department updatedDepartment =
                departmentService.updateDepartment(id, department);

        if (updatedDepartment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedDepartment);
    }

    // Delete department
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(
            @PathVariable Long id) {

        Department existingDepartment =
                departmentService.getDepartmentById(id);

        if (existingDepartment == null) {
            return ResponseEntity.notFound().build();
        }

        departmentService.deleteDepartment(id);

        return ResponseEntity.ok(
                "Department deleted successfully"
        );
    }
}