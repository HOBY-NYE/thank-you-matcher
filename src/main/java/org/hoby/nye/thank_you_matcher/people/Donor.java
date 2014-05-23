/**
 * 
 */
package org.hoby.nye.thank_you_matcher.people;

import org.hoby.nye.thank_you_matcher.utility.Address;

/**
 * @author Tim
 *
 */
public class Donor extends Recipient implements Person {
	private String first;
	private String last;
	private String org;
	private String donorType;
	private String donation;

	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param orgName
	 * @param donorType
	 * @param address
	 */
	public Donor( String firstName, String lastName, String orgName, String donorType, String donation, Address address ) {
		super( address );
		
		first = firstName;
		last = lastName;
		org = orgName;
		this.donorType = donorType;
		this.donation = donation;
	}
	
	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param donorType
	 * @param address
	 */
	public Donor( String firstName, String lastName, String donorType, String donation, Address address ) {
		this( firstName, lastName, "", donorType, donation, address );
	}

	/*
	 * (non-Javadoc)
	 * @see org.hoby.nye.thank_you_matcher.people.Person#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return first;
	}

	/*
	 * (non-Javadoc)
	 * @see org.hoby.nye.thank_you_matcher.people.Person#getLastName()
	 */
	@Override
	public String getLastName() {
		return last;
	}

	/**
	 * @return the org
	 */
	public String getOrg() {
		return org;
	}

	/**
	 * @return the type
	 */
	public String getDonorType() {
		return donorType;
	}
	
	/**
	 * 
	 * @return the donation
	 */
	public String getDonation() {
		return donation;
	}
	
}
