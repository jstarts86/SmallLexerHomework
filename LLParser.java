import java.io.IOException;
import java.util.*;

public class LLParser {

	private SmallLexer lexer;
	private String lookahead;
	private Stack<String> stack;





	public static void main(String[] args) throws IOException {
		SmallLexer lexer = new SmallLexer();
		ArrayList<String> tokenList = new ArrayList<>();
		tokenList = lexer.lexIntoTokenList("test1.txt");
		System.out.println(tokenList);
		Map<String, Terminal> terminals = new HashMap<>();
		Map<String, NonTerminal> nonTerminals = new HashMap<>();
		Map<String, Symbol> s = new HashMap<>();

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
		Production body_print = new Production((NonTerminal) s.get("Body"), List.of(s.get("Print_Statement")));
		Production body_dec = new Production((NonTerminal) s.get("Body"), List.of(s.get("Declaration_Statement")));
		Production line_Terminator = new Production((NonTerminal) s.get("Line_Terminator"), List.of(s.get("statement_terminator")));
		Production line_Terminator_comma = new Production((NonTerminal) s.get("Line_Terminator"), List.of(s.get("punctuation_comma")));
		Production int_Statement = new Production((NonTerminal) s.get("Int_Statement"), List.of(s.get("int"),s.get("Declaration_Statement")));
		Production declaration_Statement = new Production((NonTerminal) s.get("Declaration_Statement"), List.of(s.get("identifier"),s.get("assignment_operator"),s.get("Expression"), s.get("Line_Terminator")));
		Production declaration_Sequence = new Production((NonTerminal) s.get("Declaration_Sequence"), List.of(s.get("Declaration_Statement"),s.get("Declaration_Tail")));
		Production declaration_Tail = new Production((NonTerminal) s.get("Declaration_Sequence"), List.of(s.get("Declaration_Statement"),s.get("Declaration_Tail")));
		Production declaration_Tail_eps = new Production((NonTerminal) s.get("Declaration_Sequence"), List.of(s.get("eps")));
		Production if_Statement = new Production((NonTerminal) s.get("If_Statement"), List.of(s.get("if"), s.get("If_Conditional") ,s.get("begin"), s.get("Body_Sequence"), s.get("end")));

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


		Map<NonTerminal, Map<Terminal, Production>> parsingTable = new HashMap<>();

		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Program_Statement"), terminals.get("program"), program_Statement);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence"), terminals.get("int"), body_Sequence);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence"), terminals.get("if"), body_Sequence);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence"), terminals.get("else"), body_Sequence);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence"), terminals.get("print_line"), body_Sequence);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence"), terminals.get("identifier"), body_Sequence);

		// Body Sequence
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence_Tail"), terminals.get("int"), body_Sequence_Tail);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence_Tail"), terminals.get("if"), body_Sequence_Tail);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence_Tail"), terminals.get("else"), body_Sequence_Tail);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence_Tail"), terminals.get("print_line"), body_Sequence_Tail);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence_Tail"), terminals.get("identifier"), body_Sequence_Tail);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body_Sequence_Tail"), terminals.get("eps"), body_Sequence_Tail_eps);

		//Body
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body"), terminals.get("int"), body_int);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body"), terminals.get("if"), body_if);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body"), terminals.get("else"), body_else);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body"), terminals.get("print_line"), body_print);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Body"), terminals.get("identifier"), body_dec);

		//line terminator

		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Line_Terminator"), terminals.get("statement_terminator"), line_Terminator);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Line_Terminator"), terminals.get("punctuation_comma"), line_Terminator_comma);

		//Int Statement

		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Int_Statement"), terminals.get("int"), int_Statement);

		// Declaration Statement

		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Declaration_Statement"), terminals.get("identifier"), declaration_Statement);

		//Declaration Sequence
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Declaration_Sequence"), terminals.get("identifier"), declaration_Sequence);

		//Declaration Tail
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Declaration_Tail"), terminals.get("int"), declaration_Tail_eps);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Declaration_Tail"), terminals.get("if"), declaration_Tail_eps);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Declaration_Tail"), terminals.get("else"), declaration_Tail_eps);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Declaration_Tail"), terminals.get("print_line"), declaration_Tail_eps);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Declaration_Tail"), terminals.get("identifier"), declaration_Tail);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Declaration_Tail"), terminals.get("end"), declaration_Tail_eps);

		//if statement
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("If_Statement"), terminals.get("if"), if_Statement );

		//if conditional
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("If_Conditional"), terminals.get("left_parenthesis_operator"), if_Conditional );

		//if Argument
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("If_Argument"), terminals.get("identifier"), if_Argument_id );
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("If_Argument"), terminals.get("number_literal"), if_Argument_number);

		//Conditional Operator

		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Conditional_Operator"), terminals.get("less_than_operator"), conditional_Operator_less);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Conditional_Operator"), terminals.get("greater_than_operator"), conditional_Operator_great);

		//else than
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Else_Statement"), terminals.get("else"), else_Statement);

		//print statement

		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Print_Statement"), terminals.get("print_line"), print_Statement);

		//Expression

		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Expression"), terminals.get("identifier"), expression);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Expression"), terminals.get("number_literal"), expression);

		//Primary Expression

		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Primary_Expression"), terminals.get("identifer"), primary_Expression_id);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Primary_Expression"), terminals.get("number_literal"), primary_Expression_number);

		//Expression Tail
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Expression_Tail"), terminals.get("punctuation_comma"), expression_Tail_eps);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Expression_Tail"), terminals.get("statement_terminator"), expression_Tail_eps);
		LLParser.addToParsingTable(parsingTable,nonTerminals.get("Expression_Tail"), terminals.get("multiplication_operator"), expression_Tail_mult);


	}
	public static void addToParsingTable(Map<NonTerminal, Map<Terminal, Production>> parsingTable, NonTerminal nt, Terminal t, Production p) {
            parsingTable.computeIfAbsent(nt, k -> new HashMap<>()).put(t, p);
        }

}
