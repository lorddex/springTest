package org.ldlabs.spring.test.rest;

import java.util.List;

import javax.validation.Valid;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Interfaccia rest.
 * 
 * @author Francesco Apollonio
 *
 */
@RestController
public class AlumniRestController {

	private static final String	REST_BASE	= "/ex-1/alumni";

	@Autowired
	private StudentRepository repository;
	
	@Autowired 
	private MongoTemplate mongoTemplate;

    /**
     * Restituisce una serie di elementi ricercati in base ai parametri. La richiesta puo' essere paginata.
     * 
     * @param name Il nome dello studente da trovare. La ricerca e' case insensitive.
     * @param education Puo' essere <b>phd</b> o <b>master</b>. Se specificato ricerca uno studente che abbia un master o il phd.
     * @param page Il numero di pagina da ottenere.
     * @param limit Il numero di elementi da ritornare nella pagina.
     * 
     * @return Un oggetto di tipo {@link FindResponseBody}.
     */
    @RequestMapping(value=REST_BASE, method=RequestMethod.GET, produces={"application/json"})
    public ResponseEntity<FindResponseBody> find(@RequestParam(value="name", required=false) String name, 
    		@RequestParam(value="education", required=false) String education,
    		@RequestParam(value="page", defaultValue = "0") Integer page, 
    		@RequestParam(value="limit", defaultValue = "100") Integer limit) {
		
		Query query = new Query();
		query.with(new PageRequest(page, limit));
		
		if (name != null)
		{
			query.addCriteria(Criteria.where("name").regex(name, "i"));
		}
		
		if (education != null)
		{
			if ("phd".equals(education.toLowerCase()))
			{
				query.addCriteria(Criteria.where("education.phd").exists(true));
			}
			else if ("master".equals(education.toLowerCase()))
			{
				query.addCriteria(Criteria.where("education.master").exists(true));
			}
		}

		List<Student> found = mongoTemplate.find(query, Student.class);
		
		if (found == null || found.isEmpty())
		{
			return new ResponseEntity<FindResponseBody>(HttpStatus.NO_CONTENT);
		}
		
		FindResponseBody response = new FindResponseBody();
		response.setData(found);
		response.setTotalCount(found.size());
		
		return new ResponseEntity<FindResponseBody>(response, HttpStatus.OK);
		
    }
    
    /**
     * Metodo per ottenere uno Studente a partire dal suo id.
     * 
     * @param studentId L'id stringa dello studente da ottenere.
     * 
     * @return Lo studente trovato a partire dal suo id, codice di errore 404 nel caso non ci sia uno studente con quel nome.
     */
    @RequestMapping(value=REST_BASE+"/{studentId}", method=RequestMethod.GET, produces={"application/json"})
    public ResponseEntity<Student> get(@PathVariable String studentId) {
		
    	ResponseEntity<Student> response = null;
		Student student = repository.findOne(studentId);
		
		if (student == null)
		{
			response = new ResponseEntity<Student>(null, null, HttpStatus.NOT_FOUND);
		}
		else
		{
			response = new ResponseEntity<Student>(student, HttpStatus.OK);
		}
		
		return response;
		
    }
	
    /**
     * Metodo per salvare un nuovo studente.
     * 
     * @param student Lo studente da salvare.
     * 
     * @return Il codice di ritorno e' 201 e nell'header e' presente un link alla location con cui ottenere l'oggetto salvato.
     */
	@RequestMapping(value=REST_BASE, method=RequestMethod.POST, consumes={"application/json"})
    public ResponseEntity<?> insert(@RequestBody @Valid Student student, BindingResult result) {
		
		if (!result.hasErrors())
		{
			Student saved = repository.save(student);
			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(ServletUriComponentsBuilder
					.fromCurrentRequest().path("/{id}")
					.buildAndExpand(saved.getId()).toUri());
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
    }
    
}