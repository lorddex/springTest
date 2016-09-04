package org.ldlabs.spring.test.repository;

import java.util.List;

import org.ldlabs.spring.test.model.Student;


public interface StudentCustomRepository
{

	List<Student> findStudentByNameAndEducationLevel(String name, EducationValue education, Integer page, Integer limit);
	
}
