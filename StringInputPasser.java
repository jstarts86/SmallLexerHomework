public class StringInputPasser {
	private String consumed;
	private String nonConsumed;
	private Boolean isAccepting;

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
	}
}
