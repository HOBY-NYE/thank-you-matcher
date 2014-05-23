/**
 * 
 */
package org.hoby.nye.thank_you_matcher;

/**
 * @author Tim
 *
 */
public class ThankYouMatcher {

	/**
	 * 
	 */
	public ThankYouMatcher() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if( args.length < 6 ) {
			System.err.println( "Not enough arguments provided!" );
			System.err.println( "Usage: java -jar thankyou.jar --donorFile <donor.xls> --staffFile <staff.xls> --studentFile <student.xls> [--outputFile <output.xls>]" );
			System.exit( -1 );
		}
		
		String donorFile = null;
		String staffFile = null;
		String studentFile = null;
		String outputFile = "output.xlsx";
		
		for( int i = 0; i < args.length; i += 2 ) {
			if( args[i].matches( "--staffFile" ) ) {
				staffFile =  args[i+1];
				continue;
			}
			
			if( args[i].matches( "--studentFile" ) ) {
				studentFile = args[i+1];
				continue;
			}
			
			if( args[i].matches( "--donorFile" ) ) {
				donorFile = args[i+1];
				continue;
			}
			
			if( args[i].matches( "--outputFile" ) ) {
				outputFile = args[i+1];
				continue;
			}
			
		}
		
		if( staffFile == null || studentFile == null || donorFile == null || outputFile == null ) {
			System.err.println( "Required argument missing!" );
			System.err.println( "Usage: java -jar thankyou.jar --donorFile <donor.xls> --staffFile <staff.xls> --studentFile <student.xls> [--outputFile <output.xls>]" );
			System.exit( -1 );
		}
	}

}
