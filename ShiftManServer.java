package shiftman.server;

import java.util.Arrays;
import java.util.List;

/**
 * The server side implementation of ShiftMan. Holds the roster object.
 * @author Raymond Yang
 *
 */

public class ShiftManServer implements ShiftMan {
	
	private Roster _roster;
	
	public ShiftManServer() {
		
	}

	/**
	 * Creates a new roster object with the specified name. It will catch
	 * an InvalidNameException if the specified name is empty or null.
	 * @param shopName	The name of the shop the roster is for
	 * @return String	Returns an empty string if there are no problems or a message
	 * 					relaying what the problem is.
	 */
	@Override
	public String newRoster(String shopName){
		try {
			Roster roster = new Roster(shopName);
			_roster = roster;
			return "";
		}
		catch(InvalidNameException e) {
			return e.getMessage();
		}
	}

	/**
	 * Sets the working hours for the specified day. It will return an error message
	 * if a roster object has yet to be created. It will catch an InvalidTimeException if the
	 * dayOfWeek variable does not match one of the day strings given in ShiftMan documentation, or 
	 * if the times given are invalid.
	 * @param dayOfWeek	The day of the week to set working hours for
	 * 		  startTime	The opening time for the day
	 * 		  endTime 	The closing time for the day
	 * @return String 	Returns an empty string if there are no problems or a message
	 * 					relaying what the problem is.
	 */
	@Override
	public String setWorkingHours(String dayOfWeek, String startTime, String endTime) {
		if(_roster == null) {
			return "ERROR: no roster has been created";
		}
		try {
			DateTime date = new DateTime(dayOfWeek,startTime,endTime);
			_roster.addWorkingHours(date);
		}
		catch (InvalidTimeException e) {
			return e.getMessage();
		}
		
		return "";
	}

	/**
	 * Adds a shift to the specified day with the specified start and end times, with a
	 * minimum number of workers needed to man the shift. It will catch an InvalidTimeException if the
	 * dayOfWeek variable does not match one of the day strings given in ShiftMan documentation, or 
	 * if the times given are invalid.
	 * @param dayOfWeek			The day to add the shift for
	 * 		  startTime 		The start time of the shift
	 * 		  endTime			The end time of the shift
	 * 		  minimumWorkers	The minimum number of workers needed for the shift
	 * @return String	Returns an empty string if there are no problems or a message
	 * 					relaying what the problem is.
	 */
	@Override
	public String addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {
		if(_roster == null) {
			return "ERROR: no roster has been created";
		}
		try {
			Shift shift = new Shift(dayOfWeek,startTime,endTime, minimumWorkers);
			_roster.addShift(shift);
		}
		catch (InvalidTimeException e) {
			return e.getMessage();
		}
		return "";
	}

	/**
	 * Registers a person to the shop as a staff member with the specified given name and
	 * family name. Catches InvalidNameException if either the givenname or familyName are 
	 * empty or null.
	 * @param givenname		The given name of the staff member
	 * 		  familyName	The family name of the staff member
	 * @return String	Returns an empty string if there are no problems or a message
	 * 					relaying what the problem is.
	 */
	@Override
	public String registerStaff(String givenname, String familyName) {
		if(_roster == null) {
			return "ERROR: no roster has been created";
		}
		try {
			Staff staff = new Staff(givenname, familyName);
			_roster.addStaff(staff);
		}
		catch(InvalidNameException e) {
			return e.getMessage();
		}
		return "";
	}
	
	/**
	 * Assign a specific staff member given by two parameters to a specified shift given by 3
	 * parameters, and also assigns them as either worker or manager.
	 * @param dayOfWeek		The day of the shift
	 * 		  startTime 	The start time of the shift
	 * 		  endTime 		The end time of the shift
	 * 		  givenName		The given name of the staff member
	 * 		  familyName	The family name of the staff member
	 * 		  isManager		True = staff is to be a manager, False = staff is to be a worker
	 * @return String 	Returns an empty string if there are no problems or an error message
	 * 					if the roster has not been created.
	 */		  
	@Override
	public String assignStaff(String dayOfWeek, String startTime, String endTime, String givenName, String familyName,
			boolean isManager) {
		if(_roster == null) {
			return "ERROR: no roster has been created";
		}
		Shift shift = _roster.findShift(dayOfWeek, startTime, endTime);
		Staff staff = _roster.findStaff(givenName, familyName);
		_roster.assignStaff(shift, staff, isManager);
		return "";
	}

	
	/**
	 * Get a list of the registered staff ordered by their family names
	 * @return List<String>	Returns a list of string which each entry in the list being 1 staff member or 
	 * 						a list with one entry containing an error message if the roster has not been created 
	 */
	@Override
	public List<String> getRegisteredStaff() {
		if(_roster == null) {
			//Creates a list with one entry
			return Arrays.asList("ERROR: no roster has been created");
		}
		FormatStaff registeredStaff = new FormatStaff(_roster.listOfStaff());
		return registeredStaff.displayStaffList();
	}

	/**
	 * Get a list of the registered but unassigned staff ordered by their family names
	 * @return List<String>	Returns a list of string which each entry in the list being 1 staff member or a list 
	 * 						with one entry containing an error message if the roster has not been created 
	 */
	@Override
	public List<String> getUnassignedStaff() {
		if(_roster == null) {
			//Creates a list with one entry
			return Arrays.asList("ERROR: no roster has been created");
		}
		FormatStaff unassignedStaff = new FormatStaff( _roster.unassignedStaffList());
		return unassignedStaff.displayStaffList();
	}
	
	/**
	 * Get a list of the shifts that do not have managers
	 * @return List<String>	Returns a list of string which each entry in the list being 1 shift or a list with one 
	 * 						entry containing an error message if the roster has not been created 
	 */
	@Override
	public List<String> shiftsWithoutManagers() {
		if(_roster == null) {
			//Creates a list with one entry
			return Arrays.asList("ERROR: no roster has been created");
		}
		FormatShift shiftList = new FormatShift(_roster.getShiftsWithoutManagers());
		List<String> shiftString = shiftList.displayShiftList();
		return shiftString;
	}

	/**
	 * Get a list of understaffed shifts
	 * @return List<String>	Returns a list of string which each entry in the list being 1 shift or a list with one 
	 * 						entry containing an error message if the roster has not been created 
	 */						
	@Override
	public List<String> understaffedShifts() {
		if(_roster == null) {
			//Creates a list with one entry
			return Arrays.asList("ERROR: no roster has been created");
		}
		FormatShift shiftList = new FormatShift(_roster.getUnderstaffedShifts());
		List<String> shiftString = shiftList.displayShiftList();
		return shiftString;
	}

	/**
	 * Get a list of overstaffed shifts
	 * @return List<String>	Returns a list of string which each entry in the list being 1 shift or a list with one 
	 * 						entry containing an error message if the roster has not been created 
	 */	
	@Override
	public List<String> overstaffedShifts() {
		if(_roster == null) {
			//Creates a list with one entry
			return Arrays.asList("ERROR: no roster has been created");
		}
		FormatShift shiftList = new FormatShift(_roster.getOverstaffedShifts());
		List<String> shiftString = shiftList.displayShiftList();
		return shiftString;
	}

	/**
	 * Get a roster for the day
	 * @return List<String>	Return a list of string with the first entry being the name of the shop, the second entry
	 * 						being the working hours of the day, and the remaining entries being the shifts with 1 entry 
	 * 						per shift containing shift times, manager name and worker names. Returns a list with one 
	 * 						entry containing an error message if the roster has not been created
	 */
	@Override
	public List<String> getRosterForDay(String dayOfWeek) {
		if(_roster == null) {
			//Creates a list with one entry
			return Arrays.asList("ERROR: no roster has been created");
		}
		return _roster.getRosterForDay(dayOfWeek);
	}

	/**
	 * Get a roster for the specified worker
	 * @return List<String> Returns a list of string with the first entry being the name of the worker. The remaining entries
	 * 						(one entry per shift) will be the shifts the worker is assigned to. Returns a list with one 
	 * 						entry containing an error message if the roster has not been created
	 */
	@Override
	public List<String> getRosterForWorker(String workerName) {
		if(_roster == null) {
			//Creates a list with one entry
			return Arrays.asList("ERROR: no roster has been created");
		}
		//Split the name into the given name and family name
		String[] splitName = workerName.split(" ");
		return _roster.getRosterForWorker(splitName[0], splitName[1]);
	}

	/**
	 * Get the shifts managed by the specified manager
	 * @return List<String> Return a list of string with the first entry being the name of the manager. The remaining entries
	 * 						(one entry per shift) will be the shifts the manager is assigned to. Returns a list with one entry
	 * 						containing an error message if the roster has not been created
	 */
	@Override
	public List<String> getShiftsManagedBy(String managerName) {
		if(_roster == null) {
			//Creates a list with one entry
			return Arrays.asList("ERROR: no roster has been created");
		}
		//Split the name into the given name and family name
		String[] splitName = managerName.split(" ");
		return _roster.getShiftsManagedBy(splitName[0], splitName[1]);
	}

	@Override
	public String reportRosterIssues() {
		return "";
	}

	@Override
	public String displayRoster() {
		return "";
	}
}
