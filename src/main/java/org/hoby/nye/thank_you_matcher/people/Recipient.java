package org.hoby.nye.thank_you_matcher.people;

import org.hoby.nye.thank_you_matcher.utility.Address;
import org.hoby.nye.thank_you_matcher.utility.ZipCode;

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
	public Recipient( String line1, String line2, String city, String state, String zip, int letterCount ) {
		ZipCode zipCode;

		if( zip.contains("-")) {
			String[] split = zip.split("-");
			zipCode = new ZipCode(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
		} else {
			zipCode = new ZipCode(Integer.valueOf(zip));
		}

		address = new Address( line1, line2, city, state, zipCode );
		this.letterCount = letterCount;
	}
	
	/**
	 * 
	 * @param line1
	 * @param line2
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Recipient( String line1, String line2, String city, String state, ZipCode zip, int letterCount ) {
		address = new Address( line1, line2, city, state, zip );
		this.letterCount = letterCount;
	}
	
	/**
	 * 
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Recipient( String street, String city, String state, String zip, int letterCount ) {
		this(street, "", city, state, zip, letterCount);
	}

    /**
     *
     * @param street
     * @param city
     * @param state
     * @param zip
     */
    public Recipient( String street, String city, String state, ZipCode zip, int letterCount ) {
        this( street, "", city, state, zip, letterCount );
    }
	
	/**
	 * 
	 * @param address
	 */
	public Recipient( Address address, int letterCount ) {
		this.address = address;
		this.letterCount = letterCount;
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
