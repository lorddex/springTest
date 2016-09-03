
package org.ldlabs.spring.test.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ldlabs.spring.test.model.Address;
import org.ldlabs.spring.test.model.Course;
import org.ldlabs.spring.test.model.Education;
import org.ldlabs.spring.test.model.Student;
import org.ldlabs.spring.test.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestTest
{

	private MediaType contentType	= new MediaType(
		MediaType.APPLICATION_JSON
				.getType(),
		MediaType.APPLICATION_JSON
				.getSubtype(),
		Charset.forName("utf8"));

	private MockMvc	mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private StudentRepository repository;

	private Student student1;

	private Student	student2;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters)
	{

		this.mappingJackson2HttpMessageConverter = Arrays
				.asList(converters)
				.stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny().get();

		Assert.assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	protected String json(Object o) throws IOException
	{
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o,
				MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

	@Before
	public void before()
	{
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext).build();
		repository.deleteAll();

		student1 = new Student("Pippo");
		Address address1 = new Address("Via Prova", 1, "Italy");
		student1.setAddresses(new ArrayList<Address>());
		student1.getAddresses().add(address1);
		student1.setEducation(new Education());
		student1.getEducation().setMaster(
				new Course("Universita' di Bologna", 2010));
		student1.getEducation().setPhd(
				new Course("Universita' di Bologna", 2013));
		repository.save(student1);

		student2 = new Student("Topolino");
		Address address2 = new Address("Via Prova", 1, "Italy");
		student2.setAddresses(new ArrayList<Address>());
		student2.getAddresses().add(address2);
		student2.setEducation(new Education());
		student2.getEducation().setMaster(
				new Course("Universita' di Bologna", 2010));
		repository.save(student2);

	}

	@Test
	public void findTestWithName()
	{

		try
		{
			mockMvc.perform(
					MockMvcRequestBuilders.get(
							AlumniRestController.REST_BASE + "?name="
									+ student1.getName()).content(
							json(student1)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.totalCount", is(1)))
					.andExpect(
							jsonPath("$.data[0].name", is(student1.getName())));
		}
		catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void findTestWithEducation()
	{

		try
		{
			mockMvc.perform(
					MockMvcRequestBuilders.get(AlumniRestController.REST_BASE
							+ "?education=phd"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.totalCount", is(1)))
					.andExpect(
							jsonPath("$.data[0].name", is(student1.getName())));

			mockMvc.perform(
					MockMvcRequestBuilders.get(AlumniRestController.REST_BASE
							+ "?education=master")).andExpect(status().isOk())
					.andExpect(jsonPath("$.totalCount", is(2)));

		}
		catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void findTestWithPagination()
	{

		try
		{

			mockMvc.perform(
					MockMvcRequestBuilders.get(AlumniRestController.REST_BASE))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.totalCount", is(2)));

			mockMvc.perform(
					MockMvcRequestBuilders.get(AlumniRestController.REST_BASE
							+ "?page=0&limit=1"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.totalCount", is(1)))
					.andExpect(
							jsonPath("$.data[0].name", is(student1.getName())));

			mockMvc.perform(
					MockMvcRequestBuilders.get(AlumniRestController.REST_BASE
							+ "?page=1&limit=1"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.totalCount", is(1)))
					.andExpect(
							jsonPath("$.data[0].name", is(student2.getName())));

		}
		catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void findTestByWrongName()
	{

		try
		{
			mockMvc.perform(
					MockMvcRequestBuilders.get(AlumniRestController.REST_BASE
							+ "?name=pluto")).andExpect(status().isNoContent());
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void getTest()
	{

		Student found = repository.findOne(Example.of(student1));

		try
		{

			mockMvc.perform(
					MockMvcRequestBuilders.get(AlumniRestController.REST_BASE
							+ "/" + found.getId())).andExpect(status().isOk())
					.andExpect(jsonPath("$.name", is(student1.getName())));

		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void getTestNotFound()
	{

		Student found = repository.findOne(Example.of(student1));

		try
		{

			mockMvc.perform(
					MockMvcRequestBuilders.get(AlumniRestController.REST_BASE
							+ "/notExistingId")).andExpect(
					status().isNotFound());

		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void saveTest()
	{

		Student newStudent = new Student("Paperino");
		Address address1 = new Address("Via Prova", 1, "Italy");
		newStudent.setAddresses(new ArrayList<Address>());
		newStudent.getAddresses().add(address1);
		newStudent.setEducation(new Education());
		newStudent.getEducation().setMaster(
				new Course("Universita' di Bologna", 2010));
		newStudent.getEducation().setPhd(
				new Course("Universita' di Bologna", 2013));

		String student = null;
		try
		{
			student = json(newStudent);
			this.mockMvc.perform(
					MockMvcRequestBuilders.post(AlumniRestController.REST_BASE)
							.contentType(contentType).content(student))
					.andExpect(status().isCreated());
		}
		catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void saveTestWithAddressValidationError()
	{

		Student newStudent = new Student("Paperino");
		Address address1 = new Address("", 1, "Italy");
		newStudent.setAddresses(new ArrayList<Address>());
		newStudent.getAddresses().add(address1);
		newStudent.setEducation(new Education());
		newStudent.getEducation().setMaster(
				new Course("Universita' di Bologna", 2010));
		newStudent.getEducation().setPhd(
				new Course("Universita' di Bologna", 2013));

		String student = null;
		try
		{
			student = json(newStudent);
			this.mockMvc.perform(
					MockMvcRequestBuilders.post(AlumniRestController.REST_BASE))
						.andExpect(status().is4xxClientError());
		}
		catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void saveTestWithNameError()
	{

		Student newStudent = new Student("Paperino1");
		Address address1 = new Address("Test", 1, "Italy");
		newStudent.setAddresses(new ArrayList<Address>());
		newStudent.getAddresses().add(address1);
		newStudent.setEducation(new Education());
		newStudent.getEducation().setMaster(
				new Course("Universita' di Bologna", 2010));
		newStudent.getEducation().setPhd(
				new Course("Universita' di Bologna", 2013));

		String student = null;
		try
		{
			student = json(newStudent);
			this.mockMvc.perform(
					MockMvcRequestBuilders.post(AlumniRestController.REST_BASE))
						.andExpect(status().is4xxClientError());
		}
		catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
		
	}

}
