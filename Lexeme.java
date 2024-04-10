import java.util.ArrayList;
import java.util.HashMap;

public class Lexeme {
	public static final int ERROR = -1000;
	public static final int NON_ACCEPTING_STATE = -10;
	public static final int ACCEPTING_STATE = -1;
	private int[][] transitionTable;
	private String currentWord;
	private Boolean isAccepting;
	private HashMap<Integer,ArrayList<Character>> inputChars;
	private Boolean[][] advance_table;

	public HashMap getInputChars() {
		return inputChars;
	}

	public void setInputChars(HashMap inputChars) {
		this.inputChars = inputChars;
	}

	public int[][] getTransitionTable() {
		return transitionTable;
	}

	public void setTransitionTable(int[][] transitionTable) {
		this.transitionTable = transitionTable;
	}

	public String getCurrentWord() {
		return currentWord;
	}

	public void setCurrentWord(String currentWord) {
		this.currentWord = currentWord;
	}

	public Boolean getAccepting() {
		return isAccepting;
	}

	public void setAccepting(Boolean accepting) {
		isAccepting = accepting;
	}



}
