package shiftman.server;

import java.util.Comparator;

public class StaffLastNameComparator implements Comparator<Staff>{

	//Compares the family names of two staff
	public int compare(Staff staff1, Staff staff2) {
		return staff1.whatIsMyLastName().compareTo(staff2.whatIsMyLastName());
	}
}
