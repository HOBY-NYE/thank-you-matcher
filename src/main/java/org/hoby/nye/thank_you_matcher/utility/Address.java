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
	private Integer zip;

	/**
	 * 
	 * @param line1
	 * @param line2
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Address( String line1, String line2, String city, String state, Integer zip ) {
		this.line1 = line1;
		this.line2 = line2;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	/**
	 * 
	 * @param line1
	 * @param line2
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Address( String line1, String line2, String city, String state, String zip ) {
		this( line1, line2, city, state, Integer.valueOf( zip ) );
	}	
	
	/**
	 * 
	 * @param line1
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Address( String line1, String city, String state, Integer zip ) {
		this( line1, "", city, state, zip );
	}

	/**
	 * 
	 * @param line1
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Address( String line1, String city, String state, String zip ) {
		this( line1, "", city, state, Integer.valueOf( zip ) );
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
		
		if( zip < arg0.zip) {
			return -1;
		} else if( zip == arg0.zip ) {
			return 0;
		} else if( zip > arg0.zip ) {
			return 1;
		}
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 1;
		result = prime * result + ( ( city == null ) ? 0 : city.hashCode() );
		result = prime * result + ( ( line1 == null ) ? 0 : line1.hashCode() );
		result = prime * result + ( ( line2 == null ) ? 0 : line2.hashCode() );
		result = prime * result + ( ( state == null ) ? 0 : state.hashCode() );
		result = prime * result + ( ( zip == null ) ? 0 : zip.hashCode() );

		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj ) {
		// Reference equality
		if( this == obj ) {
			return true;
		}
		
		// Null reference and type check
		if( obj == null || !( obj instanceof Address ) ) {
			return false;
		}
		
		// Check the members for equality
		Address other = ( Address ) obj;
		if(city == null && other.city != null ) {
			return false;
		} else if( ! city.equals( other.city ) ) {
			return false;
		}
		
		if( line1 == null && other.line1 != null ) {
			return false;
		} else if( !line1.equals( other.line1 ) ) {
			return false;
		}
		
		if( line2 == null && other.line2 != null ) {
			return false;
		} else if( !line2.equals( other.line2 ) ) {
			return false;
		}
		
		if( state == null && other.state != null ) {
			return false;
		} else if( !state.equals( other.state ) ) {
			return false;
		}
		
		if( zip == null && other.zip != null ) {
			return false;
		} else if( !zip.equals( other.zip ) ) {
			return false;
		}
		
		return true;
	}

}
