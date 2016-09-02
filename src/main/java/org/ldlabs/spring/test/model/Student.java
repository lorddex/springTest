package org.ldlabs.spring.test.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Student {

	@Id
	private String id;
	
    private String name;
    
	private List<Address> addresses;
    
	private Education education;
	
	public Student() {
    	super();
    }
	
	public Student(String name) {
    	super();
    	this.setName(name);
    }
	
	public String getId()
	{
		return this.id;
	}

	
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	public List<Address> getAddresses()
	{
		return this.addresses;
	}

	
	public void setAddresses(List<Address> addresses)
	{
		this.addresses = addresses;
	}
	
	/**
	 * @return the education
	 */
	public Education getEducation()
	{
		return education;
	}


	/**
	 * @param education the education to set
	 */
	public void setEducation(Education education)
	{
		this.education = education;
	}

	@Override
    public String toString() {
        return String.format(
                "Student[name='%s']",
                name);
    }
	
}