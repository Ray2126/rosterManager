package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Takes a shift list as a parameter then performs various tasks on that list
 * @author Raymond Yang
 */
public class FormatShift {

	private List<Shift> _shiftList;
	
	public FormatShift(List<Shift> shiftList) {
		_shiftList = shiftList;
	}
	
	private List<Shift> sortShiftList() {
		ShiftComparator shiftComparator = new ShiftComparator();
		Collections.sort(_shiftList, shiftComparator);
		return _shiftList;
	}
	
	/**
	 * Get a shift list with the specified attribute, shifts without managers, shifts
	 * that are overstaffed, or shifts that are understaffed
	 * @param X				A string that describes what to get
	 * @return List<Shift>	The sorted list with the specified attribute
	 */
	public List<Shift> shiftListFor(String X) {
		List<Shift> shiftList = new ArrayList<Shift>();
		for(int i = 0; i < _shiftList.size(); i++) {
			if(X.equals("WithoutManager")) {
				if(_shiftList.get(i).haveManager() == false) {
					shiftList.add(_shiftList.get(i));
				}
			}
			else if(X.equals("Overstaffed")) {
				if(_shiftList.get(i).minimumWorkersNeeded() < _shiftList.get(i).howManyWorkers()) {
					shiftList.add(_shiftList.get(i));
				}
			}
			else {
				if(_shiftList.get(i).minimumWorkersNeeded() > _shiftList.get(i).howManyWorkers()) {
					shiftList.add(_shiftList.get(i));
				}
			}
		}
		_shiftList = shiftList;
		this.sortShiftList();
		return _shiftList;
	}
	
	/**
	 * Takes the shift list and formats it to "[startTime-endTime]"
	 * @return List<String>	A list containing one shift per entry with the correct format
	 */
	public List<String> displayShiftList() {
		List<String> shiftsDisplay = new ArrayList<String>();
		for(int i = 0; i < _shiftList.size(); i++) {
			shiftsDisplay.add(this.displayShift(_shiftList.get(i)));
		}
		return shiftsDisplay;
	}
	
	private String displayShift(Shift shift) {
		return shift.whatDay() + "[" + shift.startTimeString() + "-" + shift.endTimeString() + "]"; 
	}

	/**
	 * Takes the shift list and formats it correctly for the .getRosterForX methods
	 * @return List<String>	Contains the correctly formatted string list
	 */
	public List<String> stringForRoster() {
		this.sortShiftList();
		List<String> stringForRoster = new ArrayList<String>();
		for(int i = 0; i < _shiftList.size(); i++) {
			//Initialize variable temp as current shift as it will be easier to see and read later on
			Shift temp = _shiftList.get(i);
			String shiftDesc = this.displayShift(temp) + " ";
			
			//Add manager string parts to the list
			if(temp.haveManager()) {
				shiftDesc = shiftDesc + "Manager:" + temp.whoIsManager().whatIsMyLastName() + ", " + temp.whoIsManager().whatIsMyFirstName();
			}
			else {
				shiftDesc = shiftDesc + "[No manager assigned]";
			}
			
			shiftDesc = shiftDesc + " ";
			
			//Add worker string parts to the list
			if(temp.howManyWorkers() == 0) {
				shiftDesc = shiftDesc + "[No workers assigned]";
			}
			else {
				shiftDesc = shiftDesc + "[";
				FormatStaff staffDesc = new FormatStaff(temp.whoIsWorking());
				List<String> staffStringList = staffDesc.displayStaffList();
				//Add each individual worker to the list
				shiftDesc = shiftDesc + staffStringList.get(0);
				for(int j = 1; j < staffStringList.size(); j++) {
					shiftDesc = shiftDesc + ", " + staffStringList.get(j);
				}
				shiftDesc = shiftDesc + "]";
			}
			
			stringForRoster.add(shiftDesc);
		}
		return stringForRoster;
	}
}
