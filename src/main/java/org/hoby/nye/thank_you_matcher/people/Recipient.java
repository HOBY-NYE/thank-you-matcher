/**
 * 
 */
package org.hoby.nye.thank_you_matcher.people;

import org.hoby.nye.thank_you_matcher.utility.Address;

/**
 * @author Tim
 *
 */
public abstract class Recipient {
	protected Address address;

	/**
	 * 
	 * @param line1
	 * @param line2
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Recipient( String line1, String line2, String city, String state, String zip ) {
		address = new Address( line1, line2, city, state, zip );
	}
	
	/**
	 * 
	 * @param line1
	 * @param line2
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Recipient( String line1, String line2, String city, String state, Integer zip ) {
		address = new Address( line1, line2, city, state, zip );
	}
	
	/**
	 * 
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Recipient( String street, String city, String state, String zip ) {
		address = new Address( street, city, state, zip );
	}
	
	/**
	 * 
	 * @param address
	 */
	public Recipient( Address address ) {
		this.address = address;
	}
	
	/**
	 * 
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Recipient( String street, String city, String state, Integer zip ) {
		address = new Address( street, city, state, zip );
	}
	
	/**
	 * Provides the mailing address.
	 * 
	 * @return the {@linkplain Address} of this {@linkplain Recipient}
	 */
	public Address getAddress() {
		return address;
		
	}

}
