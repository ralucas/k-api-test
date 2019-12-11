package com.employees.api.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

@KeySpace("employees")
public class Employee {
    @Id
    private String id;

	private String firstName;
	private String middleInitial;
	private String lastName;
	private Date birthDate;
	private Date employmentDate;
	private Status status;

	public Employee(
        String id, 
        String firstName,
        String middleInitial,
        String lastName, 
        Date birthDate,
        Date employmentDate,
        Status status
    ) {
		this.id = id;
		this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName; 
        this.birthDate = birthDate;
        this.employmentDate = employmentDate;
        this.status = status;
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
    }

	public String getLastName() {
		return lastName;
    }

	public String getMiddleInitial() {
		return middleInitial;
    }

	public String getBirthDate() {
		return birthDate.toString();
    }

	public String getEmploymentDate() {
		return employmentDate.toString();
    }

    public Status getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String fn) {
        this.firstName = fn;
    }

    public void setLastName(String ln) {
        this.lastName = ln;
    }

    public void setMiddleInitial(String mi) {
        this.middleInitial = mi;
    }
    
    public void setBirthDate(Date bd) {
        this.birthDate = bd;
    }

    public void setEmploymentDate(Date ed) {
        this.employmentDate = ed;
    }

    public void setStatus(Status s) {
        this.status = s;
    }
}