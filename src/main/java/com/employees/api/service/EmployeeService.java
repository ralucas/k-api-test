package com.employees.api.service;

import com.employees.api.model.Employee;
import com.employees.api.model.Status;
import com.employees.api.repository.EmployeeRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@Service("employeeService")
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Collection<Employee> list(int pageNum, int perPage) {
        List<Order> so = new ArrayList<Order>();
        so.add(new Order(Direction.DESC, "status"));
        so.add(new Order(Direction.DESC, "lastName"));
        PageRequest p = PageRequest.of(pageNum, perPage, Sort.by(so));
        Iterable<Employee> employees = employeeRepository.findAll(p);
        return StreamSupport.stream(employees.spliterator(), true)
            .filter(e -> e.getStatus().equals(Status.ACTIVE))
            .collect(Collectors.toList());
    }

    public Employee getById(UUID id) {
        Optional<Employee> e = employeeRepository.findById(id);
        if (e.isEmpty() || e.get().getStatus().equals(Status.INACTIVE)) {
            return null;
        }
        return e.get();
    }

    public Employee update(UUID id, Employee e) {
        Optional<Employee> oe = employeeRepository.findById(id);
        if (oe.isEmpty()) {
            return null;
        } 
        Employee employee = oe.get();
        employee.setDateOfEmployment(e.getDateOfBirth());
        employee.setStatus(e.getStatus());
        employee.setDateOfEmployment(e.getDateOfEmployment());
        employee.setFirstName(e.getFirstName());
        employee.setLastName(e.getLastName());
        employee.setMiddleInitial(e.getMiddleInitial());
        return employeeRepository.save(e);
    }

    public boolean inactivate(UUID id) throws Exception {
        Optional<Employee> oe = employeeRepository.findById(id);
        if (oe.isEmpty()) {
            return false;
        }
        Employee e = oe.get();
        e.setStatus(Status.fromString("INACTIVE"));
        employeeRepository.save(e);
        return true;
    }
}