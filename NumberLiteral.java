import java.util.ArrayList;
import java.util.HashMap;

public class NumberLiteral extends Lexeme {

	NumberLiteral() {
		this.setAccepting(false);
		int[][] array = {
			//   $ , Letter, Digit
				{1, NON_ACCEPTING_STATE},
				{1, ACCEPTING_STATE},
		};
		Boolean[][] advance_table = {
				{true, true,false },
				{true, true, true},
		};
		HashMap<Integer, ArrayList<Character>> map = new HashMap<Integer, ArrayList<Character>>();
		ArrayList<Character> allDigits = new ArrayList<>();

        // Iterate through the ASCII values of digits
        for (int i = '0'; i <= '9'; i++) {
            allDigits.add((char) i);
        }

		map.put(0, allDigits);
		this.setTransitionTable(array);
		this.setInputChars(map);
	}


}
