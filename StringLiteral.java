import java.util.ArrayList;
import java.util.HashMap;

public class StringLiteral extends Lexeme {

	StringLiteral() {
		this.setAccepting(false);
		int[][] array = {
			//   $ , Letter, Digit
				{1, ERROR, NON_ACCEPTING_STATE},
				{2, 3, NON_ACCEPTING_STATE},
				{ERROR,ERROR, ACCEPTING_STATE},
				{2, 3, NON_ACCEPTING_STATE},
		};
		Boolean[][] advance_table = {
				{true, true,false },
				{true, true, true},
		};
		HashMap<Integer, ArrayList<Character>> map = new HashMap<Integer, ArrayList<Character>>();
		ArrayList<Character> quote_list = new ArrayList<>();
		quote_list.add('"');
		map.put(0, quote_list);
		ArrayList<Character> all_char_except_quote = new ArrayList<>();
        for (char c = '\u0000'; c < '\uFFFF'; c++) {
            if (c != '"') {
                all_char_except_quote.add(c);
            }
        }
		map.put(1, all_char_except_quote);

		this.setTransitionTable(array);
		this.setInputChars(map);
	}
}
