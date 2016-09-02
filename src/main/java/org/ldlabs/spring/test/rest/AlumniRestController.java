package org.ldlabs.spring.test.rest;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.ldlabs.spring.test.model.Student;
import org.ldlabs.spring.test.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping(value="/ex-1/alumni", method=RequestMethod.GET, produces={"application/json"})
    public ResponseEntity<FindResponseBody> find(@RequestParam(value="name", required=false) String name) {
		
		Collection<Student> found = null;
		
		if (name == null)
		{
			found = repository.findAll();
		}
		else
		{
			found = repository.findByName(name);
		}
		
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