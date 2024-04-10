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
		int accepting_index = current_lexeme.getTransitionTable()[0].length;
		StringInputPasser result = new StringInputPasser();
		String consumed_string = result.getConsumed();
		int current_transition_table[][] = current_lexeme.getTransitionTable();
		HashMap<Integer, ArrayList<Character>> current_hashmap = current_lexeme.getInputChars();
		//Loop through all of the string
		for (int i = 0; i < current_string.length(); i++) {
			char ch = current_string.charAt(i);
			int new_state = 0;
			while((current_transition_table[state][accepting_index] != Lexeme.ACCEPTING_STATE) && (current_transition_table[state][accepting_index] != Lexeme.ERROR)) {
				for(Map.Entry<Integer, ArrayList<Character>> entry : current_hashmap.entrySet()) {
					int key = entry.getKey();
					ArrayList<Character> value = entry.getValue();
					if(value.contains(ch)) {
						new_state = current_transition_table[state][key];
						break;
					}
				}
				state = new_state;
				if(current_transition_table[state][accepting_index] == Lexeme.ACCEPTING_STATE) {
					isAccepted = true;
				} else {
					isAccepted = false;
				}
				consumed_string += ch;
			}
		}
		result.setConsumed(consumed_string);

		return null;
	}
}
