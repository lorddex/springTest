package org.ldlabs.spring.test.repository;

import org.ldlabs.spring.test.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

//@RepositoryRestResource(collectionResourceRel = "alumni", path = "alumni")
public interface StudentRepository extends MongoRepository<Student, String> {

//	@Query("{ $and: [ { 'name': ?0 }, { 'education': { 'master': { $exists: true } } } ] }")
//	@Query("{ 'name': ?0 }")
//	public Page<Student> findByNameAndMasterEducation(@Param("name") String name, Pageable pageable);
	
//	public Page<Student> findByEducation(@Param("education") Education education, Pageable pageable);
    

}
