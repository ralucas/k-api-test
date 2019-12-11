package com.employees.api;

import com.employees.api.model.Employee;
import com.employees.api.service.EmployeeService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
	@Autowired
	EmployeeService es;

	@RequestMapping(
		method = RequestMethod.GET,
		produces = "application/json"
	)
	@ResponseBody
	public String listEmployees() {
		return es.list();
	}

	@RequestMapping(
		method = RequestMethod.POST,
		produces = "application/json"
	)
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createEmployee(@RequestBody Employee employee) {
		es.save(employee);
	}

	// @RequestMapping(
	// 	value = "/employees/{id}", 
	// 	method = RequestMethod.GET,
	// 	produces = "application/json"
	// )
	// @ResponseBody
	// public String getEmployeeById() {
	// }

	// @RequestMapping(
	// 	value = "/employees/{id}", 
	// 	method = RequestMethod.PUT,
	// 	produces = "application/json"
	// )
	// @ResponseBody
	// public String updateEmployee() {
	// }


	// @RequestMapping(
	// 	value = "/employees/{id}", 
	// 	method = RequestMethod.DELETE,
	// 	produces = "application/json"
	// )
	// @ResponseBody
	// public String inactivateEmployee() {
	// }
}
