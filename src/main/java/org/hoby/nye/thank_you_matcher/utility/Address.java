/**
 * 
 */
package org.hoby.nye.thank_you_matcher.utility;

/**
 * @author Tim
 *
 */
public class Address implements Comparable< Address > {
	private String line1;
	private String line2;
	private String city;
	private String state;
	private ZipCode zip;

	/**
	 * 
	 * @param line1
	 * @param line2
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Address( String line1, String line2, String city, String state, ZipCode zip ) {
		this.line1 = line1;
		this.line2 = line2;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	/**
	 * 
	 * @param line1
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Address( String line1, String city, String state, ZipCode zip ) {
		this( line1, "", city, state, zip );
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo( Address arg0 ) {
		if( this.equals(arg0) ) {
			return 0;
		}
		
		return zip.compareTo(arg0.zip);
	}

    /**
	 * @return the line1
	 */
	public String getLine1() {
		return line1;
	}

	/**
	 * @return the line2
	 */
	public String getLine2() {
		return line2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the zip
	 */
	public ZipCode getZip() {
		return zip;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (line1 != null ? !line1.equals(address.line1) : address.line1 != null) return false;
        if (line2 != null ? !line2.equals(address.line2) : address.line2 != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (state != null ? !state.equals(address.state) : address.state != null) return false;

        return zip != null ? zip.equals(address.zip) : address.zip == null;
    }

    @Override
    public int hashCode() {
        int result = line1 != null ? line1.hashCode() : 0;
        result = 31 * result + (line2 != null ? line2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        return result;
    }
}
