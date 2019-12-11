Employee REST server
---

Create a web application that exposes REST operations for employees. The API should be able to:

- Get employees by an ID
- Create new employees
- Update existing employees
- Delete employees
- Get all employees

An employee is made up of the following data:

__Employee spec__:
> ID - Unique identifier for an employee
> FirstName - Employees first name
> MiddleInitial - Employees middle initial
> LastName - Employeed last name
> DateOfBirth - Employee birthday and year
> DateOfEmployment - Employee start date
> Status - ACTIVE or INACTIVE


### Startup

On startup, the application should be able to ingest an external source of employees, and should make them available via the GET endpoint.


### ACTIVE vs INACTIVE employees

By default, all employees are active, but by way of the API, can be switched to inactive. This should be done by the delete API call. This call should require some manner of authorization header.
When an employee is inactive, they should no longer be able to be retrieved in either the get by id, or get all employees calls
 

All data should be persisted into either memory, or externally. Please include instructions on how to run and interact with the web server.
