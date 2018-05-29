/**
 * 
 */
package org.hoby.nye.thank_you_matcher.people;

import org.hoby.nye.thank_you_matcher.utility.Address;

/**
 * @author Tim
 *
 */
public class Speaker extends Recipient implements Person {
	private String title;
	private String firstName;
	private String lastName;
	private String role;

	/**
	 * @param address
	 */
	public Speaker(String title, String first, String last, String role, Address address ) {
		super(address);
		
		this.title = title;
		this.firstName = first;
		this.lastName = last;
		this.role = role;
	}
	
	/**
	 * 
	 */
	public String getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see org.hoby.nye.thank_you_matcher.people.Person#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/* (non-Javadoc)
	 * @see org.hoby.nye.thank_you_matcher.people.Person#getLastName()
	 */
	@Override
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * 
	 */
	public String getRole() {
		return role;
	}

}
