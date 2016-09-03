package org.ldlabs.spring.test.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Address
{
	
	@NotNull
	@Size(min=1)
	private String street;
	
	@NotNull
	@Min(1)
	private Integer number;
	
	@NotNull
	@Size(min=1)
	private String country;
	
	public Address()
	{
		super();
	}
	
	public Address(String street, Integer number, String country)
	{
		super();
		this.setStreet(street);
		this.setNumber(number);
		this.setCountry(country);
	}

	/**
	 * @return the street
	 */
	public String getStreet()
	{
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street)
	{
		this.street = street;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber()
	{
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number)
	{
		this.number = number;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}
	
	@Override
    public String toString() {
        return String.format(
                "Address[street='%s', no='%s', country='%s']",
                street, number, country);
    }
	
}
