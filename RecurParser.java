import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RecurParser {
	private ArrayList<String> tokenList = new ArrayList<>();
	private int currentIndex = 0;
	private int lookAheadIndex = 1;
	private Boolean isError = false;
	private int begin_count;
	private int end_count;
	public RecurParser(ArrayList<String> tokenList) {
		this.tokenList = tokenList;
	}

	public ArrayList<String> getTokenList() {
		return tokenList;
	}

	public void setTokenList(ArrayList<String> tokenList) {
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

	public void match(String token) {
		if (currentIndex < tokenList.size() && Objects.equals(this.getTokenList().get(this.getCurrentIndex()), token)) {
			if(Objects.equals(token, "begin")) {
				begin_count++;
			} if(Objects.equals(token, "end")) {
				end_count++;
			}
			this.setCurrentIndex(this.getCurrentIndex() + 1);
			this.setLookAheadIndex(this.getLookAheadIndex() + 1);
		} else {
			this.setError(true);
		}
	}

	public void program_statement(){
		this.match("program");
		this.match("identifier");
		this.match("begin");
		this.body_sequence();
		this.match("end");
	}
	public void body_sequence() {
		this.body();
		this.body_sequence_tail();
	}

	public void body() {
		if(Objects.equals(this.getTokenList().get(currentIndex), "int")) {
			this.int_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex), "if")) {
			this.if_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex), "else")) {
			this.else_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex), "else_if")) {
			this.else_if_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex), "print_line")) {
			this.print_line_statement();
		}
		else if(Objects.equals(this.getTokenList().get(currentIndex), "identifier")) {
			if(!Objects.equals(tokenList.get(currentIndex - 1), "int")) {
				declaration_statement();
			}
		}
	}
	public void declaration_statement() {
			this.match("identifier");
			if(Objects.equals(tokenList.get(currentIndex), "statement_terminator") | Objects.equals(tokenList.get(currentIndex), "punctuation_comma")) {
				this.line_terminator();
			} else {
				this.match("assignment_operator");
				this.expression();
				this.line_terminator();
			}
	}
	public void declaration_statement_sequence() {
		this.declaration_statement();
		this.declaration_statement_tail();
	}
	public void declaration_statement_tail() {
		if (Objects.equals(tokenList.get(currentIndex), "punctuation_comma")) {
			this.match("punctuation_comma");
			this.declaration_statement_sequence();
		}
	}

	public void body_sequence_tail() {
		if (currentIndex < tokenList.size() && startsWithBodyElement(this.getTokenList().get(this.getCurrentIndex()))) {
			body_sequence();
		}
	}
	private boolean startsWithBodyElement(String token) {
		return Arrays.asList("int", "if", "else", "else_if", "print_line", "identifier").contains(token);
	}

	public void int_statement() {
		this.match("int");
		this.declaration_statement_sequence();
	}
	public void line_terminator() {
		if(Objects.equals(this.tokenList.get(currentIndex), "statement_terminator")) {
			this.match("statement_terminator");
		}else {
			this.match("punctuation_comma");
		}
	}

	public void if_statement() {
		this.match("if");
		this.if_conditional();
		this.match("begin");
		this.body_sequence();
		this.match("end");
	}
	public void else_if_statement() {
		this.match("else_if");
		this.if_conditional();
		this.match("begin");
		this.body_sequence();
		this.match("end");
	}
	public void if_conditional() {
		this.match("left_parenthesis_operator");
		this.if_argument();
		this.conditional_operator();
		this.if_argument();
		this.match("right_parenthesis_operator");
	}
	public void if_argument() {
		if(Objects.equals(this.tokenList.get(currentIndex), "number_literal")) {
			this.match("number_literal");
		} else {
			this.match("identifier");
		}
	}
	public void conditional_operator() {
		if(Objects.equals(this.tokenList.get(currentIndex), "less_than_operator")) {
			this.match("less_than_operator");
		} else {
			this.match("greater_than_operator");
		}
	}
	public void else_statement() {
		this.match("else");
		this.match("begin");
		this.body_sequence();
		this.match("end");
	}
	public void print_line_statement() {
		this.match("print_line");
		this.match("left_parenthesis_operator");
		if(tokenList.get(currentIndex).equals("string_literal")) {
			this.match("string_literal");
		} else if(tokenList.get(currentIndex).equals("identifier")) {
			this.match("identifier");
		}
		this.match("right_parenthesis_operator");
		this.line_terminator();
	}
	public void expression() {
		primary_expression();
		expression_tail();
	}

	public void primary_expression() {
		if (tokenList.get(currentIndex).equals("number_literal")) {
			match("number_literal");
		} else if (tokenList.get(currentIndex).equals("identifier")) {
			match("identifier");
		}
	}

	public void expression_tail() {
		if (currentIndex < tokenList.size() && tokenList.get(currentIndex).equals("multiplication_operator")) {
			match("multiplication_operator");
			expression();
		}
	}

	public static void main(String[] args) throws IOException {
		SmallLexer lexer = new SmallLexer();
		ArrayList<String> tokenList = SmallLexer.lexIntoTokenList(args[0]);
		RecurParser parser = new RecurParser(tokenList);
		System.out.print(tokenList);
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