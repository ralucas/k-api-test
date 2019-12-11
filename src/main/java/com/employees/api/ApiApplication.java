package com.employees.api;

import com.employees.api.model.Employee;
import com.employees.api.model.Status;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.employees.api.service.EmployeeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@SpringBootApplication
@EnableMapRepositories
public class ApiApplication implements ApplicationRunner {
    private static final String[] columns = {
        "ID", 
        "FirstName", 
        "MiddleInitial", 
        "LastName", 
        "DateOfBirth", 
        "DateOfEmployment", 
        "Status"
	};

	@Autowired
	EmployeeService es;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Override
    public void run(ApplicationArguments args) throws Exception {
		boolean containsOption = args.containsOption("data-file");
		
		if (containsOption) {
			List<String> values = args.getOptionValues("data-file");
			if (values != null) {
				handleInputDataFile(values.get(0));
			}
		}
    }

    private void handleInputDataFile(String dataFilePath) {
		String[] headers = new String[0];
		try {
			InputStream is = new FileInputStream(dataFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			if (br.ready()) {
				headers = br.readLine().split(",");
			}
			if (validateHeaders(headers)) {
				while (br.ready()) {
					String[] line = br.readLine().split(",");
					try {
						Employee em = lineToEmployee(line);
						es.save(em);
					} catch (Exception e) {
						//ignore
					}
				}
				br.close();
			} else {
				System.out.println("Data file is malformed");
				System.exit(-1);
			}
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.out.println("Bad path or missing data file");
			System.exit(-1);
		} catch(IOException ie) {
			ie.printStackTrace();
			System.out.println("Bad data file");
			System.exit(-1);
		}
    }

    private boolean validateHeaders(String[] headers) {
        if (columns.length != headers.length) {
            return false;
        }
        for (int i = 0; i < columns.length; i++) {
            if (!headers[i].equals(columns[i])) {
                return false;
            }
        }
        return true;
    }

    private Employee lineToEmployee(String[] line) throws Exception {
        String id = null;
        String firstName = null;
        String middleInitial = null;
        String lastName = null;
        Date birthDate = null;
        Date employmentDate = null;
        Status status = null;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < columns.length; i++) {
            String item = line[i];
            switch(i) {
                case 0:
                    id = item;
                    break;
                case 1:
                    firstName = item;
                    break;
                case 2:
                    middleInitial = item;
                    break;
                case 3:
                    lastName = item;
                    break;
                case 4:
                    try {
                        birthDate = format.parse(item);
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                        System.out.println("Error parsing birthdate");
                        throw new Exception("Bad birthdate");
                    }
                    break;
                case 5:
                    try {
                        employmentDate = format.parse(item);
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                        System.out.println("Error parsing employmentdate");
                        throw new Exception("Bad employment date");
                    }
                    break;
                case 6:
                    status = Status.fromString(item);
                    if (status == null) {
                        System.out.println("Bad status");
                        throw new Exception("Bad status");
                    }
                    break;
                default: 
                    throw new Exception("Bad line");
            }
        }

        return new Employee(id, firstName, middleInitial, lastName, birthDate, employmentDate, status);
    }
}