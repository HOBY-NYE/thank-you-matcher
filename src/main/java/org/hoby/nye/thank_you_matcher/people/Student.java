/**
 * 
 */
package org.hoby.nye.thank_you_matcher.people;

/**
 * @author Tim
 *
 */
public class Student extends Writer implements Person {
	private String firstName;
	private String lastName;
	private String section;
	private String group;

	/**
	 * 
	 * @param first
	 * @param last
	 */
	public Student( String first, String last, String section, String group ) {
		firstName = first;
		lastName = last;
		this.section = section;
		this.group = group;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.hoby.nye.thank_you_matcher.people.Person#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/*
	 * (non-Javadoc)
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
	public String getSection() {
		return section;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getGroup() {
		return group;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( firstName == null ) ? 0 : firstName.hashCode() );
		result = prime * result + ( ( lastName == null ) ? 0 : lastName.hashCode() );
		result = prime * result + ( ( section == null ) ? 0 : section.hashCode() );
		result = prime * result + ( ( group == null ) ? 0 : group.hashCode() );
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// Reference Equality
		if( this == obj ) {
			return true;
		}
		
		// Null and type checking
		if(obj == null || !( obj instanceof Student ) ) {
			return false;
		}
		
		// Field checks
		Student other = ( Student ) obj;
		
		if( firstName == null && other.firstName != null ) {
			return false;
		} else if( !firstName.equals( other.firstName ) ) {
			return false;
		}
		
		if( lastName == null && other.lastName != null ) {
			return false;
		} else if( !lastName.equals( other.lastName ) ) {
			return false;
		}
		
		if( section == null && other.section != null ) {
			return false;
		} else if( !section.equals( other.section ) ) {
			return false;
		}
		
		if( group == null && other.group != null ) {
			return false;
		} else if( !group.equals( other.group ) ) {
			return false;
		}
		
		return true;
	}

}
