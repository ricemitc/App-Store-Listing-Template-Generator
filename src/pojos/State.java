package pojos;

public class State {

	private String fullName;
	private String abbreviation;
	
	public State() {
		
	}
	
	public State(String line) {
		String[] stateValues = line.split(",");
		this.abbreviation = stateValues[0].trim();
		this.fullName = stateValues[1].trim();
	}
	
	public State(String fullName, String abbreviation) {
		this.fullName = fullName;
		this.abbreviation = abbreviation;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getFullName() {
		return this.fullName;
	}
	
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	public String getAbbreviation() {
		return this.abbreviation;
	}

	@Override
	public String toString() {
		return "State [fullName=" + fullName + ", abbreviation=" + abbreviation + "]";
	}
	
	
}
