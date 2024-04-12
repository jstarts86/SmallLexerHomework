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
		NumberLiteral numberLiteral = new NumberLiteral();
		LexemeChecker checker = new LexemeChecker();
		StringInputPasser test = new StringInputPasser(content);
		StringInputPasser temp_holder = new StringInputPasser(content);
		while(!test.getNonConsumed().isEmpty()) {
			if(!test.getConsumed().isEmpty() && CharacterList.blankTokenDelimeterList.contains(test.getNonConsumed().charAt(0))) {
				test.setNonConsumed(test.getNonConsumed().stripLeading());
			}

			else if(Character.isAlphabetic(test.getNonConsumed().charAt(0)) || test.getNonConsumed().charAt(0) == '$') {
				// if consumed is not empty character or \n then print out Consumed and
				temp_holder = checker.check_lexeme(test.getNonConsumed(), identifier);
				if(!temp_holder.getError()) {
					test = checker.check_lexeme(test.getNonConsumed(),identifier);
					checker.printTokenDelimeters(test.getDelimeter());
					checker.correctOutputWithGivenLexeme(test);
				}

			}else if(test.getNonConsumed().charAt(0) == '-') {
				temp_holder = checker.check_lexeme(test.getNonConsumed(), comment);
				if(!temp_holder.getError()) {
					test = checker.check_lexeme(test.getNonConsumed(),comment);
					checker.printTokenDelimeters(test.getDelimeter());
					checker.correctOutputWithGivenLexeme(test);
				}
			}
			else if(test.getNonConsumed().charAt(0) == '"') {
				temp_holder = checker.check_lexeme(test.getNonConsumed(), stringLiteral);
				if(!temp_holder.getError()) {
					test = checker.check_lexeme(test.getNonConsumed(),stringLiteral);
					checker.printTokenDelimeters(test.getDelimeter());
			        checker.correctOutputWithGivenLexeme(test);
				}
			} else if(Character.isDigit(test.getNonConsumed().charAt(0))) {
				temp_holder = checker.check_lexeme(test.getNonConsumed(), numberLiteral);
				if(!temp_holder.getError()) {
					test = checker.check_lexeme(test.getNonConsumed(),numberLiteral);
					checker.printTokenDelimeters(test.getDelimeter());
			        checker.correctOutputWithGivenLexeme(test);
				} else {
					temp_holder = checker.check_lexeme(test.getNonConsumed(), identifier);
					if (temp_holder.getError()) {
						test = checker.check_lexeme(test.getNonConsumed(), identifier);
					if(test.getConsumed().charAt(0) != '$' || !Character.isAlphabetic(test.getConsumed().charAt(0))) {
						System.out.println(test.getConsumed() + "             illegal ID starting with wrong character");
						checker.printTokenDelimeters(test.getDelimeter());
					}
					}
				}
			} else if(CharacterList.realTokenDelimeters.contains(test.getNonConsumed().charAt(0))){
				checker.printTokenDelimeters(test.getNonConsumed().charAt(0));
				test.setNonConsumed(test.getNonConsumed().substring(1));
			} else {
				// if consumed is not empty character or \n then print out Consumed and
				temp_holder = checker.check_lexeme(test.getNonConsumed(), identifier);
				if (temp_holder.getError()) {
					test = checker.check_lexeme(test.getNonConsumed(), identifier);
					if(test.getConsumed().charAt(0) != '$' || !Character.isAlphabetic(test.getConsumed().charAt(0))) {
						System.out.println(test.getConsumed() + "             illegal ID starting with wrong character");
						checker.printTokenDelimeters(test.getDelimeter());
					}
				}
			}
//			test = checker.check_lexeme(test.getNonConsumed(), identifier);
//			checker.printTokenDelimeters(test.getDelimeter());
//			checker.correctOutputWithGivenLexeme(test);

		}
//		while(!test.getNonConsumed().isEmpty()) {
//			switch (test.getNonConsumed().charAt(0)) {
//				case ' ', '\n':
//					System.out.println("hiiiiiiiiiIIIII");
//					test.setNonConsumed(test.getNonConsumed().substring(1));
//					continue;
//				case '-':
//					temp_holder = checker.check_lexeme(test.getNonConsumed(),comment);
//					if(!temp_holder.getError()) {
//						continue;
//					} else {
////						test = checker.check_lexeme(temp_holder.getNonConsumed(),comment);
//						break;
//					}
//				case '$':
//					temp_holder = checker.check_lexeme(test.getNonConsumed(),identifier);
//					if(!temp_holder.getError()) {
//						continue;
//					} else {
////						test = checker.check_lexeme(temp_holder.getNonConsumed(),identifier);
//						break;
//					}
//				case '"':
//					temp_holder = checker.check_lexeme(test.getNonConsumed(), stringLiteral);
//					if(!temp_holder.getError()) {
//						continue;
//					} else {
////						test = checker.check_lexeme(temp_holder.getNonConsumed(), stringLiteral);
//						break;
//					}
//
//				default:
//					if(Character.isAlphabetic(test.getNonConsumed().charAt(0))) {
//						break;
//					} if(test.getNonConsumed().charAt(0) == ' ' || test.getNonConsumed().charAt(0) == '\n' ) {
//
//						break;
//					}
//			}

//			System.out.println("this is test");
//			System.out.println(test);
//			test = temp_holder;
//			System.out.println("this is temp");
//			System.out.println(temp_holder);
//			System.out.print('\n');
		//}
	}
}
