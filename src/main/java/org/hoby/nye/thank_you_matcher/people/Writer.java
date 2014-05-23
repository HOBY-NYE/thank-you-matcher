/**
 * 
 */
package org.hoby.nye.thank_you_matcher.people;

import java.util.List;
import java.util.Vector;

/**
 * @author Tim
 *
 */
public abstract class Writer implements Comparable< Writer > {
	protected List< Recipient > recipients;
	
	/**
	 * 
	 */
	public Writer() {
		recipients = new Vector<>();
	}
	
	/**
	 * Adds a new {@link Recipient} to the list of recipients this {@linkplain Writer} must produce a letter for.
	 * 
	 * @param recipient
	 */
	public void addRecipient( Recipient recipient ) {
		
	}
	
	/**
	 * Provides a {@link List} of recipients that this {@linkplain Writer} must produce a letter for.
	 * 
	 * @return the {@linkplain List} of recipients
	 */
	public List< Recipient > getRecipientList() {
		return recipients;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo( Writer o )  {
		if( this.equals(o) ) { 
			return 0;
		}
		
		if( recipients.size() < o.recipients.size() ) {
			return -1;
		}
		
		if( recipients.size() > o.recipients.size() ) {
			return 1;
		}
		
		return 0;
	}

}
