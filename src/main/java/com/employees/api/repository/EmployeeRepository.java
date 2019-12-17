package com.employees.api.repository;

import java.util.UUID;

import com.employees.api.model.Employee;

import org.springframework.data.repository.PagingAndSortingRepository;;
import org.springframework.stereotype.Repository;

@Repository("employeeRepository")
public interface EmployeeRepository
  extends PagingAndSortingRepository<Employee, UUID> {

	Employee findByLastName(String string);
}