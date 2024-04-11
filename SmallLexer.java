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
		Comment comment = new Comment();
		StringLiteral stringLiteral = new StringLiteral();
		LexemeChecker checker = new LexemeChecker();
		StringInputPasser test = new StringInputPasser(content);
		StringInputPasser temp_holder = new StringInputPasser(content);
		while(!test.getNonConsumed().isEmpty()) {
			switch (test.getNonConsumed().charAt(0)) {
				case '-':
					temp_holder = checker.check_lexeme(test.getNonConsumed(),comment);
					if(!temp_holder.getError()) {
						continue;
					} else {
//						test = checker.check_lexeme(temp_holder.getNonConsumed(),comment);
						break;
					}
				case '$':
					temp_holder = checker.check_lexeme(test.getNonConsumed(),identifier);
					if(!temp_holder.getError()) {
						continue;
					} else {
//						test = checker.check_lexeme(temp_holder.getNonConsumed(),identifier);
						break;
					}
				case '"':
					temp_holder = checker.check_lexeme(test.getNonConsumed(), stringLiteral);
					if(!temp_holder.getError()) {
						continue;
					} else {
//						test = checker.check_lexeme(temp_holder.getNonConsumed(), stringLiteral);
						break;
					}
				default:
					if(Character.isAlphabetic(test.getNonConsumed().charAt(0))) {
						temp_holder = checker.check_lexeme(test.getNonConsumed(), identifier);
						break;
					}
			}
			test = checker.check_lexeme(temp_holder.getNonConsumed(), temp_holder.getWhichLexeme());
			System.out.println(test);
			System.out.print('\n');
		}
	}
}
