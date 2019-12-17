package com.employees.api;

import com.employees.api.model.Employee;
import com.employees.api.model.Status;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.time.LocalDate;

import com.employees.api.service.EmployeeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    private static final Logger logger = LoggerFactory.getLogger(ApiApplication.class);

	@Autowired
	EmployeeService es;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Override
    public void run(ApplicationArguments args) throws Exception {
		boolean hasDataFile = args.containsOption("data-file");
		boolean generateData = args.containsOption("generate-data");
		
		if (hasDataFile) {
			List<String> values = args.getOptionValues("data-file");
			if (values != null) {
				handleInputDataFile(values.get(0));
			} else {
                logger.error("data-file option requires a path");
                System.exit(-1);
            }
		} else if (generateData) {
			List<String> values = args.getOptionValues("generate-data");
			if (values != null) {
				generateData(values.get(0));
			} else {
                logger.error("generate-data option requires an amount");
                System.exit(-1);
			}
        }
    }

    private void generateData(String numOfRecords) {
        try {
            int records = Integer.parseInt(numOfRecords);
            Path fnPath = Paths.get("names/firstnames.txt");
            int fnLen = (int) Files.lines(fnPath).count();
            Path lnPath = Paths.get("names/lastnames.txt");
            int lnLen = (int) Files.lines(lnPath).count();
            for (int i = 0; i < records; i++) {
                UUID id = UUID.randomUUID();
                String firstName = Files.lines(fnPath)
                    .skip(new Random().nextInt(fnLen)).findFirst().get();
                int mi = new Random().nextInt(26) + 65;
                String middleInitial = String.valueOf((char) mi);
                String lastName = Files.lines(lnPath)
                    .skip(new Random().nextInt(lnLen)).findFirst().get();
                LocalDate birthDate = LocalDate.of(
                    new Random().nextInt(2000) + 1950,
                    new Random().nextInt(11) + 1,
                    new Random().nextInt(27) + 1
                );
                LocalDate employmentDate = LocalDate.of(
                    new Random().nextInt(2019) + 2010,
                    new Random().nextInt(11) + 1,
                    new Random().nextInt(27) + 1
                );
                String[] s = new String[] {"ACTIVE", "INACTIVE"};
                Status status = Status.fromString(s[new Random().nextInt(1)]);
                Employee e = new Employee(id, firstName, middleInitial, lastName, birthDate, employmentDate, status);
                es.save(e);
            }
        } catch (NumberFormatException nfe) {
            logger.error("Error parsing generate arg {}", nfe, nfe);
            System.exit(-1);
        } catch (FileNotFoundException fnfe) {
            logger.error("Error file issues {}", fnfe, fnfe);
            System.exit(-1);
        } catch (Exception ex) {
            logger.error("Error generation exception {}", ex, ex);
            System.exit(-1);
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
				logger.error("Data file is malformed");
				System.exit(-1);
			}
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			logger.error("Bad path or missing data file");
			System.exit(-1);
		} catch(IOException ie) {
			ie.printStackTrace();
			logger.error("Bad data file");
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
        UUID id = null;
        String firstName = null;
        String middleInitial = null;
        String lastName = null;
        LocalDate birthDate = null;
        LocalDate employmentDate = null;
        Status status = null;

        for (int i = 0; i < columns.length; i++) {
            String item = line[i];
            switch(i) {
                case 0:
                    id = UUID.fromString(item);
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
                    birthDate = LocalDate.parse(item);
                case 5:
                    employmentDate = LocalDate.parse(item);
                    break;
                case 6:
                    status = Status.fromString(item);
                    if (status == null) {
                        logger.error("Bad status");
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