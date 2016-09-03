
package org.ldlabs.spring.test.model;

public class Education
{

	private Course master;

	private Course phd;

	public Education()
	{
		super();
	}

	/**
	 * @return the master
	 */
	public Course getMaster()
	{
		return master;
	}

	/**
	 * @param master
	 *            the master to set
	 */
	public void setMaster(Course master)
	{
		this.master = master;
	}

	/**
	 * @return the phd
	 */
	public Course getPhd()
	{
		return phd;
	}

	/**
	 * @param phd
	 *            the phd to set
	 */
	public void setPhd(Course phd)
	{
		this.phd = phd;
	}

}
