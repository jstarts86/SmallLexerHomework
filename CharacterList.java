import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CharacterList {

	    public static final List<Character> tokenDelimeterList =
        Collections.unmodifiableList(Arrays.asList('=', ';','>', '<' ,'+', '-','*' ,'/', ')' , '(', ';', '\n', ' ', '\0'));

		public static final List<Character> realTokenDelimeters =
        Collections.unmodifiableList(Arrays.asList('=', ';','>', '<' ,'+', '-','*' ,'/', ')' , '(', ';', '\n', ' ', '\0'));

}
