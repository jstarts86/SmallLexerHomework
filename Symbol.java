import java.util.ArrayList;
import java.util.List;

public class Symbol {
	String name;
	public static List<String> terminalList = List.of("program", "int", "if", "else", "print_line", "identifier", "statement_terminator", "punctuation_comma",
			"left_parenthesis_operator",
			"assignment_operator", "right_parenthesis_operator", "number_literal", "multiplication_operator", "begin", "end", "less_than_operator", "greater_than_operator", "string_literal",
			"if_argument","/eps", "$");
	public static List<String> nonTerminalList = List.of("Program_Statement", "Body_Sequence", "Body_Sequence_Tail", "Body", "Line_Terminator", "Int_Statement",
			"Declaration_Statement", "Declaration_Sequence", "Declaration_Tail", "If_Statement", "If_Conditional", "If_Argument", "Conditional_Operator", "Else_Statement", "Print_Statement",
			"Expression", "Primary_Expression", "Expression_Tail");

	NonTerminal Program_Statement = new NonTerminal("Program_Statement");
	NonTerminal Body_Sequence = new NonTerminal("Body_Sequence");
	NonTerminal Body_Sequence_Tail = new NonTerminal("Body_Sequence_Tail");
	NonTerminal Body = new NonTerminal("Body");
	NonTerminal Line_Terminator = new NonTerminal("Line_Terminator");
	NonTerminal Int_Statement = new NonTerminal("Int_Statement");
	NonTerminal Delcaration_Statement = new NonTerminal("Delcaration_Statement");
	NonTerminal Declaration_Sequence = new NonTerminal("Declaration_Sequence");
	NonTerminal Declaration_Tail = new NonTerminal("Declaration_Tail");
	NonTerminal If_Statment = new NonTerminal("If_Statment");
	NonTerminal If_Conditional = new NonTerminal("If_Conditional");
	NonTerminal If_Argument = new NonTerminal("If_Argument");
	NonTerminal Else_Statment = new NonTerminal("Else_Statement");
	NonTerminal Print_Statement = new NonTerminal("Print_Statement");
	NonTerminal Expression = new NonTerminal("Expression");
	NonTerminal Primary_Expression = new NonTerminal("Primary_Expression");
	NonTerminal Expression_Tail = new NonTerminal("Expression_Tail");
	NonTerminal Conditional_Operator = new NonTerminal("Conditional_Operator");

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
