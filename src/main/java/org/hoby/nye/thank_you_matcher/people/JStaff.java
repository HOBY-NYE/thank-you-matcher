/**
 * 
 */
package org.hoby.nye.thank_you_matcher.people;


/**
 * @author Tim
 *
 */
public class JStaff extends Student {
	private final String position = "J-Staff";

	/**
	 * @param first
	 * @param last
	 * @param section
	 * @param group
	 */
	public JStaff(String first, String last, String section, String group) {
		super(first, last, section, group);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPosition() {
		return String.format( "%s %s", super.getGroup(), position );
	}

}
