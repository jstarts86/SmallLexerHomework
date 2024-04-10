import java.util.ArrayList;
import java.util.HashMap;

public class Identifier extends Lexeme{

	public Identifier(String current_word) {
		this.setCurrentWord(current_word);
		this.setAccepting(false);
		int[][] array = {
			//   $ , Letter, Digit
				{1, 1, ERROR, NON_ACCEPTING_STATE},
				{1,1,1, ACCEPTING_STATE}
		};
		HashMap<Integer, ArrayList<Character>> map = new HashMap<Integer, ArrayList<Character>>();
		ArrayList<Character> $_list = new ArrayList<>();
		$_list.add('$');
		map.put(0, $_list);
		ArrayList<Character> all_letters = new ArrayList<>();
        // Iterate through the ASCII values of uppercase letters
        for (int i = 'A'; i <= 'Z'; i++) {
            all_letters.add((char) i);
        }

        // Iterate through the ASCII values of lowercase letters
        for (int i = 'a'; i <= 'z'; i++) {
            all_letters.add((char) i);
        }
		map.put(1, all_letters);

		ArrayList<Character> allDigits = new ArrayList<>();

        // Iterate through the ASCII values of digits
        for (int i = '0'; i <= '9'; i++) {
            allDigits.add((char) i);
        }
		map.put(2, allDigits);
    }





}
