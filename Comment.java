import java.util.ArrayList;
import java.util.HashMap;

public class Comment extends Lexeme {

	public Comment() {
		this.setAccepting(false);

		int[][] array = {
			//   $ , Letter, Digit
				{1, ERROR, ERROR, NON_ACCEPTING_STATE},
				{2, ERROR, ERROR, NON_ACCEPTING_STATE},
				{4, 4, 3, NON_ACCEPTING_STATE},
				{ERROR, ERROR, ERROR, ACCEPTING_STATE},
				{4, 4, 3, NON_ACCEPTING_STATE}
		};
		Boolean[][] advance_table = {
				{true, true,false },
				{true, true, true},
		};
		HashMap<Integer, ArrayList<Character>> map = new HashMap<Integer, ArrayList<Character>>();
		ArrayList<Character> dash_list = new ArrayList<>();
		dash_list.add('-');
		map.put(0, dash_list);
		ArrayList<Character> all_char_except_NewLine_Dash = new ArrayList<>();
        for (char c = '\u0000'; c < '\uFFFF'; c++) {
            if (c != '\n' && c != '\r' && c != '-') {
                all_char_except_NewLine_Dash.add(c);
            }
        }
		map.put(1, all_char_except_NewLine_Dash);

		ArrayList<Character> new_line_list = new ArrayList<>();
		new_line_list.add('\n');

        // Iterate through the ASCII values of digits
		map.put(2, new_line_list);
		this.setTransitionTable(array);
		this.setInputChars(map);
	}
}
