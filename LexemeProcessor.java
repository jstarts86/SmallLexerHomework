import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LexemeProcessor {
	private int state = 0;
	private boolean isAccepted;

	private ArrayList<TokenTerminal> tokenList = new ArrayList<>();
	public ArrayList<TokenTerminal> getTokenList() {
		return tokenList;
	}

	public void setTokenList(ArrayList<TokenTerminal> tokenList) {
		this.tokenList = tokenList;
	}


	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public void setAccepted(boolean accepted) {
		isAccepted = accepted;
	}

	public LexerContext check_lexeme(String current_string, Lexeme current_lexeme) {
		int accepting_index = current_lexeme.getTransitionTable()[0].length - 1;
		LexerContext result = new LexerContext();
		String consumed_string = "";
		int current_transition_table[][] = current_lexeme.getTransitionTable();
		HashMap<Integer, ArrayList<Character>> current_hashmap = current_lexeme.getInputChars();
		Boolean current_acceptance_table[][] = current_lexeme.getAdvanceTable();

		//Keeps track if the current character is
		Boolean isErrorState = false;

		//Loop through all of the string
		int state = 0;
		for (int i = 0; i < current_string.length(); i++) {
			int new_state = 0;
			char ch = current_string.charAt(i);
			// if the current input is not going through a comment or string literal if you meet a token delimiter return the
			if(CharacterList.tokenDelimeterList.contains(ch) && current_lexeme.getClass() != Comment.class && current_lexeme.getClass() != StringLiteral.class) {
				result.setDelimeter(ch);
				result.setConsumed(consumed_string);
				result.setAccepting(isAccepted);
				result.setNonConsumed(current_string.substring(i + 1));
				result.setError(isErrorState);
				result.setWhichLexeme(current_lexeme);
				return result;

			}
			if((i > 0 && ch == '"') && (current_lexeme.getClass() == StringLiteral.class)) {
				result.setDelimeter(ch);
				result.setConsumed(consumed_string + '"');
				result.setAccepting(isAccepted);
				result.setNonConsumed(current_string.substring(i + 1));
				result.setError(isErrorState);
				result.setWhichLexeme(current_lexeme);
				return result;
			}
			// if
			else if ((ch == '\n' || ch =='\r') && current_lexeme.getClass() == Comment.class && isErrorState == false) {
				result.setDelimeter(ch);
				result.setConsumed(consumed_string);
				result.setAccepting(isAccepted);
				result.setNonConsumed(current_string.substring(i + 1));
				result.setError(isErrorState);
				result.setWhichLexeme(current_lexeme);
				return result;
			} else if(isErrorState == true && (CharacterList.tokenDelimeterList.contains(ch) || CharacterList.blankTokenDelimeterList.contains(ch))) {
				result.setDelimeter(ch);
				result.setConsumed(consumed_string);
				result.setAccepting(isAccepted);
				result.setNonConsumed(current_string.substring(i + 1));
				result.setError(isErrorState);
				result.setWhichLexeme(current_lexeme);
				return result;
			}

			//check which state the character should go too
			if(!isErrorState) {
				for (Map.Entry<Integer, ArrayList<Character>> entry : current_hashmap.entrySet()) {
					int key = entry.getKey();
					ArrayList<Character> value = entry.getValue();
					if (value.contains(ch)) {
						new_state = current_transition_table[state][key];
						if (new_state == Lexeme.ERROR) {
							isErrorState = true;
							break;
						} else {
							isErrorState = false;
							break;
						}
					} else {
						isErrorState = true;
					}
				}
				if (isErrorState == false) {
					state = new_state;
					if (current_transition_table[state][accepting_index] == Lexeme.ACCEPTING_STATE) {
						isAccepted = true;
					} else {
						isAccepted = false;
					}
				}
			}
			consumed_string += ch;
		}
		return result;
	}
	public void correctOutputWithGivenLexeme(LexerContext processed_word) {
		if(processed_word.getWhichLexeme().getClass() == Identifier.class) {
			if(!processed_word.getError()) {
				if(CharacterList.keyWordList.contains(processed_word.getConsumed())) {
					switch (processed_word.getConsumed()) {
						case "program":
							tokenList.add(new TokenTerminal(processed_word.getConsumed(), "program"));
							System.out.println(processed_word.getConsumed() + "                " + "program");
							break;
						case "begin":
							tokenList.add(new TokenTerminal(processed_word.getConsumed(), "begin"));
							System.out.println(processed_word.getConsumed() + "                " + "begin");
							break;
						case "end":
							tokenList.add(new TokenTerminal(processed_word.getConsumed(), "end"));
							System.out.println(processed_word.getConsumed() + "                " + "end");
							break;
						case "int":
							tokenList.add(new TokenTerminal(processed_word.getConsumed(), "int"));
							System.out.println(processed_word.getConsumed() + "                " + "int");
							break;
						case "print_line":
							tokenList.add(new TokenTerminal(processed_word.getConsumed(), "print_line"));
							System.out.println(processed_word.getConsumed() + "                " + "print_line");
							break;
						case "if":
							tokenList.add(new TokenTerminal(processed_word.getConsumed(), "if"));
							System.out.println(processed_word.getConsumed() + "                " + "if");
							break;
						case "else_if":
							tokenList.add(new TokenTerminal(processed_word.getConsumed(), "else_if"));
							System.out.println(processed_word.getConsumed() + "                " + "else_if");
							break;
						case "else":
							tokenList.add(new TokenTerminal(processed_word.getConsumed(), "else"));
							System.out.println(processed_word.getConsumed() + "                " + "else");
							break;
						default:
							tokenList.add(new TokenTerminal(processed_word.getConsumed(), "keyword"));
							System.out.println(processed_word.getConsumed() + "                " + "keyword");
					}
				} else {
					tokenList.add(new TokenTerminal(processed_word.getConsumed(),"identifier"));
					System.out.println(processed_word.getConsumed()+ "                "  + "identifier");
				}
			}
		} else if(processed_word.getWhichLexeme().getClass() == Comment.class) {
			if(!processed_word.getError()) {
				System.out.println(processed_word.getConsumed()+ "                "   + "comment");
			}
		} else if(processed_word.getWhichLexeme().getClass() == StringLiteral.class) {
			if(!processed_word.getError()) {
				tokenList.add(new TokenTerminal(processed_word.getConsumed(), "string_literal"));
				System.out.println(processed_word.getConsumed()+ "                "   + "String Literal");
			}
		} else if(processed_word.getWhichLexeme().getClass() == NumberLiteral.class) {
			if(!processed_word.getError()) {
				tokenList.add(new TokenTerminal(processed_word.getConsumed(),"number_literal"));
				System.out.println(processed_word.getConsumed()+ "                "   + "Number Literal");
			}
		}


	}
	public void printTokenDelimeters(Character tokenDelemiter) {
		switch (tokenDelemiter) {
			case '=':
				tokenList.add(new TokenTerminal("=","assignment_operator"));
				System.out.println("=" + "                " + "assignment operator");
				break;
			case '>':
				tokenList.add(new TokenTerminal(">","greater_than_operator"));
				System.out.println(">" + "                " + "greater than operator");
				break;
			case '<':
				tokenList.add(new TokenTerminal("<","less_than_operator"));
				System.out.println("<" + "                " + "less than operator");
				break;
			case '+':
				tokenList.add(new TokenTerminal("+","plus_operator"));
				System.out.println("+" + "                " + "plus operator");
				break;
			case '-':
				tokenList.add(new TokenTerminal("-","minus_operator"));
				System.out.println("-"+ "                "  + "minus operator");
				break;
			case '*':
				tokenList.add( new TokenTerminal("*", "multiplication_operator"));
				System.out.println("*" + "                "  + "multiplication operator");
				break;
			case ')':
				tokenList.add(new TokenTerminal("(", "right_parenthesis_operator"));
				System.out.println(")"+ "                " + "right parenthesis operator");
				break;
			case '(':
				tokenList.add(new TokenTerminal("(", "left_parenthesis_operator"));
				System.out.println("("+ "                "  + "left parenthesis operator");
				break;
			case ';':
				tokenList.add(new TokenTerminal(";","statement_terminator"));
				System.out.println(";" + "                "  + "statement terminator");
				break;
			case ',':
				tokenList.add(new TokenTerminal( ",","punctuation_comma"));
				System.out.println("," + "                "  + "punctuation - comma");
				break;
		}
	}
}
