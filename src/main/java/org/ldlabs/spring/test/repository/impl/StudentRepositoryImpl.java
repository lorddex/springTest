package org.ldlabs.spring.test.repository.impl;

import java.util.List;

import org.ldlabs.spring.test.model.Student;
import org.ldlabs.spring.test.repository.EducationValue;
import org.ldlabs.spring.test.repository.StudentCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Custom repository.
 * 
 * @author Francesco Apollonio
 *
 */
@Repository
public class StudentRepositoryImpl implements StudentCustomRepository
{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public StudentRepositoryImpl()
	{
		super();
	}
	
	@Override
	public List<Student> findStudentWithDegrees(String name, EducationValue education, Integer page, Integer limit)
	{
		
		Query query = new Query();
		query.with(new PageRequest(page, limit));

		if (name != null)
		{
			query.addCriteria(Criteria.where("name").regex(name, "i"));
		}

		if (education != null)
		{
			if (EducationValue.PHD.equals(education))
			{
				query.addCriteria(Criteria.where("education.phd").exists(true));
			}
			else if (EducationValue.MASTER.equals(education))
			{
				query.addCriteria(Criteria.where("education.master").exists(
						true));
			}
		}

		return mongoTemplate.find(query, Student.class);
		
	}

}
