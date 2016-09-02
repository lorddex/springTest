package org.ldlabs.spring.test.repository;

import java.util.List;

import org.ldlabs.spring.test.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

//@RepositoryRestResource(collectionResourceRel = "alumni", path = "alumni")
public interface StudentRepository extends MongoRepository<Student, String> {

    public List<Student> findByName(@Param("name") String firstName);
    
//    public List<Student> findByLastName(@Param("surname") String lastName);

}
