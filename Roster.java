 package shiftman.server;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raymond Yang
 */


public class Roster {
	
	private String _shopName;
	private List<DateTime> _workingHours = new ArrayList<DateTime>();
	private List<Staff> _staffList = new ArrayList<Staff>();
	private List<Shift> _shiftsList = new ArrayList<Shift>();
	private List<Staff> _assignedStaff = new ArrayList<Staff>();
	
	/**
	 * Creates a roster object with the specified name
	 * @param shopName	The name of the shop the roster is for
	 * @throws InvalidNameException	If the specified name is null or empty
	 *
	 */
	public Roster(String shopName) throws InvalidNameException {
		if(shopName == null || shopName.isEmpty()) {
			throw new InvalidNameException("ERROR: Name cannot be empty");
		}
		_shopName = shopName;
	}
	
	public void addWorkingHours(DateTime workingHours){
		_workingHours.add(workingHours);
	}
	
	public void addStaff(Staff staff) {
		_staffList.add(staff);
	}
	
	public void addShift(Shift shift) {
		_shiftsList.add(shift);
	}
	
	public void assignStaff(Shift shift, Staff staff, boolean isManager) {
		_assignedStaff.add(staff);
		shift.assignStaff(staff, isManager);
	}
	
	
	public List<Staff> unassignedStaffList() {
		FormatStaff staffList = new FormatStaff(_staffList);
		return staffList.getUnassignedStaff(_assignedStaff);
	}


	public List<Shift> getShiftsWithoutManagers() {
		FormatShift shiftList = new FormatShift(_shiftsList);	
		return shiftList.shiftListFor("WithoutManager");
	}

	public List<Shift> getUnderstaffedShifts() {
		FormatShift shiftList = new FormatShift(_shiftsList);
		return shiftList.shiftListFor("Understaffed");
	}

	public List<Shift> getOverstaffedShifts() {
		FormatShift shiftList = new FormatShift(_shiftsList);
		return shiftList.shiftListFor("Overstaffed");
	}
	
	public List<Shift> getShiftsForDay(String dayOfWeek) {
		List<Shift> shiftsForDay = new ArrayList<Shift>();
		//Check every shift for one that matches the day
		for(int i = 0; i < _shiftsList.size(); i++) {
			if(_shiftsList.get(i).whatDay() == dayOfWeek) {
				shiftsForDay.add(_shiftsList.get(i));
			}
		}
		return shiftsForDay;
	}
	
	public List<String> getRosterForDay(String dayOfWeek) {
		List<String> rosterForDay = new ArrayList<String>();
		rosterForDay.add(_shopName);
		//Find the specified day from the working hours
		for(int i = 0; i < _workingHours.size(); i++) {
			if(_workingHours.get(i).whatDay().equals(dayOfWeek)) {
				rosterForDay.add(dayOfWeek + " " + _workingHours.get(i).startTimeString() + "-" + _workingHours.get(i).endTimeString());
			}
		}
		
		List<Shift> shiftList = this.getShiftsForDay(dayOfWeek);
		FormatShift shiftDesc = new FormatShift(shiftList);
		//Append all elements from the sorted and formatted list to the returning list
		rosterForDay.addAll(shiftDesc.stringForRoster());
		
		//Return an empty list if could not find any shifts or working hours for specified day
		if(rosterForDay.size() == 1) {
			return new ArrayList<String>();
		}
		return rosterForDay;
		
	}
	
	public List<String> getRosterForWorker(String givenName, String familyName) {
		List<String> workerShiftsDesc = new ArrayList<String>();
		workerShiftsDesc.add(familyName + ", " + givenName);
		
		List<Shift> workerShifts = new ArrayList<Shift>();
		//Find the worker with the specified name
		Staff staff = this.findStaff(givenName, familyName);
		
		//Find the shifts the worker is assigned to
		for(int i = 0; i < _shiftsList.size(); i++) {
			for(int j = 0; j < _shiftsList.get(i).whoIsWorking().size(); j++) {
				if(_shiftsList.get(i).whoIsWorking().get(j) == (staff)) {
					workerShifts.add(_shiftsList.get(i));
					break;
				}
			}
		}
		
		//Format the string list
		FormatShift shiftFormat = new FormatShift(workerShifts);
		workerShiftsDesc.addAll(shiftFormat.displayShiftList());
		
		//Return an empty list if could not find the worker or any shifts for the worker
		if(workerShiftsDesc.size() == 1) {
			return new ArrayList<String>();
		}
		
		return workerShiftsDesc;
	}
	
	public List<String> getShiftsManagedBy(String givenName, String familyName) {
		List<String> managerShiftsDesc = new ArrayList<String>();
		managerShiftsDesc.add(familyName + ", " + givenName);
		
		//Find the manager with the specified name
		List<Shift> managerShifts = new ArrayList<Shift>();
		Staff staff = this.findStaff(givenName, familyName);
		
		//Find all the shifts he/she is a manager for
		for(int i = 0; i < _shiftsList.size(); i++) {
			if(_shiftsList.get(i).whoIsManager() != null) {
				if(_shiftsList.get(i).whoIsManager()== (staff)) {
					managerShifts.add(_shiftsList.get(i));
				}
			}
		}
		
		//Format the string list 
		FormatShift shiftFormat = new FormatShift(managerShifts);
		managerShiftsDesc.addAll(shiftFormat.displayShiftList());
		
		//Return an empty list if could not find manager with specified name or manager is
		//not assigned to any shifts
		if(managerShiftsDesc.size() == 1) {
			return new ArrayList<String>();
		}
		
		return managerShiftsDesc;
	}
	
	public Shift findShift(String dayOfWeek, String startTime, String endTime) {
		//Remove the colon from the string so it is just numbers
		startTime = startTime.replace(":","");
		endTime = endTime.replace(":","");
		//Look through the shift list for a shift that matches
		for(int i = 0; i < _shiftsList.size(); i++) {
			if(_shiftsList.get(i).whatDay().equals(dayOfWeek)) {
				if(_shiftsList.get(i).whatIsStartTime() == (Integer.parseInt(startTime))) {
					if(_shiftsList.get(i).whatIsEndTime() == (Integer.parseInt(endTime))) {
						return _shiftsList.get(i);
					}
				}
			}
		}
		return null;
	}
	
	public Staff findStaff(String givenName, String familyName) {
		//Look through the staff list for a staff name that matches the parameters
		for(int i = 0; i < _staffList.size(); i++) {
			if(_staffList.get(i).whatIsMyFirstName().equals(givenName)) {
				if(_staffList.get(i).whatIsMyLastName().equals(familyName)) {
					return _staffList.get(i);
				}
			}
		}
		return null;
	}

	
	public List<Staff> listOfStaff() {
		return _staffList;
	}

}
