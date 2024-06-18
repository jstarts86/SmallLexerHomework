import java.util.ArrayList;
import java.util.List;

public class Symbol {
	String name;
	public static List<String> terminalList = List.of("program", "int", "if",
			"else", "print_line", "identifier", "statement_terminator",
			"punctuation_comma", "display_line", "for", "while",
			"left_parenthesis_operator", "else_if",
			"assignment_operator", "right_parenthesis_operator","integer" , "number_literal", "multiplication_operator", "begin", "end", "less_than_operator", "greater_than_operator",
			"string_literal",
			"if_argument","eps", "$");
	public static List<String> nonTerminalList = List.of("Program_Statement", "Body_Sequence", "Body_Sequence_Tail", "Body", "Line_Terminator", "Int_Statement",
			"Declaration_Statement", "Declaration_Sequence",
			"Declaration_End", "Declaration_Tail", "If_Statement",
			"If_Conditional", "If_Argument", "Conditional_Operator",
			"Else_Statement", "Print_Statement", "Display_Statement",
			"Expression", "Primary_Expression", "Expression_Tail","Else_If_Statement");

public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public Symbol(String name) {
		this.name = name;
	}
}
