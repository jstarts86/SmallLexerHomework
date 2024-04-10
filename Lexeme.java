import java.util.ArrayList;
import java.util.HashMap;

public class Lexeme {
	public static final int ERROR = -1000;
	public static final int NON_ACCEPTING_STATE = -10;
	public static final int ACCEPTING_STATE = -1;
	private int[][] transitionTable;
	private Boolean isAccepting;
	private HashMap<Integer,ArrayList<Character>> inputChars;
	private Boolean[][] advanceTable;

	public Boolean[][] getAdvanceTable() {
		return advanceTable;
	}

	public void setAdvanceTable(Boolean[][] advanceTable) {
		this.advanceTable = advanceTable;
	}

	public void setInputChars(HashMap<Integer, ArrayList<Character>> inputChars) {
		this.inputChars = inputChars;
	}

	public HashMap getInputChars() {
		return inputChars;
	}

	public int[][] getTransitionTable() {
		return transitionTable;
	}

	public void setTransitionTable(int[][] transitionTable) {
		this.transitionTable = transitionTable;
	}

	public Boolean getAccepting() {
		return isAccepting;
	}

	public void setAccepting(Boolean accepting) {
		isAccepting = accepting;
	}



}
