package org.ldlabs.spring.test.model;


public class Course
{

	private String university;
	
	private Integer year;
	
	public Course()
	{
		super();
	}
	
	
	public Course(String university, Integer year)
	{
		super();
		this.setUniversity(university);
		this.setYear(year);
	}

	/**
	 * @return the university
	 */
	public String getUniversity()
	{
		return university;
	}

	/**
	 * @param university the university to set
	 */
	public void setUniversity(String university)
	{
		this.university = university;
	}

	/**
	 * @return the year
	 */
	public Integer getYear()
	{
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year)
	{
		this.year = year;
	}
	
}
