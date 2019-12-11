package com.employees.api.repository;

import com.employees.api.model.Employee;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("employeeRepository")
public interface EmployeeRepository
  extends CrudRepository<Employee, String> {
}