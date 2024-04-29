import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SmallParser {
	private ArrayList<String> tokenList = new ArrayList<>();
	private int currentIndex = 0;
	private int lookAheadIndex = 1;
	private Boolean isError = false;

	public SmallParser(ArrayList<String> tokenList) {
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
		if (Objects.equals(this.getTokenList().get(this.getCurrentIndex()), token)) {
			this.setCurrentIndex(this.getCurrentIndex() + 1);
			this.setLookAheadIndex(this.getLookAheadIndex() + 1);
		} else {

			System.out.println(" ");
			System.out.println("Expected " + token);
			System.out.println("Missing " + this.getTokenList().get(this.getCurrentIndex()));
			System.out.println("Index " + this.currentIndex);
			this.setError(true);
			return;
		}
	}

	public void program_statement(){
		this.match("program");
		this.match("identifier");
		this.match("begin");
		this.body_sequence();
		this.match("end");
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
	public void body_sequence_tail() {
		body_sequence();
	}

	public void body_sequence() {
		while(currentIndex < tokenList.size() && !getError() && !Objects.equals(tokenList.get(currentIndex), "end")) {
			body();
		}
	}
	public void declaration_statement() {
			this.match("identifier");
			if(!getError() && Objects.equals(tokenList.get(currentIndex), "assignment_operator")) {
				this.match("assignment_operator");
				this.match("number_literal");
			}
			if(currentIndex < tokenList.size() && Objects.equals(tokenList.get(currentIndex), "punctuation_comma") && !getError() && Objects.equals(tokenList.get(currentIndex),
					"statement_terminator")) {
				this.line_terminator();
			}
	}
	public void declaration_statement_sequence() {
		while (currentIndex < tokenList.size() && Objects.equals(tokenList.get(currentIndex), ",") && !getError()) {
			this.declaration_statement();
		}
	}


	public void int_statement() {
		this.match("int");
		if(!getError()) {
			this.declaration_statement_sequence();
		}
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
		this.match("string_literal");
		this.match("right_parenthesis_operator");
		this.line_terminator();
	}

	public static void main(String[] args) throws IOException {
		SmallLexer lexer = new SmallLexer();
		ArrayList<String> tokenList = SmallLexer.lexIntoTokenList("test1.txt");
		SmallParser parser = new SmallParser(tokenList);
		parser.program_statement();
		if(!parser.getError()) {
			System.out.println("Parsing successful");
		} else {
			System.out.println("Parsing failed");
		}

	}
}
