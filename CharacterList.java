import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CharacterList {

	    public static final List<Character> tokenDelimeterList =
        Collections.unmodifiableList(Arrays.asList('=', '>', '<' ,'+', '-','*' , ')' , '(', ';', '\n', ' ', '\0', ',', '\r', '\t'));

		//
		public static final List<Character> realTokenDelimeters =
        Collections.unmodifiableList(Arrays.asList(',','=', '>', '<' ,'+', '-','*' , ')' , '(', ';', ',' ));

		public static final List<Character> stringLiteralDelimeterList =
        Collections.unmodifiableList(Arrays.asList('=', '>', '<' ,'+', '-','*' , ')' , '(', ';', '\n'));

		public static final List<String> keyWordList =
        Collections.unmodifiableList(Arrays.asList("program", "begin", "if","for","display", "print_line", "int", "while", "end", "else", "else_if"));

		public static final List<Character> blankTokenDelimeterList =
        Collections.unmodifiableList(Arrays.asList(' ' ,'\r' ,'\n' , '\t', '\n'));
		public void printTokenDelimeters(Character tokenDelemiter) {
			switch (tokenDelemiter) {
				case '=':
					System.out.println("=" + "assign");
				case '>':
					System.out.println(">" + "Greater than");
				case '<':
					System.out.println("<" + "Less than");
				case '+':
					System.out.println("+" + "plus operator");
				case '-':
					System.out.println("-" + "minus operator");
				case '*':
					System.out.println("*" + "multiplication");
				case ')':
					System.out.println(")" + "right parentheses");
				case '(':
					System.out.println("(" + "left parantheses");
				case ';':
					System.out.println(";" + "statement terminator");
				case ',':
					System.out.println("," + "comma");
			}
		}

}
