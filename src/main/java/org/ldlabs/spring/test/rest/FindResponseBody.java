package org.ldlabs.spring.test.rest;

import java.util.Collection;

import org.ldlabs.spring.test.model.Student;


public class FindResponseBody
{

	private Integer totalCount;
	
	private Collection<Student> data;
	
	public FindResponseBody()
	{
		super();
	}

	/**
	 * @return the totalCount
	 */
	public Integer getTotalCount()
	{
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Integer totalCount)
	{
		this.totalCount = totalCount;
	}

	/**
	 * @return the data
	 */
	public Collection<Student> getData()
	{
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Collection<Student> data)
	{
		this.data = data;
	}
	
}
