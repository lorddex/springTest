
package org.ldlabs.spring.test.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.ldlabs.spring.test.model.Student;
import org.ldlabs.spring.test.repository.EducationValue;
import org.ldlabs.spring.test.repository.StudentCustomRepository;
import org.ldlabs.spring.test.repository.StudentRepository;
import org.ldlabs.spring.test.rest.response.FindResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
@RequestMapping(value = AlumniRestController.REST_BASE)
public class AlumniRestController
{

	/**
	 * The REST base path.
	 */
	public static final String	REST_BASE	= "/ex-1/v1.0/alumni";
	
	@Autowired
	private StudentRepository repository;

	/**
	 * Restituisce una serie di elementi ricercati
	 * in base ai parametri. La richiesta puo'
	 * essere paginata.
	 * Ex.
	 * 
	 * <pre>
	 * GET /ex-1/v1.0/alumni?name=pippo&amp;education=master&amp;page=0&amp;limit=1 HTTP/1.1
	 * Host: localhost:8080
	 * Cache-Control: no-cache
	 * </pre>
	 * 
	 * @param name
	 *            Il nome dello studente da
	 *            trovare. La ricerca e' case
	 *            insensitive.
	 * @param education
	 *            Puo' essere <b>phd</b> o
	 *            <b>master</b>. Se specificato
	 *            ricerca uno studente che abbia
	 *            un master o il phd.
	 * @param page
	 *            Il numero di pagina da ottenere.
	 * @param limit
	 *            Il numero di elementi da
	 *            ritornare nella pagina.
	 * 
	 * @return Un oggetto di tipo
	 *         {@link FindResponseBody}.
	 */
	@RequestMapping( method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<FindResponseBody> find(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "education", required = false) String education,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "100") Integer limit)
	{

		
		EducationValue educationValue = null;
		
		if (education != null && !education.isEmpty())
		{
			try
			{
				educationValue = Enum.valueOf(EducationValue.class, education.toUpperCase());
			}
			catch (IllegalArgumentException e)
			{
				FindResponseBody response = new FindResponseBody();
				response.setErrorMessages(new ArrayList<String>());
				response.getErrorMessages().add(e.getMessage());
				ResponseEntity<FindResponseBody> responseEntity = new ResponseEntity<FindResponseBody>(response, HttpStatus.BAD_REQUEST);
				return responseEntity;
			}
		}
		
		List<Student> found = repository.findStudentByNameAndEducationLevel(name, educationValue, page, limit);

		if (found == null || found.isEmpty()) { return new ResponseEntity<FindResponseBody>(
				HttpStatus.NO_CONTENT); }

		FindResponseBody response = new FindResponseBody();
		response.setData(found);
		response.setTotalCount(found.size());

		return new ResponseEntity<FindResponseBody>(response, HttpStatus.OK);

	}

	/**
	 * Metodo per ottenere uno Studente a partire
	 * dal suo id.
	 * 
	 * Ex:
	 * 
	 * <pre>
	 * GET /ex-1/v1.0/alumni/57caa7e97073e61b678c884c HTTP/1.1
	 * Host: localhost:8080
	 * Cache-Control: no-cache
	 * </pre>
	 * 
	 * @param studentId
	 *            L'id stringa dello studente da
	 *            ottenere.
	 * 
	 * @return Lo studente trovato a partire dal
	 *         suo id, codice di errore 404 nel
	 *         caso non ci sia uno studente con
	 *         quel nome.
	 */
	@RequestMapping(value = "/{studentId}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Student> get(@PathVariable String studentId)
	{

		Student student = repository.findOne(studentId);

		if (student == null)
		{
			return new ResponseEntity<Student>(null, null, HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<Student>(student, HttpStatus.OK);
		}

	}

	/**
	 * Metodo per salvare un nuovo studente. Il
	 * metodo effettua la validazione del dato in
	 * ingresso, seguendo le constraint di
	 * validazione
	 * specificate nel modello.
	 * 
	 * Unico metodo protetto con login:
	 * <ul>
	 * <li>User: admin</li>
	 * <li>Password: test</li>
	 * </ul>
	 * 
	 * Ex:
	 * 
	 * <pre>
	 * POST /ex-1/v1.0/alumni HTTP/1.1
	 * Host: localhost:8080
	 * Content-Type: application/json
	 * Authorization: Basic YWRtaW46dGVzdA==
	 * Cache-Control: no-cache
	 * 
	 * {
	 *     "name": "nome",
	 *     "addresses": [
	 *         { 
	 *             "street": "test", 
	 *             "number": 1, 
	 *             "country": "country"
	 *         },
	 *         { 
	 *             "street": "streetname2", 
	 *             "number": 33, 
	 *             "country": "country"
	 *         }
	 *     ],
	 *     "education": { 
	 *         "master": { 
	 *             "university": "Politecnico Milano", 
	 *             "year": 2004
	 *         },
	 *         "phd": { 
	 *             "university": "UCSD", 
	 *             "year": 2009
	 *         }
	 *     }
	 * }
	 * </pre>
	 * 
	 * @param student
	 *            Lo studente da salvare.
	 * 
	 * @return Il codice di ritorno e' 201 e
	 *         nell'header e' presente un link
	 *         alla location con cui ottenere
	 *         l'oggetto salvato.
	 */
	@RequestMapping( method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> insert(@RequestBody @Valid Student student,
			BindingResult result)
	{

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
			FindResponseBody response = new FindResponseBody();
			response.setErrorMessages(new ArrayList<String>());
			
			
			if (result.getAllErrors() != null)
			{
				
				for (ObjectError error: result.getAllErrors())
				{
					String errorMessage = "";
					errorMessage = errorMessage.concat(error.getCodes()[0].toString() + ": " + error.getDefaultMessage() + ".");
					response.getErrorMessages().add(errorMessage);
					
				}
			}
			
			return new ResponseEntity<>(response,
					HttpStatus.BAD_REQUEST);
		}
	}

}
