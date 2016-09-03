package org.ldlabs.spring.test.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ldlabs.spring.test.model.Address;
import org.ldlabs.spring.test.model.Course;
import org.ldlabs.spring.test.model.Education;
import org.ldlabs.spring.test.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StudentRepositoryTest
{

	private static final String	STUDENT_NAME = "Pippo";
	
	@Autowired
	private StudentRepository repository;
	
	@Before
	public void before()
	{
		repository.deleteAll();

		Student student1 = new Student(STUDENT_NAME);
		Address address1 = new Address("Via Prova", 1, "Italy");
		student1.setAddresses(new ArrayList<Address>());
		student1.getAddresses().add(address1);
		student1.setEducation(new Education());
		student1.getEducation().setMaster(
				new Course("Universita' di Bologna", 2010));
		student1.getEducation().setPhd(
				new Course("Universita' di Bologna", 2013));
		repository.save(student1);

	}
	
	@Test
	public void customRepositoryTest()
	{
		List<Student> found = repository.findByName(STUDENT_NAME);
		
		Assert.assertNotNull(found);
		Assert.assertEquals(1, found.size());
		Assert.assertEquals(STUDENT_NAME, found.iterator().next().getName());
		
	}
	
}
