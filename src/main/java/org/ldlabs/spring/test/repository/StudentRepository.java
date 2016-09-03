
package org.ldlabs.spring.test.repository;

import java.util.List;

import org.ldlabs.spring.test.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String>
{

	List<Student> findByName(String name);

}
