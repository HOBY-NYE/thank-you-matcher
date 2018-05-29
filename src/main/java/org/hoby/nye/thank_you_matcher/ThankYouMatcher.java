package org.hoby.nye.thank_you_matcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hoby.nye.thank_you_matcher.people.Donor;
import org.hoby.nye.thank_you_matcher.people.JStaff;
import org.hoby.nye.thank_you_matcher.people.Recipient;
import org.hoby.nye.thank_you_matcher.people.Speaker;
import org.hoby.nye.thank_you_matcher.people.Staff;
import org.hoby.nye.thank_you_matcher.people.Student;
import org.hoby.nye.thank_you_matcher.people.Writer;
import org.hoby.nye.thank_you_matcher.utility.Address;
import org.hoby.nye.thank_you_matcher.utility.ZipCode;

/**
 * @author Tim
 *
 */
public class ThankYouMatcher {
	private Workbook donors;
	private Workbook staff;
	private Workbook students;
	
	private Queue< Writer > studentList;
	private Map< Recipient, String > failed;

	/**
	 * 
	 */
	public ThankYouMatcher() {
		donors = null;
		staff = null;
		students = null;
		
		studentList = new PriorityQueue<>();
		failed = new HashMap<>();
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
	public void createMatchUps() {		
		Vector < Recipient > donorList = new Vector<>();
		
		// Read the Student information and populate the priority queue
		getStudentInfo();
		
		// Read the Staff workbook, add all non-J-Staff to the donor list and add J-Staff to the student list
		//getStaffInfo( donorList );
		
		/*
		 * Read donor workbook pulling out information such name, organization, address, donor type, what was donated, and benefactor ( if one exists )
		 * If there is a benefactor, add this donor to their list and update the count of letters that student needs to write 
		 * If there is not a benefactor, add this donor to the list of donor that need letters
		 */
		getDonorInfo( donorList );

		int count = 0;
		int priorCount = 0;

		for( Iterator< Recipient > it = donorList.iterator(); it.hasNext(); ) {
			/*
			 *  Needed to add the JStaff back into the PriorityQueue
			 *  Very inefficient since as the pairings go on, more polls will be needed
			 *  to get the first Student. Should overload the compareTo method to return MAX_INT
			 */
			Vector< Writer > storage = new Vector<>();
			Recipient recipient = it.next();
			Writer student = null;
			boolean invalidMatch;
			do {
				student = studentList.poll();
				storage.add( student );
				invalidMatch = student instanceof JStaff;

				if(!invalidMatch)
                {
                    if (recipient instanceof Staff)
                    {
                        Staff staff = (Staff) recipient;
                        if (staff.getPosition().contains("Facilitator")
                                || staff.getPosition().contains("Junior Staff"))
                        {
                            String[] position = staff.getPosition().split(" ");
                            Student ambassador = (Student) student;
                            invalidMatch = (ambassador != null && !ambassador.getGroup().equals(position[0]));
                        }
                        else if (staff.getPosition().contains("Section Leader"))
                        {
                            String[] position = staff.getPosition().split(" ");
                            Student ambassador = (Student) student;
                            invalidMatch = (ambassador != null && !ambassador.getSection().equals(position[0]));
                        }
                    }
                }
			} while( invalidMatch );
			
			student.addRecipient( recipient );
			count++;
			if (count - priorCount != 1)
            {
                System.out.print("");
            }
            priorCount=count;
			studentList.addAll( storage );
		}
		
		/* 
		 * For each donor that hasn't already been assigned a student; poll the priority queue, add this donor to their list, update the count, offer the priority queue
		 * For each speaker/panelist/judge; perform the same task
		 * For each staff member; perform the same task
		 */
		writeMatches();
	}
	
	/**
	 * 
	 */
	public void writeMatches() {
		/* Write Excel spreadsheet with match information
		 * Each row will be student information ( first, last, section, group ) followed by recipient information ( organization, first, last, address, donation/role/posistion )
		 */
		// Create a new Workbook
		File xlsx = new File( "Thank-You-Matches.xlsx" );
		if( !xlsx.exists() ) {
			try {
				xlsx.createNewFile();
  			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Workbook wb = new XSSFWorkbook();
		
		// Create a sheet for the match ups
		Sheet matchSheet = wb.createSheet( "Matches" );
		
		// Create the header row and populate it
		Row header = matchSheet.createRow( 0 );
		String[] headers = { "Student Name", "Student Section", "Student Group", "Donor Organization", "Donor Name", "Donor Address", "Donation", "Donation Information" };
		for( int i = 0; i < headers.length; i++ ) {
			Cell cell = header.createCell( i );
			cell.setCellValue( headers[i] );
		}
		
		// Create and populate a row for each match up
		int rownum = 0;
		for( Iterator< Writer > it = studentList.iterator(); it.hasNext(); ) {
			// JStaff can be treated as a Student here because the needed information is stored in the Student super class of JStaff
            Student student = ( Student ) it.next();

			// Create and populate a row for each recipient
			for( Iterator< Recipient > it2 = student.getRecipientList().iterator(); it2.hasNext(); ) {
				Row r = matchSheet.createRow( ++rownum );
				r.createCell( 0 ).setCellValue( String.format( "%s %s", student.getFirstName(), student.getLastName() ) );
				r.createCell( 1 ).setCellValue( student.getSection() );
				r.createCell( 2 ).setCellValue( student.getGroup() );

				Recipient recip = it2.next();
				if( recip instanceof Donor) {
					Donor donor = ( Donor ) recip;
					r.createCell( 3 ).setCellValue( donor.getOrg() );
					r.createCell( 4 ).setCellValue( String.format( "%s %s", donor.getFirstName(), donor.getLastName() ) );
					Address address = donor.getAddress();
					if( address == null ) {
						r.createCell( 5 ).setCellValue( "" );
					} else {
						r.createCell( 5 ).setCellValue( String.format( "%s %n%s, %s %s", address.getLine1(), address.getCity(), address.getState(), address.getZip() ) );
					}
                    r.createCell( 6 ).setCellValue( donor.getDonorInfo() );
				} else if( recip instanceof Speaker ) {
					Speaker speaker = ( Speaker ) recip;
					r.createCell( 3 ).setCellValue( "" );
					r.createCell( 4 ).setCellValue( String.format( "%s %s", speaker.getFirstName(), speaker.getLastName() ) );
					Address address = speaker.getAddress();
					if( address == null ) {
						r.createCell( 5 ).setCellValue( "" );
					} else {
						r.createCell( 5 ).setCellValue( String.format( "%s %n%s, %s %s", address.getLine1(), address.getCity(), address.getState(), address.getZip() ) );
					}
					r.createCell( 6 ).setCellValue( speaker.getRole() );
				} else if (recip instanceof Staff) {
				    Staff staff = (Staff) recip;
                    r.createCell( 3 ).setCellValue( "" );
                    r.createCell( 4 ).setCellValue( String.format( "%s %s", staff.getFirstName(), staff.getLastName() ) );
                    Address address = staff.getAddress();
                    if( address == null ) {
                        r.createCell( 5 ).setCellValue( "" );
                    } else {
                        r.createCell( 5 ).setCellValue( String.format( "%s %n%s, %s %s", address.getLine1(), address.getCity(), address.getState(), address.getZip() ) );
                    }
                    r.createCell( 6 ).setCellValue( staff.getPosition() );
                }
			}

		}
		
		// Create a sheet for the failed match ups
		Sheet failSheet = wb.createSheet( "Failures" );
		
		header = failSheet.createRow( 0 );
		headers = new String[] { "Donor Name", "Donor Organization", "Reason" };
		for( int i = 0; i < headers.length; i++ ) {
			header.createCell( i ).setCellValue( headers[i] );
		}
		
		// Create and populate a row for each failed match up
		rownum = 0;
		for( Iterator< Recipient > it = failed.keySet().iterator(); it .hasNext(); ) {
			Donor donor = ( Donor ) it.next();
			String reason = failed.get( donor );
			
			Row r = failSheet.createRow( ++rownum );
			r.createCell( 0 ).setCellValue( String.format( "%s %s", donor.getFirstName(), donor.getLastName() ) );
			r.createCell( 1 ).setCellValue( donor.getOrg() );
			r.createCell( 2 ).setCellValue( reason );
		}
		
		try ( FileOutputStream fos = new FileOutputStream( xlsx ) ) {
			wb.write( fos );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private void getStudentInfo() {
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
			if( r == null ) {
				continue;
			}
			
			Cell fCell   = r.getCell( firstIndex );
			if( fCell == null ) { continue; }
			String first = ( fCell.getCellType() == Cell.CELL_TYPE_STRING || fCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? fCell.getStringCellValue() : "";
			
			Cell lCell  = r.getCell( lastIndex );
			if( lCell == null ) { continue; }
			String last = ( lCell.getCellType() == Cell.CELL_TYPE_STRING || lCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? lCell.getStringCellValue() : "";
			
			Cell sCell     = r.getCell( colorIndex );
			if( sCell == null ) { continue; }
			String section = ( sCell.getCellType() == Cell.CELL_TYPE_STRING || sCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? sCell.getStringCellValue() : "";
			
			Cell gCell   = r.getCell( groupIndex );
			if( gCell == null ) { continue; }
			String group = ( gCell.getCellType() == Cell.CELL_TYPE_STRING || gCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? gCell.getStringCellValue() : "";
			
			if( ( first == "" && last == "" ) || group.equalsIgnoreCase( "x" ) ) {
				continue;
			}
			
			Student s = new Student( first, last, section, group );
			studentList.add( s );
		}
		
	}

	/**
	 * 
	 * @return
	 */
	private void getDonorInfo( Vector< Recipient > donorList ) {
        String sheetName = null;
	    // The Staff are on a sheet called Staff
        for( int i = 0; i < donors.getNumberOfSheets(); i++ ) {
            if( donors.getSheetName( i ).matches( ".*Staff.*" ) ) {
                sheetName = donors.getSheetName( i );
                break;
            }
        }
        readStaff( donors.getSheet( sheetName ), donorList );

        // The sponsors and donors are on a sheet called Donations
		for( int i = 0; i < donors.getNumberOfSheets(); i++ ) {
			if( donors.getSheetName( i ).matches( ".*Donations.*" ) ) {
				sheetName = donors.getSheetName( i );
				break;
			}
		}
		readDonations( donors.getSheet( sheetName ), donorList );
		
		// The speakers, judges, panelists, etc are on a sheet called Speakers
		for( int i = 0; i < donors.getNumberOfSheets(); i++ ) {
			if( donors.getSheetName( i ).matches( ".*Speakers.*" ) ) {
				sheetName = donors.getSheetName( i );
				break;
			}
		}
		readSpeakers( donors.getSheet( sheetName ), donorList );

	}
	
	/**
	 * 
	 */
	private void readDonations( Sheet donorSheet, Vector< Recipient > donorList ) {
		int firstIndex  = -1;
		int lastIndex   = -1;
		int orgIndex    = -1;
		int typeIndex   = -1;
		int infoIndex   = -1;
		
		int streetIndex = -1;
		int cityIndex   = -1;
		int stateIndex  = -1;
		int zipIndex    = -1;
		
		int beneTypeIndex  = -1;
		int beneFirstIndex = -1;
		int beneLastIndex  = -1;

		int countIndex = -1;

		int donorCount = 0;

		Row header = donorSheet.getRow( 0 );
		int numCells = header.getPhysicalNumberOfCells();
		for( int i = 0; i < numCells; i++ ) {
			Cell c = header.getCell( i );
			if( c.getCellType() != Cell.CELL_TYPE_STRING ) {
				continue;
			}
			
			String value = c.getStringCellValue();
			if( value.matches( ".*Donor First.*" ) ) {
				firstIndex = c.getColumnIndex();
			} else if( value.matches( ".*Donor Last.*" ) ) {
				lastIndex = c.getColumnIndex();
			} else if( value.matches( ".*Donor Organization.*") ) {
				orgIndex = c.getColumnIndex();
			} else if( value.matches( ".*Donor Type.*") ) {
				typeIndex = c.getColumnIndex();
			}  else if( value.matches( ".*Donation Info.*") ) {
                infoIndex = c.getColumnIndex();
            } else if( value.matches( ".*Street.*" ) ) {
				streetIndex = c.getColumnIndex();
			} else if( value.matches( ".*City.*") ) {
				cityIndex = c.getColumnIndex();
			} else if( value.matches( ".*State.*") ) {
				stateIndex = c.getColumnIndex();
			} else if( value.matches( ".*Zip.*") ) {
				zipIndex = c.getColumnIndex();
			} else if( value.matches( ".*Beneficiary First.*") ) {
				beneFirstIndex = c.getColumnIndex();
			} else if( value.matches( ".*Beneficiary Last.*") ) {
				beneLastIndex = c.getColumnIndex();
			} else if( value.matches( ".*Beneficiary Type.*") ) {
				beneTypeIndex = c.getColumnIndex();
			} else if( value.matches( ".*Letter Count.*") ) {
				countIndex = c.getColumnIndex();
			}
			
			if( firstIndex >= 0 && lastIndex >= 0 && orgIndex >= 0 && typeIndex >= 0 && infoIndex >= 0) {
				if( streetIndex >= 0 && cityIndex >= 0 && stateIndex >= 0 && zipIndex >= 0 ) {
					if( beneTypeIndex >= 0 && beneFirstIndex >= 0 && beneLastIndex >=0 ) {
						if( countIndex >= 0 ) {
							break;
						}
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
			Address address = getAddress( r, streetIndex, cityIndex, stateIndex, zipIndex );
			
			// Pull the other donor information
			String first = "";
			Cell firstCell = r.getCell( firstIndex );
			if( firstCell != null ) { first = firstCell.getStringCellValue(); }
			
			String last = "";
			Cell lastCell = r.getCell( lastIndex );
			if( lastCell != null ) { last = lastCell.getStringCellValue(); }
			
			String org = "";
			Cell orgCell = r.getCell( orgIndex );
			if( orgCell != null ) { org = orgCell.getStringCellValue(); }
			
			String type = "";
			Cell typeCell = r.getCell( typeIndex );
			if( typeCell != null ) { type = typeCell.getStringCellValue(); }

			String info = "";
			Cell infoCell = r.getCell( infoIndex );
			if( infoCell != null ) { info = infoCell.getStringCellValue(); }

			int count = 1;
			Cell countCell = r.getCell( countIndex );
			if( countCell != null ) { count = (int)countCell.getNumericCellValue(); }
			
			if( first == "" && last == "" && org == "" ) {
				continue;
			}
			Donor donor = new Donor( first, last, org, type, "", info, address );
			donorCount++;

			// If this donor sponsored a student or j-staff, add it
			String beneType = "";
			Cell beneTypeCell = r.getCell( beneTypeIndex );
			if( beneTypeCell != null ) { beneType = beneTypeCell.getStringCellValue(); }
			
			if( beneType.matches( "Ambassador" ) || beneType.matches( "Junior Staff" ) ) {
				String beneFirst = "";
				Cell beneFirstCell = r.getCell( beneFirstIndex );
				if( beneFirstCell != null ) { beneFirst = beneFirstCell.getStringCellValue(); }
				
				String beneLast = "";
				Cell beneLastCell = r.getCell( beneLastIndex );
				if( beneLastCell != null ) { beneLast = beneLastCell.getStringCellValue(); }
				
				if( beneFirst == "" || beneLast == "" ) { 
					failed.put(donor, "Student information missing"); 
					continue; 
				}
				
				addMatch( beneFirst, beneLast, donor );
				continue;
			}

			for( int j = 0; j < count; j++ ) {
                donorList.add(donor);
            }
		}
		System.out.println(donorCount);
	}
	
	/**
	 * 
	 */
	private void readSpeakers( Sheet donorSheet, Vector< Recipient > donorList ) {
		int titleIndex  = -1;
		int firstIndex  = -1;
		int lastIndex   = -1;
		int roleIndex   = -1;
		
		int streetIndex = -1;
		int cityIndex   = -1;
		int stateIndex  = -1;
		int zipIndex    = -1;

		int priorityIndex = -1;
		
		Row header = donorSheet.getRow( 0 );
		int numCells = header.getPhysicalNumberOfCells();
		for( int i = 0; i < numCells; i++ ) {
			Cell c = header.getCell( i );
			if( c.getCellType() != Cell.CELL_TYPE_STRING ) {
				continue;
			}
			
			String value = c.getStringCellValue();
			if( value.matches( ".*Title.*" ) ) {
				titleIndex = c.getColumnIndex();
			} else if( value.matches( ".*First.*" ) ) {
				firstIndex = c.getColumnIndex();
			} else if( value.matches( ".*Last.*" ) ) {
				lastIndex = c.getColumnIndex();
			} else if( value.matches( ".*Role.*") ) {
				roleIndex = c.getColumnIndex();
			} else if( value.matches( ".*Street.*" ) ) {
				streetIndex = c.getColumnIndex();
			} else if( value.matches( ".*City.*") ) {
				cityIndex = c.getColumnIndex();
			} else if( value.matches( ".*State.*") ) {
				stateIndex = c.getColumnIndex();
			} else if( value.matches( ".*Zip.*") ) {
				zipIndex = c.getColumnIndex();
			} else if( value.matches( ".*Letter Count.*") ) {
                priorityIndex = c.getColumnIndex();
            }
			
			if( titleIndex >= 0 && firstIndex >= 0 && lastIndex >= 0 && roleIndex >= 0) {
				if( streetIndex >= 0 && cityIndex >= 0 && stateIndex >= 0 && zipIndex >= 0 ) {
				    if( priorityIndex >= 0 ) {
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
			Address address = getAddress( r, streetIndex, cityIndex, stateIndex, zipIndex );
			
			// Pull the other donor information
			String title = "";
			Cell titleCell = r.getCell( titleIndex );
			if( titleCell != null ) { title = titleCell.getStringCellValue(); }
			
			String first = "";
			Cell firstCell = r.getCell( firstIndex );
			if( firstCell != null ) { first = firstCell.getStringCellValue(); }
			
			String last = "";
			Cell lastCell = r.getCell( lastIndex );
			if( lastCell != null ) { last = lastCell.getStringCellValue(); }
			
			String role = "";
			Cell roleCell = r.getCell( roleIndex );
			if( roleCell != null ) { role = roleCell.getStringCellValue(); }

            int count = 1;
            Cell countCell = r.getCell( priorityIndex );
            if( countCell != null ) { count = (int)countCell.getNumericCellValue(); }
			
			if( first == "" && last == "" ) {
				continue;
			}
			Speaker speaker = new Speaker( title, first, last, role, address );

            for( int j = 0; j < count; j++ ) {
                donorList.add(speaker);
            }
		}	
	}

	private void readStaff( Sheet staffSheet, Vector< Recipient > donorList ) {
        // Get the column indexes for the information we need
        int firstIndex = -1;
        int lastIndex  = -1;
        int colorIndex = -1;
        int groupIndex = -1;
        int roleIndex  = -1;

        int streetIndex = -1;
        int cityIndex   = -1;
        int stateIndex  = -1;
        int zipIndex    = -1;

        int priorityIndex = -1;

        Row header = staffSheet.getRow( 0 );
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
            } else if( value.matches( ".*Role.*") ) {
                roleIndex = c.getColumnIndex();
            } else if( value.matches( ".*Street.*" ) ) {
                streetIndex = c.getColumnIndex();
            } else if( value.matches( ".*City.*") ) {
                cityIndex = c.getColumnIndex();
            } else if( value.matches( ".*State.*") ) {
                stateIndex = c.getColumnIndex();
            } else if( value.matches( ".*Zip.*") ) {
                zipIndex = c.getColumnIndex();
            } else if( value.matches( ".*Letter Count.*") ) {
                priorityIndex = c.getColumnIndex();
            }

            if( firstIndex >= 0 && lastIndex >= 0 && groupIndex >= 0 && colorIndex >= 0 && roleIndex >= 0 ) {
                if( streetIndex >= 0 && cityIndex >= 0 && stateIndex >= 0 && zipIndex >= 0 ) {
                    if( priorityIndex >= 0 ) {
                        break;
                    }
                }
            }
        }

        int numRows = staffSheet.getPhysicalNumberOfRows();
        for( int i = 1; i < numRows; i++ ) {
            Row r = staffSheet.getRow( i );
            if( r == null ) {
                continue;
            }

            Cell fCell   = r.getCell( firstIndex );
            if( fCell == null ) { continue; }
            String first = ( fCell.getCellType() == Cell.CELL_TYPE_STRING || fCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? fCell.getStringCellValue() : "";

            Cell lCell  = r.getCell( lastIndex );
            if( lCell == null ) { continue; }
            String last = ( lCell.getCellType() == Cell.CELL_TYPE_STRING || lCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? lCell.getStringCellValue() : "";

            Cell sCell     = r.getCell( colorIndex );
            if( sCell == null ) { continue; }
            String section = ( sCell.getCellType() == Cell.CELL_TYPE_STRING || sCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? sCell.getStringCellValue() : "";

            Cell gCell   = r.getCell( groupIndex );
            if( gCell == null ) { continue; }
            String group = ( gCell.getCellType() == Cell.CELL_TYPE_STRING || gCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? gCell.getStringCellValue() : "";

            Cell rCell   = r.getCell( roleIndex );
            if( rCell == null ) { continue; }
            String role = ( rCell.getCellType() == Cell.CELL_TYPE_STRING || rCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? rCell.getStringCellValue() : "";

            Cell pCell   = r.getCell( priorityIndex );
            if( pCell == null ) { continue; }
            int priority = ( pCell.getCellType() == Cell.CELL_TYPE_NUMERIC || pCell.getCellType() == Cell.CELL_TYPE_FORMULA ) ? (int) pCell.getNumericCellValue() : 1;

            if( first == "" && last == "" ) {
                continue;
            }

            Address address = getAddress( r, streetIndex, cityIndex, stateIndex, zipIndex );
            Staff s;
            if( role.matches( "Section Leader") ) {
                s = new Staff( first, last, String.format( "%s %s", section, role ), address );
            } else if( role.matches( "Facilitator" ) ) {
                s = new Staff( first, last, String.format( "%s %s", group, role ), address );
            } else if( role.matches( "J-Staff") || role.matches( "Junior Staff") || role.matches( "J Staff") ) {
                studentList.add( new JStaff( first, last, section, group ) );
                s = new Staff( first, last, String.format( "%s %s", group, role ), address );
            } else {
                s = new Staff( first, last, role, address );
            }

            for( int j = 0; j < priority; j++ ) {
                donorList.add(s);
            }
        }
    }
	
	/**
	 * 
	 * @param row
	 * @param streetIndex
	 * @param cityIndex
	 * @param stateIndex
	 * @param zipIndex
	 * @return
	 */
	private Address getAddress( Row row, int streetIndex, int cityIndex, int stateIndex, int zipIndex ) {
		Cell streetCell = row.getCell( streetIndex );
		if( streetCell == null ) { return null; }
		String street = streetCell.getCellType() == Cell.CELL_TYPE_STRING || streetCell.getCellType() == Cell.CELL_TYPE_FORMULA ? streetCell.getStringCellValue() : "";
		
		Cell cityCell = row.getCell( cityIndex );
		if( cityCell == null ) { return null; }
		String city = cityCell.getCellType() == Cell.CELL_TYPE_STRING || cityCell.getCellType() == Cell.CELL_TYPE_FORMULA ? cityCell.getStringCellValue() : "";
		
		Cell stateCell = row.getCell( stateIndex );
		if( stateCell == null ) { return null; }
		String state = stateCell.getCellType() == Cell.CELL_TYPE_STRING || stateCell.getCellType() == Cell.CELL_TYPE_FORMULA ? stateCell.getStringCellValue() : "";

		ZipCode zip   = null;
		Cell zipCell = row.getCell( zipIndex );
		if( zipCell == null ) { return null; }
		if( zipCell.getCellType() == Cell.CELL_TYPE_STRING || zipCell.getCellType() == Cell.CELL_TYPE_FORMULA ) {

		    zip = new ZipCode(zipCell.getStringCellValue());

		} else if( zipCell.getCellType() == Cell.CELL_TYPE_NUMERIC )
        {
			zip = new ZipCode((int)zipCell.getNumericCellValue());
		}
		
		return new Address( street, city, state, zip );
	}
	
	/**
	 * 
	 * @param firstName
	 * @param lastName
	 */
	private void addMatch( String firstName, String lastName, Recipient r ) {
		for( Iterator<Writer> it = studentList.iterator(); it.hasNext(); ) {
			Student s = ( Student ) it.next();
			if( s.getFirstName().matches( firstName ) && s.getLastName().matches( lastName ) ) {
				s.addRecipient( r );
				return;
			}
		}
		
		failed.put( r, String.format( "Couldn't find %s %s in the group matchings", firstName, lastName ) );
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
