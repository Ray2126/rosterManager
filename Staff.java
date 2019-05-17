package shiftman.server;

public class Staff {
	
	private String _givenName;
	private String _familyName;
	
	
	public Staff(String givenName, String familyName) throws InvalidNameException{
		if(givenName.isEmpty() || givenName == null) {
			throw new InvalidNameException("ERROR: Name is invalid.");
		}
		if(familyName.isEmpty() || familyName == null) {
			throw new InvalidNameException("ERROR: Name is invalid.");
		}
		_givenName = givenName;
		_familyName = familyName;
	}
	
	public String whatIsMyFirstName() {
		return _givenName;
	}
	
	public String whatIsMyLastName() {
		return _familyName;
	}
	

}
