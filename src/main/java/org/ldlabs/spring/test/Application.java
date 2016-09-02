package org.ldlabs.spring.test;


import java.util.ArrayList;

import org.ldlabs.spring.test.model.Address;
import org.ldlabs.spring.test.model.Course;
import org.ldlabs.spring.test.model.Education;
import org.ldlabs.spring.test.model.Student;
import org.ldlabs.spring.test.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private StudentRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		Student student = new Student("Pippo");
		Address address = new Address("Via Prova", 1, "Italy");
		student.setAddresses(new ArrayList<Address>());
		student.getAddresses().add(address);
		student.setEducation(new Education());
		student.getEducation().setMaster(new Course("Universita' di Bologna", 2010));
		student.getEducation().setPhd(new Course("Universita' di Bologna", 2013));
		repository.save(student);
		
	}

}