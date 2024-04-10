import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SmallLexer {
	public Boolean check_token(String lexeme) {
		//jchecker
		int state = 1;
		char current_char = lexeme.charAt(0);
		// check a string of characters until they meet a token delimeter

		// stop when the lexer meets a token delemiter


		return true;
	}

	public static void main(String[] args) throws IOException {
		String content = Files.readString(Paths.get(args[0]));
		System.out.println(content);
		Identifier identifier = new Identifier();
		LexemeChecker checker = new LexemeChecker();
		StringInputPasser test = new StringInputPasser(content);
		while(!test.getNonConsumed().isEmpty()) {
			test = checker.check_lexeme(test.getNonConsumed(), identifier);
			System.out.println(test);
			System.out.print('\n');
		}
	}
}
