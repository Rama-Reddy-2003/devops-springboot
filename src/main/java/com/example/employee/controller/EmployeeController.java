package com.example.employee.controller;

import com.example.employee.model.Employee;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final List<Employee> employees = new ArrayList<>();

    public EmployeeController() {
        employees.add(new Employee(1L, "Dwarmpudi Siavji", "DevOps"));
        employees.add(new Employee(2L, "Karthik Akshay", "Java"));
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employees;
    }

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        employee.setId((long) (employees.size() + 1));
        employees.add(employee);
        return employee;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Java Spring Boot Employee API is up and running!";
    }
}