public class StringInputPasser {
	private String consumed;
	private char delimeter;
	private String nonConsumed;
	private Boolean isAccepting;
	private Boolean isError;

	public char getDelimeter() {
		return delimeter;
	}

	public void setDelimeter(char delimeter) {
		this.delimeter = delimeter;
	}

	public Boolean getError() {
		return isError;
	}

	public void setError(Boolean error) {
		isError = error;
	}

	public String getConsumed() {
		return consumed;
	}

	public void setConsumed(String consumed) {
		this.consumed = consumed;
	}

	public String getNonConsumed() {
		return nonConsumed;
	}

	public void setNonConsumed(String nonConsumed) {
		this.nonConsumed = nonConsumed;
	}

	public Boolean getAccepting() {
		return isAccepting;
	}

	public void setAccepting(Boolean accepting) {
		isAccepting = accepting;
	}

	public StringInputPasser() {
		this.nonConsumed = "";
		this.consumed = "";
		this.isAccepting = false;
		this.isError = false;
		this.delimeter = '\0';
	}

	public StringInputPasser(String nonConsumed) {
		this.nonConsumed = nonConsumed;
		this.consumed = "";
		this.isAccepting = false;
		this.isError = false;
		this.delimeter = '\0';
	}

	@Override
	public String toString() {
		return //"Non Consumed = " + nonConsumed + '\n'+
				"Consumed = " + consumed + '\n' +
				"isAccepting = " + isAccepting + '\n'+
				"isError = " + isError + '\n'+
				"Delimiter = " + delimeter;
	}
}
