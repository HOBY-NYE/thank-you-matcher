/**
 * 
 */
package org.hoby.nye.thank_you_matcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hoby.nye.thank_you_matcher.people.Donor;
import org.hoby.nye.thank_you_matcher.people.Recipient;
import org.hoby.nye.thank_you_matcher.people.Student;
import org.hoby.nye.thank_you_matcher.people.Writer;
import org.hoby.nye.thank_you_matcher.utility.Address;

/**
 * @author Tim
 *
 */
public class ThankYouMatcher {
	private Workbook donors;
	private Workbook staff;
	private Workbook students;

	/**
	 * 
	 */
	public ThankYouMatcher() {
		donors = null;
		staff = null;
		students = null;
	}
	
	/**
	 * 
	 * @param donorFile
	 * @param staffFile
	 * @param studentFile
	 */
	public void loadFiles( String donorFile, String staffFile, String studentFile ) {
		try( FileInputStream donorFis = new FileInputStream( donorFile ); FileInputStream staffFis = new FileInputStream( staffFile ); FileInputStream studentFis = new FileInputStream( studentFile ) ) {
			donors = WorkbookFactory.create( donorFis );
			staff = WorkbookFactory.create( staffFis );
			students = WorkbookFactory.create( studentFis );
		} catch (FileNotFoundException fnfe) {
			System.err.println( "Couldn't find Excel file!");
			fnfe.printStackTrace();
			System.exit( -1 );
		} catch (IOException ioe) {
			
		} catch (InvalidFormatException ife) {
			
		}
	}
	
	/**
	 * 
	 */
	public Queue< Writer > createMatchUps() {		
		// Create a priority queue that uses the count of letters to 
		PriorityQueue< Writer > studentQueue = new PriorityQueue<>( getStudentInfo() );
		
		/* TODO Perform first pass
		 * Read donor workbook pulling out information such name, organization, address, donor type, what was donated, and benefactor ( if one exists )
		 * If there is a benefactor, add this donor to their list and update the count of letters that student needs to write 
		 * If there is not a benefactor, add this donor to the list of donor that need letters
		 */
		Vector< Recipient > donorList = getDonorInfo();
		
		/* TODO Perform second pass
		 * For each donor that hasn't already been assigned a student; poll the priority queue, add this donor to their list, update the count, offer the priority queue
		 * For each speaker/panelist/judge; perform the same task
		 * For each staff member; perform the same task
		 */
		return studentQueue;
	}
	
	/**
	 * 
	 */
	public void writeMatches() {
		/* TODO Write Excel spreadsheet with match information
		 * Each row will be student information ( first, last, section, group ) followed by recipient information ( organization, first, last, address, donation/role/posistion )
		 */
	}
	
	/**
	 * 
	 */
	private Vector< Writer > getStudentInfo() {
		Vector< Writer > studentList = new Vector<>();
		
		// Get the sheet that contains the group assignments
		int assignmentIndex = students.getSheetIndex( "Group Assignments" );
		Sheet assignmentSheet = students.getSheetAt( assignmentIndex );
				
		// Get the column indexes for the information we need
		int firstIndex = -1;
		int lastIndex  = -1;
		int colorIndex = -1;
		int groupIndex = -1;
		
		Row header = assignmentSheet.getRow( 0 );
		int numCells = header.getPhysicalNumberOfCells();
		for( int i = 0; i < numCells; i++ ) {
			Cell c = header.getCell( i );
			if( c.getCellType() != Cell.CELL_TYPE_STRING ) {
				continue;
			}
			
			String value = c.getStringCellValue();
			if( value.matches( ".*First.*" ) ) {
				firstIndex = c.getColumnIndex();
			} else if( value.matches( ".*Last.*" ) ) {
				lastIndex = c.getColumnIndex();
			} else if( value.matches( ".*Group.*") ) {
				groupIndex = c.getColumnIndex();
			} else if( value.matches( ".*Color.*") ) {
				colorIndex = c.getColumnIndex();
			}
			
			if( firstIndex >= 0 && lastIndex >= 0 && groupIndex >= 0 && colorIndex >= 0 ) {
				break;
			}
		}
		
		int numRows = assignmentSheet.getPhysicalNumberOfRows();
		for( int i = 1; i < numRows; i++ ) {
			Row r = assignmentSheet.getRow( i );
			if(r == null ) {
				continue;
			}
			
			String first   = r.getCell( firstIndex ).getStringCellValue();
			String last    = r.getCell( lastIndex ).getStringCellValue();
			String section = r.getCell( colorIndex ).getStringCellValue();
			String group   = r.getCell( groupIndex ).getStringCellValue();
			
			Student s = new Student( first, last, section, group );
			studentList.add( s );
		}
		
		return studentList;
	}
	
	/**
	 * 
	 * @return
	 */
	private Vector< Recipient > getDonorInfo() {
		Vector< Recipient > donorList = new Vector<>();
		Sheet donorSheet = donors.getSheet( "Donations" );
		
		int firstIndex  = -1;
		int lastIndex   = -1;
		int orgIndex    = -1;
		int typeIndex   = -1;
		
		int streetIndex = -1;
		int cityIndex   = -1;
		int stateIndex  = -1;
		int zipIndex    = -1;
		
		int beneTypeIndex  = -1;
		int beneFirstIndex = -1;
		int beneLastIndex  = -1;
		
		Row header = donorSheet.getRow( 0 );
		int numCells = header.getPhysicalNumberOfCells();
		for( int i = 0; i < numCells; i++ ) {
			Cell c = header.getCell( i );
			if( c.getCellType() != Cell.CELL_TYPE_STRING ) {
				continue;
			}
			
			String value = c.getStringCellValue();
			if( value.matches( ".* Donor First.*" ) ) {
				firstIndex = c.getColumnIndex();
			} else if( value.matches( ".*Donor Last.*" ) ) {
				lastIndex = c.getColumnIndex();
			} else if( value.matches( ".*Donor Organization.*") ) {
				orgIndex = c.getColumnIndex();
			} else if( value.matches( ".*Donor Type.*") ) {
				typeIndex = c.getColumnIndex();
			} else if( value.matches( ".*Street.*" ) ) {
				streetIndex = c.getColumnIndex();
			} else if( value.matches( ".*City.*") ) {
				cityIndex = c.getColumnIndex();
			} else if( value.matches( ".*State.*") ) {
				stateIndex = c.getColumnIndex();
			} else if( value.matches( ".*Zip.*") ) {
				zipIndex = c.getColumnIndex();
			} else if( value.matches( ".*Beneficary First.*") ) {
				beneFirstIndex = c.getColumnIndex();
			} else if( value.matches( ".*Beneficary Last.*") ) {
				beneLastIndex = c.getColumnIndex();
			} else if( value.matches( ".*Beneficary Type.*") ) {
				beneTypeIndex = c.getColumnIndex();
			}
			
			if( firstIndex >= 0 && lastIndex >= 0 && orgIndex >= 0 && typeIndex >= 0) {
				if( streetIndex >= 0 && cityIndex >= 0 && stateIndex >= 0 && zipIndex >= 0 ) {
					if( beneTypeIndex >= 0 && beneFirstIndex >= 0 && beneLastIndex >=0 ) {
						break;
					}
				}
			}
		}
		
		int numRows = donorSheet.getPhysicalNumberOfRows();
		for( int i = 1; i < numRows; i++ ) {
			Row r = donorSheet.getRow( i );
			if( r == null ) {
				continue;
			}
			
			// Get the address of the donor
			String street = r.getCell( streetIndex ).getStringCellValue();
			String city   = r.getCell( cityIndex ).getStringCellValue();
			String state  = r.getCell( stateIndex ).getStringCellValue();
			Integer zip   = null;
			if( r.getCell( zipIndex ).getCellType() == Cell.CELL_TYPE_STRING ) {
				zip = Integer.parseInt( r.getCell( zipIndex ).getStringCellValue() );
			} else if( r.getCell( zipIndex ).getCellType() == Cell.CELL_TYPE_NUMERIC ) {
				zip = (int) r.getCell( zipIndex ).getNumericCellValue();
			}
			Address address = new Address( street, city, state, zip );
			
			// Pull the other donor information
			String first = r.getCell( firstIndex ).getStringCellValue();
			String last  = r.getCell( lastIndex ).getStringCellValue();
			String org   = r.getCell( orgIndex ).getStringCellValue();
			String type  = r.getCell( typeIndex ).getStringCellValue();
			Donor donor = new Donor( first, last, org, type, "", address );
			
			// If this donor sponsored a student or j-staff, add it to 
			String beneType = r.getCell( beneTypeIndex ).getStringCellValue();
			if( beneType.matches( "Ambassador" ) || beneType.matches( "Junior Staff" ) ) {
				
			}
		}
		
		return donorList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if( args.length < 6 ) {
			System.err.println( "Not enough arguments provided!");
			System.err.println( "Usage: java -jar thankyou.jar --donorFile <donor.xls> --staffFile <staff.xls> --studentFile <student.xls>");
			System.exit( -1 );
		}
		
		String donorFile = null;
		String staffFile = null;
		String studentFile = null;
		
		for( int i = 0; i < 6; i += 2 ) {
			if( args[i].matches("--staffFile") ) {
				staffFile =  args[i+1];
				continue;
			}
			
			if( args[i].matches("--studentFile") ) {
				studentFile = args[i+1];
				continue;
			}
			
			if( args[i].matches("--donorFile") ) {
				donorFile = args[i+1];
				continue;
			}
			
		}
		
		if( staffFile == null || studentFile == null || donorFile == null ) {
			System.err.println( "Required argument missing!");
			System.err.println( "Usage: java -jar thankyou.jar --donorFile <donor.xls> --staffFile <staff.xls> --studentFile <student.xls>");
			System.exit( -1 );
		}
		
		ThankYouMatcher tym = new ThankYouMatcher();
		tym.loadFiles(donorFile, staffFile, studentFile);
		tym.createMatchUps();
	}

}
