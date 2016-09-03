
package org.ldlabs.spring.test.rest.response;

import java.util.Collection;
import java.util.List;

import org.ldlabs.spring.test.model.Student;

public class FindResponseBody
{

	private Integer	totalCount;

	private Collection<Student>	data;

	private List<String> errorMessages;
	
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
	 * @param totalCount
	 *            the totalCount to set
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
	 * @param data
	 *            the data to set
	 */
	public void setData(Collection<Student> data)
	{
		this.data = data;
	}

	/**
	 * @return the errorMessages
	 */
	public List<String> getErrorMessages()
	{
		return errorMessages;
	}

	/**
	 * @param errorMessages the errorMessages to set
	 */
	public void setErrorMessages(List<String> errorMessages)
	{
		this.errorMessages = errorMessages;
	}

}
