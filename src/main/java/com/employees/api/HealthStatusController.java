package com.employees.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/_status")
public class HealthStatusController {
	@RequestMapping(
		method = RequestMethod.GET,
		produces = "application/json"
	)
	@ResponseStatus(code = HttpStatus.OK)
	@ResponseBody
	public String healthy() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode on = mapper.createObjectNode();
		on.put("message", "healthy");
		try {
			return mapper.writeValueAsString(on);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
			return "OK";
		}
	}

}
