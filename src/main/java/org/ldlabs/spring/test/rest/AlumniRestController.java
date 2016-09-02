package org.ldlabs.spring.test.rest;

import java.util.List;

import org.ldlabs.spring.test.model.Student;
import org.ldlabs.spring.test.repository.StudentRepository;
import org.ldlabs.spring.test.rest.response.FindResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AlumniRestController {

	@Autowired
	private StudentRepository repository;
	
	@Autowired 
	private MongoTemplate mongoTemplate;
	
	@RequestMapping(value="/ex-1/alumni", method=RequestMethod.GET, produces={"application/json"})
    public ResponseEntity<FindResponseBody> find(@RequestParam(value="name", required=false) String name, 
    		@RequestParam(value="education", required=false) String education,
    		@RequestParam(value="page", defaultValue = "0") Integer page, 
    		@RequestParam(value="limit", defaultValue = "100") Integer limit) {
		
		Query query = new Query();
		query.with(new PageRequest(page, limit));
		
		if (name != null)
		{
			query.addCriteria(Criteria.where("name").is(name));
		}
		
		if (education != null)
		{
			if ("phd".equals(education))
			{
				query.addCriteria(Criteria.where("education.phd").exists(true));
			}
			else if ("master".equals(education))
			{
				query.addCriteria(Criteria.where("education.master").exists(true));
			}
		}

		List<Student> found = mongoTemplate.find(query, Student.class);
		
//		if (education != null)
//		{
//			Education educationQuery = new Education();
//			if ("phd".equals(education))
//			{
//				educationQuery.setPhd(new Course());
//			} 
//			else if ("master".equals(education))
//			{
//				educationQuery.setMaster(new Course());
//			}
//		}
//		
//		Page<Student> found = repository.findByNameAndMasterEducation(name, new PageRequest(page, limit));
	
		if (found == null || found.isEmpty())
		{
			return new ResponseEntity<FindResponseBody>(HttpStatus.NO_CONTENT);
		}
		
		FindResponseBody response = new FindResponseBody();
		response.setData(found);
		response.setTotalCount(found.size());
		
		return new ResponseEntity<FindResponseBody>(response, HttpStatus.OK);
		
    }
	
	@RequestMapping(value="/ex-1/alumni", method=RequestMethod.POST, consumes={"application/json"})
    public ResponseEntity<?> insert(@RequestBody Student student) {
		
		Student saved = repository.save(student);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(saved.getId()).toUri());
		return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
		
    }
    
}