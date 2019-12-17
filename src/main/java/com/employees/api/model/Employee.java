package com.employees.api.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.lang.StringBuilder;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

@KeySpace("employees")
public class Employee {
    @Id
    private UUID id;

	private String firstName;
	private String middleInitial;
	private String lastName;
	private LocalDate dateOfBirth;
	private LocalDate dateOfEmployment;
    private Status status;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  

	public Employee(
        UUID id, 
        String firstName,
        String middleInitial,
        String lastName, 
        LocalDate dateOfBirth,
        LocalDate dateOfEmployment,
        Status status
    ) {
		this.id = id;
		this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName; 
        this.dateOfBirth = dateOfBirth;
        this.dateOfEmployment = dateOfEmployment;
        this.status = status;
	}

	public UUID getId() {
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

	public LocalDate getDateOfBirth() {
        return dateOfBirth; 
    }

	public LocalDate getDateOfEmployment() {
        return dateOfEmployment; 
    }

    public Status getStatus() {
        return status;
    }

    public void setId(UUID id) {
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
    
    public void setDateOfBirth(LocalDate bd) {
        this.dateOfBirth = bd;
    }

    public void setDateOfEmployment(LocalDate ed) {
        this.dateOfEmployment = ed;
    }

    public void setStatus(Status s) {
        this.status = s;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Employee: [");
        s.append("id=" + this.getId().toString());
        s.append(" ");
        s.append("firstName=" + this.getFirstName());
        s.append(" ");
        s.append("middleInitial=" + this.getMiddleInitial());
        s.append(" ");
        s.append("lastName=" + this.getLastName());
        s.append(" ");
        s.append("dateOfBirth=" + this.getDateOfBirth());
        s.append(" ");
        s.append("dateOfEmployment=" + this.getDateOfEmployment());
        s.append(" ");
        s.append("status=" + this.getStatus().toString());
        s.append("]");
        return s.toString();
    }
}