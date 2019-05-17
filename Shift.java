package shiftman.server;

import java.util.ArrayList;
import java.util.List;

public class Shift extends DateTime {
	
	private int _minimumWorkers;
	private List<Staff> _staffList = new ArrayList<Staff>();
	private boolean _managerPresent = false;
	private Staff _manager;
	private int _numberWorkers;
	
	public Shift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) throws InvalidTimeException {
		super(dayOfWeek, startTime,endTime);
		_minimumWorkers = Integer.parseInt(minimumWorkers);

	}
	
	public void assignStaff(Staff staff, boolean isManager) {
		if(isManager) {
			_manager = staff;
			_managerPresent = true;

		}
		else {
			_numberWorkers++;
			_staffList.add(staff);
		}
	}
	
	public boolean haveManager() {
		return _managerPresent;
	}
	
	public Staff whoIsManager() {
		return _manager;
	}
	
	public List<Staff> whoIsWorking() {
		return _staffList;
	}
	
	public int howManyWorkers() {
		return _numberWorkers;
	}
	
	public int minimumWorkersNeeded() {
		return _minimumWorkers;
	}
	
}
