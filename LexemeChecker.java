import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LexemeChecker {
	private int state = 0;
	private boolean isAccepted;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public void setAccepted(boolean accepted) {
		isAccepted = accepted;
	}

	public StringInputPasser check_lexeme(String current_string, Lexeme current_lexeme) {
		int accepting_index = current_lexeme.getTransitionTable()[0].length - 1;
		StringInputPasser result = new StringInputPasser();
		String consumed_string = "";
		int current_transition_table[][] = current_lexeme.getTransitionTable();
		HashMap<Integer, ArrayList<Character>> current_hashmap = current_lexeme.getInputChars();
		Boolean current_acceptance_table[][] = current_lexeme.getAdvanceTable();

		//Keeps track if the current character is
		Boolean isErrorState = false;

		//Loop through all of the string

		for (int i = 0; i < current_string.length(); i++) {
			int new_state = 0;
			char ch = current_string.charAt(i);
			if(CharacterList.tokenDelimeterList.contains(ch) && current_lexeme.getClass() != Comment.class) {
				result.setDelimeter(ch);
				result.setConsumed(consumed_string);
				result.setAccepting(isAccepted);
				result.setNonConsumed(current_string.substring(i + 1));
				result.setError(isErrorState);
				result.setWhichLexeme(current_lexeme);
				return result;
			} else if (ch == '\n' && current_lexeme.getClass() == Comment.class && isErrorState == false) {
				result.setDelimeter(ch);
				result.setConsumed(consumed_string);
				result.setAccepting(isAccepted);
				result.setNonConsumed(current_string.substring(i + 1));
				result.setError(isErrorState);
				result.setWhichLexeme(current_lexeme);
				return result;
			}

			//check which state the character should go too
			if(!isErrorState) {
				for (Map.Entry<Integer, ArrayList<Character>> entry : current_hashmap.entrySet()) {
					int key = entry.getKey();
					ArrayList<Character> value = entry.getValue();
					if (value.contains(ch)) {
						new_state = current_transition_table[state][key];
						if (new_state == Lexeme.ERROR) {
							isErrorState = true;
							break;
						} else {
							isErrorState = false;
							break;
						}
					} else {
						isErrorState = true;
					}
				}
				if (isErrorState == false) {
					state = new_state;
					if (current_transition_table[state][accepting_index] == Lexeme.ACCEPTING_STATE) {
						isAccepted = true;
					} else {
						isAccepted = false;
					}
				}
			}
			consumed_string += ch;
		}
		return result;
	}
}
