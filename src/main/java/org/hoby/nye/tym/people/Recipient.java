package org.hoby.nye.tym.people;

import org.hoby.nye.tym.utility.Address;
import org.hoby.nye.tym.utility.ZipCode;

/**
 * @author Tim
 *
 */
public abstract class Recipient {
	protected Address address;
	protected int letterCount;

	/**
	 * 
	 * @param line1
	 * @param line2
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Recipient( String line1, String line2, String city, String state, String zip ) {
		ZipCode zipCode;

		if( zip.contains("-")) {
			String[] split = zip.split("-");
			zipCode = new ZipCode(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
		} else {
			zipCode = new ZipCode(Integer.valueOf(zip));
		}

		address = new Address( line1, line2, city, state, zipCode );
	}
	
	/**
	 * 
	 * @param line1
	 * @param line2
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Recipient( String line1, String line2, String city, String state, ZipCode zip ) {
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
		this( street, "", city, state, zip );
	}

    /**
     *
     * @param street
     * @param city
     * @param state
     * @param zip
     */
    public Recipient( String street, String city, String state, ZipCode zip ) {
        this( street, "", city, state, zip );
    }
	
	/**
	 * 
	 * @param address
	 */
	public Recipient( Address address ) {
		this.address = address;
	}
	
	/**
	 * Provides the mailing address.
	 * 
	 * @return the {@linkplain Address} of this {@linkplain Recipient}
	 */
	public Address getAddress() {
		return address;
		
	}

	/**
	 *
	 * @return the number of letters this receipient receives
	 */
	public int getLetterCount() {
		return letterCount;
	}
}
