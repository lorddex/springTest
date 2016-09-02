package org.ldlabs.spring.test.repository;

import org.ldlabs.spring.test.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

//@RepositoryRestResource(collectionResourceRel = "alumni", path = "alumni")
public interface StudentRepository extends MongoRepository<Student, String> {

    public Page<Student> findByName(@Param("name") String firstName, Pageable pageable);
    
//    public List<Student> findByLastName(@Param("surname") String lastName);

}
