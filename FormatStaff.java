package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Takes a staff list as a parameter then performs various tasks on that list
 * @author Raymond Yang
 */
public class FormatStaff {

	private List<Staff> _staffList;
	
	public FormatStaff(List<Staff> staffList) {
		_staffList = staffList;
	}
	
	private List<Staff> sortStaffList() {
		StaffLastNameComparator staffComparator = new StaffLastNameComparator();
		Collections.sort(_staffList, staffComparator);
		return _staffList;
	}
	
	public List<Staff> getUnassignedStaff(List<Staff> assignedStaff) {
		List<Staff> unassignedStaff = _staffList;
		unassignedStaff.removeAll(assignedStaff);
		return unassignedStaff;
	}
	
	/**
	 * Formats the staff list correctly; ordered by family name and in the correct format
	 * @return List<String> staff names ordered by family name
	 */
	public List<String> displayStaffList() {
		this.sortStaffList();
		List<String> orderedStaffNames = new ArrayList<String>();
		for(int i = 0; i < _staffList.size(); i++) {
			orderedStaffNames.add(this.displayStaff(_staffList.get(i)));
		}
		return orderedStaffNames;
	}
	
	public String displayStaff(Staff staff) {
		return staff.whatIsMyFirstName() + " " + staff.whatIsMyLastName();
	}

	
}
