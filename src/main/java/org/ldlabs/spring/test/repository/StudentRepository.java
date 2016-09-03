
package org.ldlabs.spring.test.repository;

import org.ldlabs.spring.test.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String>
{


}
