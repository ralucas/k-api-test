package com.employees.api;

import com.employees.api.model.Employee;
import com.employees.api.service.EmployeeService;

import java.util.Collection;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {
	@Autowired
	EmployeeService es;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Collection<Employee>> listEmployees(
		@RequestParam(defaultValue = "0") Integer pageNum,
		@RequestParam(defaultValue = "100") Integer perPage 
	) {
		try {
			Collection<Employee> el = es.list(pageNum, perPage);
			return ResponseEntity.ok(el);
        } catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
	}

	@RequestMapping(
		method = RequestMethod.POST,
		consumes = "application/json",
		produces = "application/json"
	)
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
		try {
			Employee newEmployee = es.save(employee);
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@RequestMapping(
		value = "/{id}",
		method = RequestMethod.GET,
		produces = "application/json"
	)
	@ResponseBody
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id) {
        try {
			Employee e = es.getById(UUID.fromString(id));
			if (e == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
            return ResponseEntity.ok(e);
        } catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
	}

	@RequestMapping(
		value = "/{id}", 
		method = RequestMethod.PUT,
		consumes = "application/json",
		produces = "application/json"
	)
	@ResponseBody
	public ResponseEntity<Employee> updateEmployee(
		@PathVariable("id") String id, 
		@RequestBody Employee employee
	) {
		try {
			Employee updatedEmployee = es.update(UUID.fromString(id), employee);
			return ResponseEntity.ok(updatedEmployee);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@RequestMapping(
		value = "/{id}", 
		method = RequestMethod.DELETE,
		produces = "application/json"
	)
	public ResponseEntity<String> inactivateEmployee(@PathVariable("id") String id) {
		try {
			if (es.inactivate(UUID.fromString(id))) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
