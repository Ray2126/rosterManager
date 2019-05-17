package shiftman.server;

public class DateTime {

	private String _day;
	private int _startTime;
	private int _endTime;
	private String _startTimeString;
	private String _endTimeString;
	enum Dates {
		Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
	}
	
	public DateTime(String dayOfWeek, String startTime, String endTime) throws InvalidTimeException{
		//Check if the day given is identical to any of the Dates enums
		int count = 0;
		for(Dates d : Dates.values()) {
			if(dayOfWeek == d.toString()) {
				count++;
			}
		}
		if(count == 0) {
			throw new InvalidTimeException("ERROR: Please enter Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, or Sunday.");
		}
		
		//Make sure the times given are not invalid
		if(Integer.parseInt(startTime.replace(":", "")) >= Integer.parseInt(endTime.replace(":", ""))) {throw new InvalidTimeException("ERROR: The times you have entered are invalid.");}
		if((Integer.parseInt(startTime.replace(":", ""))) < 0000) {throw new InvalidTimeException("ERROR: The times you have entered are invalid.");}
		if((Integer.parseInt(startTime.replace(":", ""))) > 2400) {throw new InvalidTimeException("ERROR: The times you have entered are invalid.");}
		if(Integer.parseInt(endTime.replace(":", "")) < 0000) {throw new InvalidTimeException("ERROR: The times you have entered are invalid.");}
		if(Integer.parseInt(endTime.replace(":", "")) > 2400) {throw new InvalidTimeException("ERROR: The times you have entered are invalid.");}

		_day = dayOfWeek;
		_startTimeString = startTime;
		_endTimeString = endTime;
		_startTime = Integer.parseInt(startTime.replace(":", ""));
		_endTime = Integer.parseInt(endTime.replace(":", ""));
	}
	
	public String startTimeString() {
		return _startTimeString;
	}
	
	public String endTimeString() {
		return _endTimeString;
	}
	
	public int whatIsStartTime() {
		return _startTime;
	}
	
	public int whatIsEndTime() {
		return _endTime;
	}
	
	public String whatDay() {
		return _day;
	}
	
}
