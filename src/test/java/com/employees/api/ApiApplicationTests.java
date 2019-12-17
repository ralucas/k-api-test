package com.employees.api;

import com.employees.api.model.Employee;
import com.employees.api.model.Status;
import com.employees.api.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class ApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private EmployeeRepository er;

	private String apiToken;
	
	private UUID zaphodId = UUID.randomUUID();

	@BeforeEach
	void init() throws AssertionError {
		// Initial employee for tests
		Employee empl = new Employee(
			zaphodId,
			"Zaphod",
			"X",
			"Beeblebrox",
			LocalDate.parse("1970-01-01"),
			LocalDate.parse("2019-01-01"),
			Status.fromString("ACTIVE")
		);
		er.save(empl);
		try {
			MvcResult result = mockMvc.perform(
				post("/oauth/token")
					.header("Authorization", "Basic dGVzdC1jbGllbnQ6dGVzdC1zZWNyZXQ=")
					.contentType("application/x-www-form-urlencoded")
					.content("scope=read&grant_type=client_credentials&client_id=test-client")
				)
				.andReturn();
			JsonNode json = mapper.readTree(result.getResponse().getContentAsString());
			apiToken = json.get("access_token").textValue();
		} catch (Exception ex) {
			throw new AssertionError("No access token");
		}
	}

	@Test
	void testHealthCheck() throws Exception {
		mockMvc.perform(get("/_status"))
			.andExpect(status().isOk());
	}

	@Test
	@Order(1)
	void testGetAllEmployees() throws Exception {

		MvcResult result = mockMvc.perform(
			get("/api/employees")
				.header("Authorization", "Bearer " + apiToken)
				.contentType("application/json")
			)
			.andExpect(status().isOk())
			.andReturn();

		JsonNode json = mapper.readTree(result.getResponse().getContentAsString());
		assertEquals(1, json.size());
	}


	@Test
	@Order(2)
	void testCreateNewEmployee() throws Exception {
		ObjectNode newEmpl = mapper.createObjectNode();
		newEmpl.put("firstName", "Bilbo");
		newEmpl.put("middleInitial", "K");
		newEmpl.put("lastName", "Baggins");
		newEmpl.put("dateOfBirth", "1980-02-01");
		newEmpl.put("dateOfEmployment", "2018-05-01");
		newEmpl.put("status", "ACTIVE");
		
		mockMvc.perform(
			post("/api/employees")
				.header("Authorization", "Bearer " + apiToken)
				.contentType("application/json")
				.content(newEmpl.toString())
			)
			.andExpect(status().isCreated());

		Employee bilbo = er.findByLastName("Baggins");
		assertEquals("Bilbo", bilbo.getFirstName());
	}

	@Test
	@Order(3)
	void testGetEmployeesById() throws Exception {
		
		MvcResult result = mockMvc.perform(
			get("/api/employees/" + zaphodId.toString())
				.header("Authorization", "Bearer " + apiToken)
				.contentType("application/json")
			)
			.andExpect(status().isOk())
			.andReturn();

		JsonNode json = mapper.readTree(result.getResponse().getContentAsString());
		assertEquals("Beeblebrox", json.get("lastName").textValue());
		assertEquals("Zaphod", json.get("firstName").textValue());
	}


	@Test
	@Order(4)
	void testUpdateEmployeeById() throws Exception {
		ObjectNode empl = mapper.createObjectNode();
		empl.put("firstName", "Mark");
		empl.put("middleInitial", "X");
		empl.put("lastName", "Beeblebrox");
		empl.put("dateOfBirth", "1970-01-01");
		empl.put("dateOfEmployment", "2019-01-01");
		empl.put("status", "ACTIVE");
		
		MvcResult result = mockMvc.perform(
			put("/api/employees/" + zaphodId.toString())
				.header("Authorization", "Bearer " + apiToken)
				.contentType("application/json")
				.content(empl.toString())
			)
			.andExpect(status().isOk())
			.andReturn();

		JsonNode json = mapper.readTree(result.getResponse().getContentAsString());
		assertEquals("Beeblebrox", json.get("lastName").textValue());
		assertEquals("Mark", json.get("firstName").textValue());
	}

	@Test
	@Order(5)
	void testDeleteEmployeeById() throws Exception {
		mockMvc.perform(
			delete("/api/employees/" + zaphodId.toString())
				.header("Authorization", "Bearer " + apiToken)
			)
			.andExpect(status().isNoContent())
			.andReturn();
	}

	@Test
	void testDeleteEmployeeByIdRequiresToken() throws Exception {
		mockMvc.perform(
			delete("/api/employees/" + zaphodId.toString())
			)
			.andExpect(status().isUnauthorized())
			.andReturn();
	}
}
