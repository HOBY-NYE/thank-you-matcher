/**
 * 
 */
package org.hoby.nye.thank_you_matcher.people;

import org.hoby.nye.thank_you_matcher.utility.Address;

/**
 * @author Tim
 *
 */
public class Staff extends Recipient implements Person {
	private String firstName;
	private String lastName;
	private String position;

	/**
	 * @param address
	 */
	public Staff(String first, String last, String position, Address address) {
		super(address);
		firstName = first;
		lastName = last;
		this.position = position;
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
	 * @return
	 */
	public String getPosition() {
		return position;
	}

}
