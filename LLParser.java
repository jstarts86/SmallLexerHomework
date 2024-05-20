import java.io.IOException;
import java.util.*;

public class LLParser {

	private SmallLexer lexer;
	private String lookahead;
	private Stack<String> stack;





	public static void main(String[] args) throws IOException {
		SmallLexer lexer = new SmallLexer();
		ArrayList<TokenTerminal> tokenList = lexer.lexIntoTokenList("test1.txt");
		//System.out.println(tokenList);
//		for(TokenTerminal tok: tokenList) {
//			System.out.print(tok.getTerminal() + " and ");
//			System.out.print(tok.getToken() + ", ");
//
//		}
		Map<String, Terminal> terminals = new HashMap<>();
		Map<String, NonTerminal> nonTerminals = new HashMap<>();
		Map<String, Symbol> s = new HashMap<>();
		String lookahead;
		Stack<Symbol> stack = new Stack<>();
		stack.push(new Terminal("$"));
		stack.push(new NonTerminal("Program_Statement"));



		for (String terminalName : Symbol.terminalList) {
			Terminal terminal = new Terminal(terminalName);
			terminals.put(terminalName,terminal);
			s.put(terminalName,terminal);
		}
		for (String nonTerminalName : Symbol.nonTerminalList) {
			NonTerminal nonTerminal = new NonTerminal(nonTerminalName);
			nonTerminals.put(nonTerminalName,nonTerminal);
			s.put(nonTerminalName,nonTerminal);
		}
		Production program_Statement = new Production((NonTerminal) s.get("Program_Statement"), List.of(s.get("program"),s.get("identifier"), s.get("begin"), s.get("Body_Sequence"), s.get("end")));
		Production body_Sequence = new Production((NonTerminal) s.get("Body_Sequence"), List.of(s.get("Body"), s.get("Body_Sequence_Tail")));
		Production body_Sequence_Tail = new Production((NonTerminal) s.get("Body_Sequence_Tail"), List.of(s.get("Body_Sequence")));
		Production body_Sequence_Tail_eps = new Production((NonTerminal) s.get("Body_Sequence_Tail"), List.of(s.get("eps")));
		Production body_int = new Production((NonTerminal) s.get("Body"), List.of(s.get("Int_Statement")));

		Production body_if = new Production((NonTerminal) s.get("Body"), List.of(s.get("If_Statement")));
		Production body_else = new Production((NonTerminal) s.get("Body"), List.of(s.get("Else_Statement")));
		Production body_else_if = new Production((NonTerminal) s.get("Body"), List.of(s.get("Else_If_Statement")));
		Production body_print = new Production((NonTerminal) s.get("Body"), List.of(s.get("Print_Statement")));
		Production body_dec = new Production((NonTerminal) s.get("Body"), List.of(s.get("Declaration_Statement")));
		Production line_Terminator = new Production((NonTerminal) s.get("Line_Terminator"), List.of(s.get("statement_terminator")));
		Production line_Terminator_comma = new Production((NonTerminal) s.get("Line_Terminator"), List.of(s.get("punctuation_comma")));
		Production int_Statement = new Production((NonTerminal) s.get("Int_Statement"), List.of(s.get("int"),s.get("Declaration_Statement")));
		Production declaration_Statement = new Production((NonTerminal) s.get("Declaration_Statement"), List.of(s.get("identifier"), s.get("Declaration_End")));

		Production declaration_End_line_term = new Production((NonTerminal) s.get("Declaration_End"), List.of( s.get("Line_Terminator")));
		Production declaration_End_equal = new Production((NonTerminal) s.get("Declaration_End"), List.of(s.get("assignment_operator"), s.get("Expression"), s.get("Line_Terminator")));
		Production declaration_Sequence = new Production((NonTerminal) s.get("Declaration_Sequence"), List.of(s.get("Declaration_Statement"),s.get("Declaration_Tail")));
		Production declaration_Tail = new Production((NonTerminal) s.get("Declaration_Sequence"), List.of(s.get("Declaration_Statement"),s.get("Declaration_Tail")));
		Production declaration_Tail_eps = new Production((NonTerminal) s.get("Declaration_Sequence"), List.of(s.get("eps")));
		Production if_Statement = new Production((NonTerminal) s.get("If_Statement"), List.of(s.get("if"), s.get("If_Conditional") ,s.get("begin"), s.get("Body_Sequence"), s.get("end")));
		Production else_If_Statement = new Production((NonTerminal) s.get("Else_If_Statement"), List.of(s.get("else_if"), s.get("If_Conditional") ,s.get("begin"), s.get("Body_Sequence"), s.get("end")));

		Production if_Conditional = new Production((NonTerminal) s.get("If_Conditional"), List.of(s.get("left_parenthesis_operator"), s.get("If_Argument") ,s.get("Conditional_Operator"), s.get(
				"If_Argument"),
				s.get("right_parenthesis_operator")));

		Production if_Argument_id = new Production((NonTerminal) s.get("If_Argument"), List.of(s.get("identifier")));
		Production if_Argument_number = new Production((NonTerminal) s.get("If_Argument"), List.of(s.get("number_literal")));
		Production conditional_Operator_less = new Production((NonTerminal) s.get("Conditional_Operator"), List.of(s.get("less_than_operator")));
		Production conditional_Operator_great = new Production((NonTerminal) s.get("Conditional_Operator"), List.of(s.get("greater_than_operator")));
		Production else_Statement = new Production((NonTerminal) s.get("Else_Statement"), List.of(s.get("else"), s.get("begin") ,s.get("Body_Sequence"), s.get("end")));
		Production print_Statement = new Production((NonTerminal) s.get("Print_Statement"), List.of(s.get("print_line"), s.get("left_parenthesis_operator") ,s.get("string_literal"), s.get(
				"right_parenthesis_operator"),s.get("Line_Terminator")));

		Production expression = new Production((NonTerminal) s.get("Expression"), List.of(s.get("Primary_Expression"),s.get("Expression_Tail")));

		Production primary_Expression_id = new Production((NonTerminal) s.get("Primary_Expression"), List.of(s.get("identifier")));
		Production primary_Expression_number = new Production((NonTerminal) s.get("Primary_Expression"), List.of(s.get("number_literal")));

		Production expression_Tail_eps = new Production((NonTerminal) s.get("Expression"), List.of(s.get("eps")));
		Production expression_Tail_mult = new Production((NonTerminal) s.get("Expression"), List.of(s.get("multiplication_operator"),s.get("Expression")));


        // Parsing table entries
        Map<String, Map<String, Production>> parsingTable = new HashMap<>();
        addToParsingTable(parsingTable, "Program_Statement", "program", program_Statement);
        addToParsingTable(parsingTable, "Body_Sequence", "int", body_Sequence);
        addToParsingTable(parsingTable, "Body_Sequence", "if", body_Sequence);
        addToParsingTable(parsingTable, "Body_Sequence", "else", body_Sequence);
		addToParsingTable(parsingTable, "Body_Sequence", "else_if", body_Sequence);
        addToParsingTable(parsingTable, "Body_Sequence", "print_line", body_Sequence);
        addToParsingTable(parsingTable, "Body_Sequence", "identifier", body_Sequence);

        // Body Sequence Tail
        addToParsingTable(parsingTable, "Body_Sequence_Tail", "int", body_Sequence_Tail);
        addToParsingTable(parsingTable, "Body_Sequence_Tail", "if", body_Sequence_Tail);
        addToParsingTable(parsingTable, "Body_Sequence_Tail", "else", body_Sequence_Tail);
		addToParsingTable(parsingTable, "Body_Sequence_Tail", "else_if", body_Sequence_Tail);
        addToParsingTable(parsingTable, "Body_Sequence_Tail", "print_line", body_Sequence_Tail);
        addToParsingTable(parsingTable, "Body_Sequence_Tail", "identifier", body_Sequence_Tail);
		addToParsingTable(parsingTable, "Body_Sequence_Tail", "end", body_Sequence_Tail);
        addToParsingTable(parsingTable, "Body_Sequence_Tail", "eps", body_Sequence_Tail_eps);

        // Body
        addToParsingTable(parsingTable, "Body", "int", body_int);
        addToParsingTable(parsingTable, "Body", "if", body_if);
        addToParsingTable(parsingTable, "Body", "else", body_else);
		addToParsingTable(parsingTable, "Body", "else_if", body_else_if);
        addToParsingTable(parsingTable, "Body", "print_line", body_print);
        addToParsingTable(parsingTable, "Body", "identifier", body_dec);

        // Line Terminator
        addToParsingTable(parsingTable, "Line_Terminator", "statement_terminator", line_Terminator);
        addToParsingTable(parsingTable, "Line_Terminator", "punctuation_comma", line_Terminator_comma);

        // Int Statement
        addToParsingTable(parsingTable, "Int_Statement", "int", int_Statement);

        // Declaration Statement
        addToParsingTable(parsingTable, "Declaration_Statement", "identifier", declaration_Statement);

		// Declaration End
		addToParsingTable(parsingTable, "Declaration_End", "assignment_operator", declaration_End_equal);
		addToParsingTable(parsingTable, "Declaration_End", "punctuation_comma", declaration_End_line_term);
		addToParsingTable(parsingTable, "Declaration_End", "statement_terminator", declaration_End_line_term);
        // Declaration Sequence
        addToParsingTable(parsingTable, "Declaration_Sequence", "identifier", declaration_Sequence);

        // Declaration Tail
        addToParsingTable(parsingTable, "Declaration_Tail", "int", declaration_Tail_eps);
        addToParsingTable(parsingTable, "Declaration_Tail", "if", declaration_Tail_eps);
		addToParsingTable(parsingTable, "Declaration_Tail", "else_if", declaration_Tail_eps);
        addToParsingTable(parsingTable, "Declaration_Tail", "else", declaration_Tail_eps);
        addToParsingTable(parsingTable, "Declaration_Tail", "print_line", declaration_Tail_eps);
        addToParsingTable(parsingTable, "Declaration_Tail", "identifier", declaration_Tail);
        addToParsingTable(parsingTable, "Declaration_Tail", "end", declaration_Tail_eps);

        // If Statement
        addToParsingTable(parsingTable, "If_Statement", "if", if_Statement);

        // If Conditional
        addToParsingTable(parsingTable, "If_Conditional", "left_parenthesis_operator", if_Conditional);

        // If Argument
        addToParsingTable(parsingTable, "If_Argument", "identifier", if_Argument_id);
        addToParsingTable(parsingTable, "If_Argument", "number_literal", if_Argument_number);

        // Conditional Operator
        addToParsingTable(parsingTable, "Conditional_Operator", "less_than_operator", conditional_Operator_less);
        addToParsingTable(parsingTable, "Conditional_Operator", "greater_than_operator", conditional_Operator_great);

        // Else Statement
        addToParsingTable(parsingTable, "Else_Statement", "else", else_Statement);

		// Else if Statement
		addToParsingTable(parsingTable, "Else_If_Statement", "else_if", else_If_Statement);
        // Print Statement
        addToParsingTable(parsingTable, "Print_Statement", "print_line", print_Statement);

        // Expression
        addToParsingTable(parsingTable, "Expression", "identifier", expression);
        addToParsingTable(parsingTable, "Expression", "number_literal", expression);

        // Primary Expression
        addToParsingTable(parsingTable, "Primary_Expression", "identifier", primary_Expression_id);
        addToParsingTable(parsingTable, "Primary_Expression", "number_literal", primary_Expression_number);

        // Expression Tail
        addToParsingTable(parsingTable, "Expression_Tail", "punctuation_comma", expression_Tail_eps);
        addToParsingTable(parsingTable, "Expression_Tail", "statement_terminator", expression_Tail_eps);
        addToParsingTable(parsingTable, "Expression_Tail", "multiplication_operator", expression_Tail_mult);

		parse(parsingTable,stack,tokenList);

	}
    public static void addToParsingTable(Map<String, Map<String, Production>> parsingTable, String nonTerminal, String terminal, Production production) {
        parsingTable.computeIfAbsent(nonTerminal, k -> new HashMap<>()).put(terminal, production);
    }

	public static void parse(Map<String, Map<String, Production>> parsingTable, Stack<Symbol> stack, List<TokenTerminal> tokenList) {
		int round = 1;
		while(round < 2) {
			int current_index = 0;
			ArrayList<Terminal> terminalsList = new ArrayList<>();
			if(round == 1) {
				System.out.println(stack);
			}
			for(int i = 0; i < tokenList.size(); i++) {
				Terminal term = new Terminal( tokenList.get(i).getTerminal());
				terminalsList.add(term);
			}
			if( round == 0 ) {
				System.out.println("[GENERATE-stack] " + stack);
			}
			while(!Objects.equals(stack.peek().getName(), "$") && !Objects.equals(terminalsList.get(current_index).getName(), "$")) {
				Symbol top = stack.peek();
				Terminal currentToken = terminalsList.get(current_index);
				if(current_index == 30) {
					System.out.println(stack);
					System.out.println(terminalsList.get(current_index).getName());
				}
				if(top instanceof Terminal) {
					if(Objects.equals(top.getName(), "eps")) {
						stack.pop();
						if(round == 0) {
							System.out.println("[MATCH] - " + tokenList.get(current_index).getTerminal() + " - " + tokenList.get(current_index).getToken());
						} else if( round == 1) {
							System.out.println(stack);
						}
					} else {
						match(stack,top,currentToken);
						if(round == 0) {
							System.out.println("[MATCH] - " + tokenList.get(current_index).getTerminal() + " - " + tokenList.get(current_index).getToken());
						} else if( round == 1) {
							System.out.println(stack);
						}
						current_index++;
						System.out.println("Index = " + current_index);
						if(current_index == 30) {
							System.out.print("here");
						}
					}
				} else if (top instanceof NonTerminal) {
					if(round == 0) {
						System.out.println("[GENERATE-stack] " + stack);
					}
					Map<String,Production> parsingEntry = parsingTable.get(top.getName());
					Production currentProduction = parsingEntry.get(currentToken.getName());
					stack.pop();
					for(int i = currentProduction.rightHand.size() - 1; i >= 0; i--) {
						stack.push(currentProduction.rightHand.get(i));
						if(round == 1) {
							System.out.println(stack);
						}
					}
				}
			}
			if(Objects.equals(stack.peek().getName(), "$") && Objects.equals(terminalsList.get(current_index).getName(), "$")) {

			} else {

			}
			round++;
		}



	}
	private static void match(Stack<Symbol> stack, Symbol top, Terminal currentToken) {
        if (top.getName().equals(currentToken.getName())) {
            stack.pop();
        } else {
            //error("Terminal mismatch: " + top.getName() + " != " + currentToken.getName());
            throw new RuntimeException("Parsing error");
        }
    }
	private static void error(Stack<Symbol> stack, Symbol top, Terminal currentToken) {

	}

	private void printStack(Stack<Symbol> stack) {

	}
}
