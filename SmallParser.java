import java.util.ArrayList;
import java.util.Objects;

public class SmallParser {
	private ArrayList<String> tokenList = new ArrayList<>();
	private int currentIndex = 0;
	private int lookAheadIndex = 1;
	private Boolean isError;

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
			System.out.println("Missing" + this.getTokenList().get(this.getCurrentIndex()));
			this.setError(false);
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
		if(Objects.equals(this.getTokenList().get(lookAheadIndex), "int")) {
			this.int_statement_sequence();
		}
		if(Objects.equals(this.getTokenList().get(lookAheadIndex), "if")) {
			this.if_statement();
		}
		if(Objects.equals(this.getTokenList().get(lookAheadIndex), "else")) {
			this.else_statement();
		}
		if(Objects.equals(this.getTokenList().get(lookAheadIndex), "else_if")) {
			this.else_if_statement();
		}
		if(Objects.equals(this.getTokenList().get(lookAheadIndex), "print_line")) {
			this.print_line_statement();
		}
	}
	public void body_sequence_tail() {
		if(Objects.equals(this.getTokenList().get(lookAheadIndex), "statement_terminator")) {
			this.match("statement_terminator");
		}
	}
	public void int_statement_sequence() {
		this.int_statement();
		this.int_statement_tail();
	}
	public void int_statement() {
		this.match("int");
		this.match("identifier");
		this.match("assignment_operator");
		this.match("number_literal");
		if(Objects.equals(this.getTokenList().get(currentIndex), "punctuation_comma")) {
			this.match("comma");
		} else {
			this.match("statement_terminator");
		}
	}
	public void int_statement_tail() {
		if(Objects.equals(this.tokenList.get(lookAheadIndex), "int")) {
			this.int_statement_sequence();
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
		this.match("left_paranthesis_operator");
		this.if_argument();
		this.conditional_operator();
		this.if_argument();
		this.match("right_paranthesis_operator");
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
		this.match("left_paranthesis_operator");
		this.match("string_literal");
		this.match("right_paranthesis_operator");
		this.match("statement_terminator");
	}


}
