package shiftman.server;

import java.util.Comparator;

public class ShiftComparator implements Comparator<Shift>{

	//Compare the days and if the days are the same, compare start and end times
	public int compare(Shift shift1, Shift shift2) {
		int dayCompare = shift1.whatDay().compareTo(shift2.whatDay());
		if (dayCompare != 0) {
			return dayCompare;
		}
		else {
			if(shift1.whatIsStartTime() > shift2.whatIsEndTime()) {
				return 1;
			}
			else if (shift1.whatIsStartTime() < shift2.whatIsEndTime()){
				return -1;	
			}
			else {
				return 0;
			}
		}
	}
}
