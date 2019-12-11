package com.employees.api.service;

import com.employees.api.model.Employee;
// import com.employees.api.model.Status;
import com.employees.api.repository.EmployeeRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("employeeService")
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @JsonAnyGetter
    public String list() {
        ObjectMapper mapper = new ObjectMapper();
        Iterable<Employee> e = employeeRepository.findAll();
        try {
            String json = mapper.writeValueAsString(e);
            return json;
        } catch (Exception ex) {
            return "";
        }
    }

    public Optional<Employee> get(String id) {
        return employeeRepository.findById(id);
    }

    public void update(Employee employee) {
        employeeRepository.save(employee);

    }

    // public boolean inactivate(String id) {
    //     Optional<Employee> employee = employeeRepository.findById(id);
    //     if (employee != null) {
    //         employee.setStatus = Status.fromString("INACTIVE");
    //         employeeRepository.save(employee);
    //         return true;
    //     }
    //     return false;
    // }

}