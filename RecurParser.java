import java.io.IOException;
import java.util.*;

public class RecurParser {
	private ArrayList<TokenTerminal> tokenList = new ArrayList<>();
	private int currentIndex = 0;
	private int lookAheadIndex = 1;
	private Boolean isError = false;
	private int begin_count;
	private int end_count;
	private Map<String, Integer> symbolTable = new HashMap<>();
	public RecurParser(ArrayList<TokenTerminal> tokenList) {
		this.tokenList = tokenList;
	}

	public ArrayList<TokenTerminal> getTokenList() {
		return tokenList;
	}

	public void setTokenList(ArrayList<TokenTerminal> tokenList) {
		this.tokenList = tokenList;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getLookAheadIndex() {
		return lookAheadIndex;
	}

	public void setLookAheadIndex(int lookAheadIndex) {
		this.lookAheadIndex = lookAheadIndex;
	}

	public Boolean getError() {
		return isError;
	}

	public void setError(Boolean error) {
		isError = error;
	}

	public void match(TokenTerminal currentTerminal) {
		if (currentIndex < tokenList.size() && Objects.equals(this.getTokenList().get(this.getCurrentIndex()).terminal, currentTerminal.terminal)) {
			if(Objects.equals(currentTerminal.terminal, "begin")) {
				begin_count++;
			} if(Objects.equals(currentTerminal.terminal, "end")) {
				end_count++;
			}
			this.setCurrentIndex(this.getCurrentIndex() + 1);
			this.setLookAheadIndex(this.getLookAheadIndex() + 1);
		} else {
			this.setError(true);
		}
	}

	public void program_statement(){
		this.match(new TokenTerminal("program"));
		this.match(new TokenTerminal("identifier"));
		this.match(new TokenTerminal("begin"));
		this.body_sequence();
		this.match(new TokenTerminal("end"));
	}
	public void body_sequence() {
		this.body();
		this.body_sequence_tail();
	}

	public void body() {
		if(Objects.equals(this.getTokenList().get(currentIndex).terminal, "int")) {
			this.int_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex).terminal, "if")) {
			this.if_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex).terminal, "else")) {
			this.else_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex).terminal, "else_if")) {
			this.else_if_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex).terminal, "print_line")) {
			this.print_line_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex).terminal, "identifier")) {
			if(!Objects.equals(tokenList.get(currentIndex - 1), "int")) {
				declaration_statement();
			}
		}
	}
	public void declaration_statement() {
		String identifier = tokenList.get(currentIndex).token;
		this.match(new TokenTerminal("identifier"));
		if(Objects.equals(tokenList.get(currentIndex).terminal, "statement_terminator") | Objects.equals(tokenList.get(currentIndex).terminal, "punctuation_comma")) {
			symbolTable.put(identifier, 0);
			this.line_terminator();
		} else {
			this.match(new TokenTerminal("assignment_operator"));
			int value = this.expression();
			symbolTable.put(identifier, value);
			this.line_terminator();
		}
	}
	public void declaration_statement_sequence() {
		this.declaration_statement();
		this.declaration_statement_tail();
	}
	public void declaration_statement_tail() {
		if (Objects.equals(tokenList.get(currentIndex).terminal, "punctuation_comma")) {
			this.match(new TokenTerminal("punctuation_comma"));
			this.declaration_statement_sequence();
		}
	}

	public void body_sequence_tail() {
		if (currentIndex < tokenList.size() && startsWithBodyElement(this.getTokenList().get(this.getCurrentIndex()))) {
			body_sequence();
		}
	}
	private boolean startsWithBodyElement(TokenTerminal terminal) {
		return Arrays.asList("int", "if", "else", "else_if", "print_line", "identifier").contains(terminal.terminal);
	}

	public void int_statement() {
		this.match(new TokenTerminal("int"));
		this.declaration_statement_sequence();
	}
	public void line_terminator() {
		if(Objects.equals(this.tokenList.get(currentIndex).terminal, "statement_terminator")) {
			this.match(new TokenTerminal("statement_terminator"));
		}else {
			this.match(new TokenTerminal("punctuation_comma"));
		}
	}

	public void if_statement() {
		this.match(new TokenTerminal("if"));
		this.if_conditional();
		this.match(new TokenTerminal("begin"));
		this.body_sequence();
		this.match(new TokenTerminal("end"));
	}
	public void else_if_statement() {
		this.match(new TokenTerminal("else_if"));
		this.if_conditional();
		this.match(new TokenTerminal("begin"));
		this.body_sequence();
		this.match(new TokenTerminal("end"));
	}
	public void if_conditional() {
		this.match(new TokenTerminal("left_parenthesis_operator"));
		this.if_argument();
		this.conditional_operator();
		this.if_argument();
		this.match(new TokenTerminal("right_parenthesis_operator"));
	}
	public void if_argument() {
		if(Objects.equals(this.tokenList.get(currentIndex).terminal, "number_literal")) {
			this.match(new TokenTerminal("number_literal"));
		} else {
			this.match(new TokenTerminal("identifier"));
		}
	}
	public void conditional_operator() {
		if(Objects.equals(this.tokenList.get(currentIndex).terminal, "less_than_operator")) {
			this.match(new TokenTerminal("less_than_operator"));
		} else {
			this.match(new TokenTerminal("greater_than_operator"));
		}
	}
	public void else_statement() {
		this.match(new TokenTerminal("else"));
		this.match(new TokenTerminal("begin"));
		this.body_sequence();
		this.match(new TokenTerminal("end"));
	}
	public void print_line_statement() {
		this.match(new TokenTerminal("print_line"));
		this.match(new TokenTerminal("left_parenthesis_operator"));
		if(tokenList.get(currentIndex).terminal.equals("string_literal")) {
			String str = tokenList.get(currentIndex).token;
			str = str.substring(1,str.length()-1);
			System.out.println(str);
			this.match(new TokenTerminal("string_literal"));

		} else if(tokenList.get(currentIndex).terminal.equals("identifier")) {
			System.out.println(symbolTable.get(tokenList.get(currentIndex).token));
			this.match(new TokenTerminal("identifier"));
		}
		this.match(new TokenTerminal("right_parenthesis_operator"));
		this.line_terminator();
	}
	public int expression() {
		int value = primary_expression();
		value = expression_tail(value);
		return value;
	}

	public int primary_expression() {
		if (tokenList.get(currentIndex).terminal.equals("number_literal")) {
			int value = Integer.parseInt(tokenList.get(currentIndex).token);
			match(new TokenTerminal("number_literal"));
			return value;
		} else if (tokenList.get(currentIndex).terminal.equals("identifier")) {
			String id = tokenList.get(currentIndex).token;
			match(new TokenTerminal("identifier"));
			return symbolTable.get(id);
		}
		return 0;
	}

	public int expression_tail(int left_value) {
		if (currentIndex < tokenList.size() && tokenList.get(currentIndex).terminal.equals("multiplication_operator")) {
			match(new TokenTerminal("multiplication_operator"));
			int right_value = expression();
			return left_value * right_value;
		}
		return left_value;
	}

	public static void main(String[] args) throws IOException {
		SmallLexer lexer = new SmallLexer();
		ArrayList<TokenTerminal> tokenList = SmallLexer.lexIntoTokenList(args[0]);
		RecurParser parser = new RecurParser(tokenList);
		int index = 0;
		for(TokenTerminal currentToken: tokenList) {
			System.out.print(index+ ":" + currentToken.token +  " "+ currentToken.terminal + "\n");
			index++;
		}
		parser.program_statement();

		System.out.println(" ");

		if(!parser.getError()) {
			System.out.println("Parsing OK");
		} else {
			System.out.println("Parsing failed");
		}
		if(parser.begin_count > parser.end_count) {
			System.out.println("end missing");
		} else if ( parser.end_count > parser.begin_count) {
			System.out.println("begin missing");
		}
	}
}