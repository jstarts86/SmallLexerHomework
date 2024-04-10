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

	}
}
