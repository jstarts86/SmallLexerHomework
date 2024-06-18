import java.io.IOException;
import java.util.*;

public class LRParser {
	public LRParser() {
	}

    public void parse(LRTable lrTable, List<TokenTerminal> tokens, List<Production> productions) {
        Stack<Integer> stateStack = new Stack<>();
        stateStack.push(0);
        Stack<TokenTerminal> symbolStack = new Stack<>();
        int tokenIndex = 0;

        while (true) {
            int currentState = stateStack.peek();

            if (tokenIndex >= tokens.size()) {
                throw new RuntimeException("Unexpected end of input");
            }

            TokenTerminal currentToken = tokens.get(tokenIndex);
            Action action = lrTable.getAction(currentState, currentToken.getTerminal());

            if (action == null) {
                throw new RuntimeException("Syntax error at token: " + currentToken.getToken() + " in state: " + currentState);
            }

            System.out.println("State Stack: " + stateStack);
            System.out.println("Symbol Stack: " + symbolStack);
            System.out.println("Current State: S " + currentState);
            System.out.println("Current Token: " + currentToken.getTerminal());
            System.out.println("Action: " + action.actionType + " " + (action.state != -1 ? "S" + action.state : action.production));

            switch (action.actionType) {
                case "SHIFT":
                    System.out.println("[S] S" + currentState + " " + currentToken.getTerminal());
                    stateStack.push(action.state);
                    symbolStack.push(currentToken);
                    tokenIndex++;
                    break;
                case "REDUCE":
                    applyReduceAction(action, productions, stateStack, symbolStack, lrTable);
                    break;
                case "ACCEPT":
                    System.out.println("Accepted");
                    return;
            }
        }
    }

     private void applyReduceAction(Action action, List<Production> productions, Stack<Integer> stateStack, Stack<TokenTerminal> symbolStack, LRTable lrTable) {
        Production production = productions.get(Integer.parseInt(action.production));
        int rhsLength = (int) production.rightHand.stream().filter(symbol -> !symbol.getName().equals("eps")).count();

        for (int i = 0; i < rhsLength; i++) {
            stateStack.pop();
            symbolStack.pop();
        }

        int currentState = stateStack.peek();
        Integer nextState = lrTable.getGoto(currentState, production.leftHand.getName());

        if (nextState == null) {
            throw new RuntimeException("No transition for non-terminal: " + production.leftHand.getName() + " in state: " + currentState);
        }

        stateStack.push(nextState);
        symbolStack.push(new TokenTerminal(production.leftHand.getName(), production.leftHand.getName()));
        System.out.println("[R] " + production.leftHand.getName() + " -> " + production.rightHand);
    }

	public static void main(String[] args) throws IOException {
		SmallLexer lexer = new SmallLexer();
		ArrayList<TokenTerminal> tokenList = lexer.lexIntoTokenList("test1.txt");
		List<Production> productions = new ArrayList<Production>();
	        productions.add(new Production(
            new NonTerminal("Program_Statement'"),
            Arrays.asList(new NonTerminal("Program_Statement"))
        ));
//        productions.add(new Production(new NonTerminal("Program_Statement'"), Arrays.asList(new NonTerminal("Program_Statement"))));
        productions.add(new Production(
            new NonTerminal("Program_Statement"),
            Arrays.asList(new Terminal("program"), new Terminal("identifier"), new Terminal("begin"), new NonTerminal("Body_Sequence"), new Terminal("end"))
        ));
        productions.add(new Production(
            new NonTerminal("Body_Sequence"),
            Arrays.asList(new NonTerminal("Body"), new NonTerminal("Body_Sequence_Tail"))
        ));
        productions.add(new Production(
            new NonTerminal("Body_Sequence_Tail"),
            Arrays.asList(new NonTerminal("Body_Sequence"))
        ));
        productions.add(new Production(
            new NonTerminal("Body_Sequence_Tail"),
            Arrays.asList(new Terminal("eps"))
        ));
        productions.add(new Production(
            new NonTerminal("Body"),
            Arrays.asList(new NonTerminal("If_Statement"))
        ));
        productions.add(new Production(
            new NonTerminal("Body"),
            Arrays.asList(new NonTerminal("Else_Statement"))
        ));
        productions.add(new Production(
            new NonTerminal("Body"),
            Arrays.asList(new NonTerminal("Print_Statement"))
        ));
        productions.add(new Production(
            new NonTerminal("Body"),
            Arrays.asList(new NonTerminal("Declaration_Sequence"))
        ));
        productions.add(new Production(
            new NonTerminal("Body"),
            Arrays.asList(new NonTerminal("Display_Statement"))
        ));
        productions.add(new Production(
            new NonTerminal("Body"),
            Arrays.asList(new NonTerminal("For_Statement"))
        ));
        productions.add(new Production(
            new NonTerminal("Body"),
            Arrays.asList(new NonTerminal("While_Statement"))
        ));
        productions.add(new Production(
            new NonTerminal("Line_Terminator"),
            Arrays.asList(new Terminal("statement_terminator"))
        ));
        productions.add(new Production(
            new NonTerminal("Line_Terminator"),
            Arrays.asList(new Terminal("punctuation_comma"))
        ));
        productions.add(new Production(
            new NonTerminal("Declaration_Sequence"),
            Arrays.asList(new NonTerminal("Declaration_Statement"), new NonTerminal("Declaration_Sequence_Tail"))
        ));
        productions.add(new Production(
            new NonTerminal("Declaration_Sequence_Tail"),
            Arrays.asList(new Terminal("punctuation_comma"), new NonTerminal("Declaration_Sequence"))
        ));
        productions.add(new Production(
            new NonTerminal("Declaration_Sequence_Tail"),
            Arrays.asList(new Terminal("statement_terminator"))
        ));
        productions.add(new Production(
            new NonTerminal("Declaration_Statement"),
            Arrays.asList(new Terminal("int"), new Terminal("identifier"))
        ));
        productions.add(new Production(
            new NonTerminal("Declaration_Statement"),
            Arrays.asList(new Terminal("int"), new Terminal("identifier"), new Terminal("assignment_operator"), new NonTerminal("Expression"))
        ));
        productions.add(new Production(
            new NonTerminal("Declaration_Statement"),
            Arrays.asList(new Terminal("identifier"), new Terminal("assignment_operator"), new NonTerminal("Expression"))
        ));
        productions.add(new Production(
            new NonTerminal("Declaration_Statement"),
            Arrays.asList(new Terminal("identifier"))
        ));
        productions.add(new Production(
            new NonTerminal("If_Statement"),
            Arrays.asList(new Terminal("if"), new NonTerminal("If_Conditional"), new Terminal("begin"), new NonTerminal("Body_Sequence"), new Terminal("end"))
        ));
        productions.add(new Production(
            new NonTerminal("If_Conditional"),
            Arrays.asList(new Terminal("left_parenthesis_operator"), new NonTerminal("If_Argument"), new NonTerminal("Conditional_Operator"), new NonTerminal("If_Argument"), new Terminal("right_parenthesis_operator"))
        ));
        productions.add(new Production(
            new NonTerminal("If_Argument"),
            Arrays.asList(new Terminal("number_literal"))
        ));
        productions.add(new Production(
            new NonTerminal("If_Argument"),
            Arrays.asList(new Terminal("identifier"))
        ));
        productions.add(new Production(
            new NonTerminal("Else_Statement"),
            Arrays.asList(new Terminal("else"), new Terminal("begin"), new NonTerminal("Body_Sequence"), new Terminal("end"))
        ));
        productions.add(new Production(
            new NonTerminal("Print_Statement"),
            Arrays.asList(new Terminal("print_line"), new Terminal("left_parenthesis_operator"), new Terminal("string_literal"), new Terminal("right_parenthesis_operator"), new NonTerminal("Line_Terminator"))
        ));
        productions.add(new Production(
            new NonTerminal("Print_Statement"),
            Arrays.asList(new Terminal("print_line"), new Terminal("left_parenthesis_operator"), new Terminal("identifier"), new Terminal("right_parenthesis_operator"))
        ));
        productions.add(new Production(
            new NonTerminal("Display_Statement"),
            Arrays.asList(new Terminal("display_line"), new Terminal("left_parenthesis_operator"), new Terminal("string_literal"), new Terminal("right_parenthesis_operator"), new NonTerminal("Line_Terminator"))
        ));
        productions.add(new Production(
            new NonTerminal("Display_Statement"),
            Arrays.asList(new Terminal("display_line"), new Terminal("left_parenthesis_operator"), new Terminal("identifier"), new Terminal("right_parenthesis_operator"))
        ));
        productions.add(new Production(
            new NonTerminal("Expression"),
            Arrays.asList(new NonTerminal("Primary_Expression"), new NonTerminal("Expression_Tail"))
        ));
        productions.add(new Production(
            new NonTerminal("Primary_Expression"),
            Arrays.asList(new Terminal("number_literal"))
        ));
        productions.add(new Production(
            new NonTerminal("Primary_Expression"),
            Arrays.asList(new Terminal("identifier"))
        ));
        productions.add(new Production(
            new NonTerminal("Expression_Tail"),
            Arrays.asList(new Terminal("multiplication_operator"), new NonTerminal("Expression"))
        ));
        productions.add(new Production(
            new NonTerminal("Expression_Tail"),
            Arrays.asList(new Terminal("eps"))
        ));
        productions.add(new Production(
            new NonTerminal("Conditional_Operator"),
            Arrays.asList(new Terminal("less_than_operator"))
        ));
        productions.add(new Production(
            new NonTerminal("Conditional_Operator"),
            Arrays.asList(new Terminal("greater_than_operator"))
        ));
        productions.add(new Production(
            new NonTerminal("Conditional_Operator"),
            Arrays.asList(new Terminal("equivalence_operator"))
        ));
        productions.add(new Production(
            new NonTerminal("Conditional_Operator"),
            Arrays.asList(new Terminal("assignment_operator"), new Terminal("assignment_operator"))
        ));
        productions.add(new Production(
            new NonTerminal("For_Statement"),
            Arrays.asList(new Terminal("for"), new Terminal("left_parenthesis_operator"), new Terminal("int"), new Terminal("identifier"), new Terminal("assignment_operator"), new NonTerminal("Expression"), new Terminal("statement_terminator"), new NonTerminal("If_Argument"), new NonTerminal("Conditional_Operator"), new NonTerminal("If_Argument"), new Terminal("right_parenthesis_operator"), new Terminal("begin"), new NonTerminal("Body_Sequence"), new Terminal("end"))
        ));
        productions.add(new Production(
            new NonTerminal("While_Statement"),
            Arrays.asList(new Terminal("while"), new Terminal("left_parenthesis_operator"), new NonTerminal("If_Argument"), new NonTerminal("Conditional_Operator"), new NonTerminal("If_Argument"), new Terminal("right_parenthesis_operator"), new Terminal("begin"), new NonTerminal("Body_Sequence"), new Terminal("end"))
        ));


		LRTable lrTable = new LRTable();
        lrTable.addAction(1, "$", new Action("ACCEPT", -1, null));
        lrTable.addAction(0, "program", new Action("SHIFT", 2, null));
        lrTable.addGoto(0, "Program_Statement", 1);
        lrTable.addAction(2, "identifier", new Action("SHIFT", 3, null));
        lrTable.addAction(3, "begin", new Action("SHIFT", 4, null));
        lrTable.addAction(4, "identifier", new Action("SHIFT", 22, null));
        lrTable.addAction(4, "int", new Action("SHIFT", 21, null));
        lrTable.addAction(4, "if", new Action("SHIFT", 14, null));
        lrTable.addAction(4, "else", new Action("SHIFT", 15, null));
        lrTable.addAction(4, "print_line", new Action("SHIFT", 16, null));
        lrTable.addAction(4, "display_line", new Action("SHIFT", 18, null));
        lrTable.addAction(4, "for", new Action("SHIFT", 19, null));
        lrTable.addAction(4, "while", new Action("SHIFT", 20, null));
        lrTable.addGoto(4, "Body_Sequence", 5);
        lrTable.addGoto(4, "Body", 6);
        lrTable.addGoto(4, "Declaration_Sequence", 10);
        lrTable.addGoto(4, "Declaration_Statement", 17);
        lrTable.addGoto(4, "If_Statement", 7);
        lrTable.addGoto(4, "Else_Statement", 8);
        lrTable.addGoto(4, "Print_Statement", 9);
        lrTable.addGoto(4, "Display_Statement", 11);
        lrTable.addGoto(4, "For_Statement", 12);
        lrTable.addGoto(4, "While_Statement", 13);
        lrTable.addAction(5, "end", new Action("SHIFT", 23, null));
        lrTable.addAction(6, "identifier", new Action("SHIFT", 22, null));
        lrTable.addAction(6, "end", new Action("REDUCE", -1, "4"));
        lrTable.addAction(6, "int", new Action("SHIFT", 21, null));
        lrTable.addAction(6, "if", new Action("SHIFT", 14, null));
        lrTable.addAction(6, "else", new Action("SHIFT", 15, null));
        lrTable.addAction(6, "print_line", new Action("SHIFT", 16, null));
        lrTable.addAction(6, "display_line", new Action("SHIFT", 18, null));
        lrTable.addAction(6, "for", new Action("SHIFT", 19, null));
        lrTable.addAction(6, "while", new Action("SHIFT", 20, null));
        lrTable.addGoto(6, "Body_Sequence", 25);
        lrTable.addGoto(6, "Body_Sequence_Tail", 24);
        lrTable.addGoto(6, "Body", 6);
        lrTable.addGoto(6, "Declaration_Sequence", 10);
        lrTable.addGoto(6, "Declaration_Statement", 17);
        lrTable.addGoto(6, "If_Statement", 7);
        lrTable.addGoto(6, "Else_Statement", 8);
        lrTable.addGoto(6, "Print_Statement", 9);
        lrTable.addGoto(6, "Display_Statement", 11);
        lrTable.addGoto(6, "For_Statement", 12);
        lrTable.addGoto(6, "While_Statement", 13);
        lrTable.addAction(7, "identifier", new Action("REDUCE", -1, "5"));
        lrTable.addAction(7, "end", new Action("REDUCE", -1, "5"));
        lrTable.addAction(7, "int", new Action("REDUCE", -1, "5"));
        lrTable.addAction(7, "if", new Action("REDUCE", -1, "5"));
        lrTable.addAction(7, "else", new Action("REDUCE", -1, "5"));
        lrTable.addAction(7, "print_line", new Action("REDUCE", -1, "5"));
        lrTable.addAction(7, "display_line", new Action("REDUCE", -1, "5"));
        lrTable.addAction(7, "for", new Action("REDUCE", -1, "5"));
        lrTable.addAction(7, "while", new Action("REDUCE", -1, "5"));
        lrTable.addAction(8, "identifier", new Action("REDUCE", -1, "6"));
        lrTable.addAction(8, "end", new Action("REDUCE", -1, "6"));
        lrTable.addAction(8, "int", new Action("REDUCE", -1, "6"));
        lrTable.addAction(8, "if", new Action("REDUCE", -1, "6"));
        lrTable.addAction(8, "else", new Action("REDUCE", -1, "6"));
        lrTable.addAction(8, "print_line", new Action("REDUCE", -1, "6"));
        lrTable.addAction(8, "display_line", new Action("REDUCE", -1, "6"));
        lrTable.addAction(8, "for", new Action("REDUCE", -1, "6"));
        lrTable.addAction(8, "while", new Action("REDUCE", -1, "6"));
        lrTable.addAction(9, "identifier", new Action("REDUCE", -1, "7"));
        lrTable.addAction(9, "end", new Action("REDUCE", -1, "7"));
        lrTable.addAction(9, "int", new Action("REDUCE", -1, "7"));
        lrTable.addAction(9, "if", new Action("REDUCE", -1, "7"));
        lrTable.addAction(9, "else", new Action("REDUCE", -1, "7"));
        lrTable.addAction(9, "print_line", new Action("REDUCE", -1, "7"));
        lrTable.addAction(9, "display_line", new Action("REDUCE", -1, "7"));
        lrTable.addAction(9, "for", new Action("REDUCE", -1, "7"));
        lrTable.addAction(9, "while", new Action("REDUCE", -1, "7"));
        lrTable.addAction(10, "identifier", new Action("REDUCE", -1, "8"));
        lrTable.addAction(10, "end", new Action("REDUCE", -1, "8"));
        lrTable.addAction(10, "int", new Action("REDUCE", -1, "8"));
        lrTable.addAction(10, "if", new Action("REDUCE", -1, "8"));
        lrTable.addAction(10, "else", new Action("REDUCE", -1, "8"));
        lrTable.addAction(10, "print_line", new Action("REDUCE", -1, "8"));
        lrTable.addAction(10, "display_line", new Action("REDUCE", -1, "8"));
        lrTable.addAction(10, "for", new Action("REDUCE", -1, "8"));
        lrTable.addAction(10, "while", new Action("REDUCE", -1, "8"));
        lrTable.addAction(11, "identifier", new Action("REDUCE", -1, "9"));
        lrTable.addAction(11, "end", new Action("REDUCE", -1, "9"));
        lrTable.addAction(11, "int", new Action("REDUCE", -1, "9"));
        lrTable.addAction(11, "if", new Action("REDUCE", -1, "9"));
        lrTable.addAction(11, "else", new Action("REDUCE", -1, "9"));
        lrTable.addAction(11, "print_line", new Action("REDUCE", -1, "9"));
        lrTable.addAction(11, "display_line", new Action("REDUCE", -1, "9"));
        lrTable.addAction(11, "for", new Action("REDUCE", -1, "9"));
        lrTable.addAction(11, "while", new Action("REDUCE", -1, "9"));
        lrTable.addAction(12, "identifier", new Action("REDUCE", -1, "10"));
        lrTable.addAction(12, "end", new Action("REDUCE", -1, "10"));
        lrTable.addAction(12, "int", new Action("REDUCE", -1, "10"));
        lrTable.addAction(12, "if", new Action("REDUCE", -1, "10"));
        lrTable.addAction(12, "else", new Action("REDUCE", -1, "10"));
        lrTable.addAction(12, "print_line", new Action("REDUCE", -1, "10"));
        lrTable.addAction(12, "display_line", new Action("REDUCE", -1, "10"));
        lrTable.addAction(12, "for", new Action("REDUCE", -1, "10"));
        lrTable.addAction(12, "while", new Action("REDUCE", -1, "10"));
        lrTable.addAction(13, "identifier", new Action("REDUCE", -1, "11"));
        lrTable.addAction(13, "end", new Action("REDUCE", -1, "11"));
        lrTable.addAction(13, "int", new Action("REDUCE", -1, "11"));
        lrTable.addAction(13, "if", new Action("REDUCE", -1, "11"));
        lrTable.addAction(13, "else", new Action("REDUCE", -1, "11"));
        lrTable.addAction(13, "print_line", new Action("REDUCE", -1, "11"));
        lrTable.addAction(13, "display_line", new Action("REDUCE", -1, "11"));
        lrTable.addAction(13, "for", new Action("REDUCE", -1, "11"));
        lrTable.addAction(13, "while", new Action("REDUCE", -1, "11"));
        lrTable.addAction(14, "left_parenthesis_operator", new Action("SHIFT", 27, null));
        lrTable.addGoto(14, "If_Conditional", 26);
        lrTable.addAction(15, "begin", new Action("SHIFT", 28, null));
        lrTable.addAction(16, "left_parenthesis_operator", new Action("SHIFT", 29, null));
        lrTable.addAction(17, "statement_terminator", new Action("SHIFT", 32, null));
        lrTable.addAction(17, "punctuation_comma", new Action("SHIFT", 31, null));
        lrTable.addGoto(17, "Declaration_Sequence_Tail", 30);
        lrTable.addAction(18, "left_parenthesis_operator", new Action("SHIFT", 34, null));
        lrTable.addAction(19, "left_parenthesis_operator", new Action("SHIFT", 35, null));
        lrTable.addAction(20, "left_parenthesis_operator", new Action("SHIFT", 36, null));
        lrTable.addAction(21, "identifier", new Action("SHIFT", 37, null));
        lrTable.addAction(22, "statement_terminator", new Action("REDUCE", -1, "20"));
        lrTable.addAction(22, "punctuation_comma", new Action("REDUCE", -1, "20"));
        lrTable.addAction(22, "assignment_operator", new Action("SHIFT", 38, null));
        lrTable.addAction(24, "end", new Action("REDUCE", -1, "2"));
        lrTable.addAction(25, "end", new Action("REDUCE", -1, "3"));
        lrTable.addAction(26, "begin", new Action("SHIFT", 39, null));
        lrTable.addAction(27, "identifier", new Action("SHIFT", 42, null));
        lrTable.addAction(27, "number_literal", new Action("SHIFT", 41, null));
        lrTable.addGoto(27, "If_Argument", 40);
        lrTable.addAction(28, "identifier", new Action("SHIFT", 22, null));
        lrTable.addAction(28, "int", new Action("SHIFT", 21, null));
        lrTable.addAction(28, "if", new Action("SHIFT", 14, null));
        lrTable.addAction(28, "else", new Action("SHIFT", 15, null));
        lrTable.addAction(28, "print_line", new Action("SHIFT", 16, null));
        lrTable.addAction(28, "display_line", new Action("SHIFT", 18, null));
        lrTable.addAction(28, "for", new Action("SHIFT", 19, null));
        lrTable.addAction(28, "while", new Action("SHIFT", 20, null));
        lrTable.addGoto(28, "Body_Sequence", 43);
        lrTable.addGoto(28, "Body", 6);
        lrTable.addGoto(28, "Declaration_Sequence", 10);
        lrTable.addGoto(28, "Declaration_Statement", 17);
        lrTable.addGoto(28, "If_Statement", 7);
        lrTable.addGoto(28, "Else_Statement", 8);
        lrTable.addGoto(28, "Print_Statement", 9);
        lrTable.addGoto(28, "Display_Statement", 11);
        lrTable.addGoto(28, "For_Statement", 12);
        lrTable.addGoto(28, "While_Statement", 13);
        lrTable.addAction(29, "identifier", new Action("SHIFT", 45, null));
        lrTable.addAction(29, "string_literal", new Action("SHIFT", 44, null));
        lrTable.addAction(30, "identifier", new Action("REDUCE", -1, "14"));
        lrTable.addAction(30, "end", new Action("REDUCE", -1, "14"));
        lrTable.addAction(30, "int", new Action("REDUCE", -1, "14"));
        lrTable.addAction(30, "if", new Action("REDUCE", -1, "14"));
        lrTable.addAction(30, "else", new Action("REDUCE", -1, "14"));
        lrTable.addAction(30, "print_line", new Action("REDUCE", -1, "14"));
        lrTable.addAction(30, "display_line", new Action("REDUCE", -1, "14"));
        lrTable.addAction(30, "for", new Action("REDUCE", -1, "14"));
        lrTable.addAction(30, "while", new Action("REDUCE", -1, "14"));
        lrTable.addAction(31, "identifier", new Action("SHIFT", 22, null));
        lrTable.addAction(31, "int", new Action("SHIFT", 21, null));
        lrTable.addGoto(31, "Declaration_Sequence", 46);
        lrTable.addGoto(31, "Declaration_Statement", 17);
        lrTable.addAction(32, "identifier", new Action("REDUCE", -1, "16"));
        lrTable.addAction(32, "end", new Action("REDUCE", -1, "16"));
        lrTable.addAction(32, "int", new Action("REDUCE", -1, "16"));
        lrTable.addAction(32, "if", new Action("REDUCE", -1, "16"));
        lrTable.addAction(32, "else", new Action("REDUCE", -1, "16"));
        lrTable.addAction(32, "print_line", new Action("REDUCE", -1, "16"));
        lrTable.addAction(32, "display_line", new Action("REDUCE", -1, "16"));
        lrTable.addAction(32, "for", new Action("REDUCE", -1, "16"));
        lrTable.addAction(32, "while", new Action("REDUCE", -1, "16"));
        lrTable.addAction(33, "left_parenthesis_operator", new Action("SHIFT", 47, null));
        lrTable.addAction(34, "identifier", new Action("SHIFT", 48, null));
        lrTable.addAction(35, "int", new Action("SHIFT", 49, null));
        lrTable.addAction(36, "identifier", new Action("SHIFT", 42, null));
        lrTable.addAction(36, "number_literal", new Action("SHIFT", 41, null));
        lrTable.addGoto(36, "If_Argument", 50);
        lrTable.addAction(37, "statement_terminator", new Action("REDUCE", -1, "17"));
        lrTable.addAction(37, "punctuation_comma", new Action("REDUCE", -1, "17"));
        lrTable.addAction(37, "assignment_operator", new Action("SHIFT", 51, null));
        lrTable.addAction(38, "identifier", new Action("SHIFT", 55, null));
        lrTable.addAction(38, "number_literal", new Action("SHIFT", 54, null));
        lrTable.addGoto(38, "Expression", 52);
        lrTable.addGoto(38, "Primary_Expression", 53);
        lrTable.addAction(39, "identifier", new Action("SHIFT", 22, null));
        lrTable.addAction(39, "int", new Action("SHIFT", 21, null));
        lrTable.addAction(39, "if", new Action("SHIFT", 14, null));
        lrTable.addAction(39, "else", new Action("SHIFT", 15, null));
        lrTable.addAction(39, "print_line", new Action("SHIFT", 16, null));
        lrTable.addAction(39, "display_line", new Action("SHIFT", 18, null));
        lrTable.addAction(39, "for", new Action("SHIFT", 19, null));
        lrTable.addAction(39, "while", new Action("SHIFT", 20, null));
        lrTable.addGoto(39, "Body_Sequence", 56);
        lrTable.addGoto(39, "Body", 6);
        lrTable.addGoto(39, "Declaration_Sequence", 10);
        lrTable.addGoto(39, "Declaration_Statement", 17);
        lrTable.addGoto(39, "If_Statement", 7);
        lrTable.addGoto(39, "Else_Statement", 8);
        lrTable.addGoto(39, "Print_Statement", 9);
        lrTable.addGoto(39, "Display_Statement", 11);
        lrTable.addGoto(39, "For_Statement", 12);
        lrTable.addGoto(39, "While_Statement", 13);
        lrTable.addAction(40, "assignment_operator", new Action("SHIFT", 61, null));
        lrTable.addAction(40, "less_than_operator", new Action("SHIFT", 58, null));
        lrTable.addAction(40, "greater_than_operator", new Action("SHIFT", 59, null));
        lrTable.addAction(40, "equivalence_operator", new Action("SHIFT", 60, null));
        lrTable.addGoto(40, "Conditional_Operator", 57);
        lrTable.addAction(41, "assignment_operator", new Action("REDUCE", -1, "23"));
        lrTable.addAction(41, "less_than_operator", new Action("REDUCE", -1, "23"));
        lrTable.addAction(41, "greater_than_operator", new Action("REDUCE", -1, "23"));
        lrTable.addAction(41, "equivalence_operator", new Action("REDUCE", -1, "23"));
        lrTable.addAction(42, "assignment_operator", new Action("REDUCE", -1, "24"));
        lrTable.addAction(42, "less_than_operator", new Action("REDUCE", -1, "24"));
        lrTable.addAction(42, "greater_than_operator", new Action("REDUCE", -1, "24"));
        lrTable.addAction(42, "equivalence_operator", new Action("REDUCE", -1, "24"));
        lrTable.addAction(43, "end", new Action("SHIFT", 62, null));
        lrTable.addAction(44, "right_parenthesis_operator", new Action("SHIFT", 63, null));
        lrTable.addAction(45, "right_parenthesis_operator", new Action("SHIFT", 64, null));
        lrTable.addAction(46, "identifier", new Action("REDUCE", -1, "15"));
        lrTable.addAction(46, "end", new Action("REDUCE", -1, "15"));
        lrTable.addAction(46, "int", new Action("REDUCE", -1, "15"));
        lrTable.addAction(46, "if", new Action("REDUCE", -1, "15"));
        lrTable.addAction(46, "else", new Action("REDUCE", -1, "15"));
        lrTable.addAction(46, "print_line", new Action("REDUCE", -1, "15"));
        lrTable.addAction(46, "display_line", new Action("REDUCE", -1, "15"));
        lrTable.addAction(46, "for", new Action("REDUCE", -1, "15"));
        lrTable.addAction(46, "while", new Action("REDUCE", -1, "15"));
        lrTable.addAction(47, "string_literal", new Action("SHIFT", 65, null));
        lrTable.addAction(48, "right_parenthesis_operator", new Action("SHIFT", 66, null));
        lrTable.addAction(49, "identifier", new Action("SHIFT", 67, null));
        lrTable.addAction(50, "assignment_operator", new Action("SHIFT", 61, null));
        lrTable.addAction(50, "less_than_operator", new Action("SHIFT", 58, null));
        lrTable.addAction(50, "greater_than_operator", new Action("SHIFT", 59, null));
        lrTable.addAction(50, "equivalence_operator", new Action("SHIFT", 60, null));
        lrTable.addGoto(50, "Conditional_Operator", 68);
        lrTable.addAction(51, "identifier", new Action("SHIFT", 55, null));
        lrTable.addAction(51, "number_literal", new Action("SHIFT", 54, null));
        lrTable.addGoto(51, "Expression", 69);
        lrTable.addGoto(51, "Primary_Expression", 53);
        lrTable.addAction(52, "statement_terminator", new Action("REDUCE", -1, "19"));
        lrTable.addAction(52, "punctuation_comma", new Action("REDUCE", -1, "19"));
        lrTable.addAction(53, "statement_terminator", new Action("REDUCE", -1, "34"));
        lrTable.addAction(53, "punctuation_comma", new Action("REDUCE", -1, "34"));
        lrTable.addAction(53, "multiplication_operator", new Action("SHIFT", 71, null));
        lrTable.addGoto(53, "Expression_Tail", 70);
        lrTable.addAction(54, "statement_terminator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(54, "punctuation_comma", new Action("REDUCE", -1, "31"));
        lrTable.addAction(54, "multiplication_operator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(55, "statement_terminator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(55, "punctuation_comma", new Action("REDUCE", -1, "32"));
        lrTable.addAction(55, "multiplication_operator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(56, "end", new Action("SHIFT", 72, null));
        lrTable.addAction(57, "identifier", new Action("SHIFT", 75, null));
        lrTable.addAction(57, "number_literal", new Action("SHIFT", 74, null));
        lrTable.addGoto(57, "If_Argument", 73);
        lrTable.addAction(58, "identifier", new Action("REDUCE", -1, "35"));
        lrTable.addAction(58, "number_literal", new Action("REDUCE", -1, "35"));
        lrTable.addAction(59, "identifier", new Action("REDUCE", -1, "36"));
        lrTable.addAction(59, "number_literal", new Action("REDUCE", -1, "36"));
        lrTable.addAction(60, "identifier", new Action("REDUCE", -1, "37"));
        lrTable.addAction(60, "number_literal", new Action("REDUCE", -1, "37"));
        lrTable.addAction(61, "assignment_operator", new Action("SHIFT", 76, null));
        lrTable.addAction(62, "identifier", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "end", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "int", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "if", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "else", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "print_line", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "display_line", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "for", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "while", new Action("REDUCE", -1, "25"));
        lrTable.addAction(63, "statement_terminator", new Action("SHIFT", 78, null));
        lrTable.addAction(63, "punctuation_comma", new Action("SHIFT", 79, null));
        lrTable.addGoto(63, "Line_Terminator", 77);
        lrTable.addAction(64, "identifier", new Action("REDUCE", -1, "27"));
        lrTable.addAction(64, "end", new Action("REDUCE", -1, "27"));
        lrTable.addAction(64, "int", new Action("REDUCE", -1, "27"));
        lrTable.addAction(64, "if", new Action("REDUCE", -1, "27"));
        lrTable.addAction(64, "else", new Action("REDUCE", -1, "27"));
        lrTable.addAction(64, "print_line", new Action("REDUCE", -1, "27"));
        lrTable.addAction(64, "display_line", new Action("REDUCE", -1, "27"));
        lrTable.addAction(64, "for", new Action("REDUCE", -1, "27"));
        lrTable.addAction(64, "while", new Action("REDUCE", -1, "27"));
        lrTable.addAction(66, "identifier", new Action("REDUCE", -1, "29"));
        lrTable.addAction(66, "end", new Action("REDUCE", -1, "29"));
        lrTable.addAction(66, "int", new Action("REDUCE", -1, "29"));
        lrTable.addAction(66, "if", new Action("REDUCE", -1, "29"));
        lrTable.addAction(66, "else", new Action("REDUCE", -1, "29"));
        lrTable.addAction(66, "print_line", new Action("REDUCE", -1, "29"));
        lrTable.addAction(66, "display_line", new Action("REDUCE", -1, "29"));
        lrTable.addAction(66, "for", new Action("REDUCE", -1, "29"));
        lrTable.addAction(66, "while", new Action("REDUCE", -1, "29"));
        lrTable.addAction(67, "assignment_operator", new Action("SHIFT", 81, null));
        lrTable.addAction(68, "identifier", new Action("SHIFT", 75, null));
        lrTable.addAction(68, "number_literal", new Action("SHIFT", 74, null));
        lrTable.addGoto(68, "If_Argument", 82);
        lrTable.addAction(69, "statement_terminator", new Action("REDUCE", -1, "18"));
        lrTable.addAction(69, "punctuation_comma", new Action("REDUCE", -1, "18"));
        lrTable.addAction(70, "statement_terminator", new Action("REDUCE", -1, "30"));
        lrTable.addAction(70, "punctuation_comma", new Action("REDUCE", -1, "30"));
        lrTable.addAction(71, "identifier", new Action("SHIFT", 55, null));
        lrTable.addAction(71, "number_literal", new Action("SHIFT", 54, null));
        lrTable.addGoto(71, "Expression", 83);
        lrTable.addGoto(71, "Primary_Expression", 53);
        lrTable.addAction(72, "identifier", new Action("REDUCE", -1, "21"));
        lrTable.addAction(72, "end", new Action("REDUCE", -1, "21"));
        lrTable.addAction(72, "int", new Action("REDUCE", -1, "21"));
        lrTable.addAction(72, "if", new Action("REDUCE", -1, "21"));
        lrTable.addAction(72, "else", new Action("REDUCE", -1, "21"));
        lrTable.addAction(72, "print_line", new Action("REDUCE", -1, "21"));
        lrTable.addAction(72, "display_line", new Action("REDUCE", -1, "21"));
        lrTable.addAction(72, "for", new Action("REDUCE", -1, "21"));
        lrTable.addAction(72, "while", new Action("REDUCE", -1, "21"));
        lrTable.addAction(73, "right_parenthesis_operator", new Action("SHIFT", 84, null));
        lrTable.addAction(74, "right_parenthesis_operator", new Action("REDUCE", -1, "23"));
        lrTable.addAction(75, "right_parenthesis_operator", new Action("REDUCE", -1, "24"));
        lrTable.addAction(76, "identifier", new Action("REDUCE", -1, "38"));
        lrTable.addAction(76, "number_literal", new Action("REDUCE", -1, "38"));
        lrTable.addAction(77, "identifier", new Action("REDUCE", -1, "26"));
        lrTable.addAction(77, "end", new Action("REDUCE", -1, "26"));
        lrTable.addAction(77, "int", new Action("REDUCE", -1, "26"));
        lrTable.addAction(77, "if", new Action("REDUCE", -1, "26"));
        lrTable.addAction(77, "else", new Action("REDUCE", -1, "26"));
        lrTable.addAction(77, "print_line", new Action("REDUCE", -1, "26"));
        lrTable.addAction(77, "display_line", new Action("REDUCE", -1, "26"));
        lrTable.addAction(77, "for", new Action("REDUCE", -1, "26"));
        lrTable.addAction(77, "while", new Action("REDUCE", -1, "26"));
        lrTable.addAction(78, "identifier", new Action("REDUCE", -1, "12"));
        lrTable.addAction(78, "end", new Action("REDUCE", -1, "12"));
        lrTable.addAction(78, "int", new Action("REDUCE", -1, "12"));
        lrTable.addAction(78, "if", new Action("REDUCE", -1, "12"));
        lrTable.addAction(78, "else", new Action("REDUCE", -1, "12"));
        lrTable.addAction(78, "print_line", new Action("REDUCE", -1, "12"));
        lrTable.addAction(78, "display_line", new Action("REDUCE", -1, "12"));
        lrTable.addAction(78, "for", new Action("REDUCE", -1, "12"));
        lrTable.addAction(78, "while", new Action("REDUCE", -1, "12"));
        lrTable.addAction(79, "identifier", new Action("REDUCE", -1, "13"));
        lrTable.addAction(79, "end", new Action("REDUCE", -1, "13"));
        lrTable.addAction(79, "int", new Action("REDUCE", -1, "13"));
        lrTable.addAction(79, "if", new Action("REDUCE", -1, "13"));
        lrTable.addAction(79, "else", new Action("REDUCE", -1, "13"));
        lrTable.addAction(79, "print_line", new Action("REDUCE", -1, "13"));
        lrTable.addAction(79, "display_line", new Action("REDUCE", -1, "13"));
        lrTable.addAction(79, "for", new Action("REDUCE", -1, "13"));
        lrTable.addAction(79, "while", new Action("REDUCE", -1, "13"));
        lrTable.addAction(80, "right_parenthesis_operator", new Action("SHIFT", 85, null));
        lrTable.addAction(81, "identifier", new Action("SHIFT", 89, null));
        lrTable.addAction(81, "number_literal", new Action("SHIFT", 88, null));
        lrTable.addGoto(81, "Expression", 86);
        lrTable.addGoto(81, "Primary_Expression", 87);
        lrTable.addAction(82, "right_parenthesis_operator", new Action("SHIFT", 90, null));
        lrTable.addAction(83, "statement_terminator", new Action("REDUCE", -1, "33"));
        lrTable.addAction(83, "punctuation_comma", new Action("REDUCE", -1, "33"));
        lrTable.addAction(84, "begin", new Action("REDUCE", -1, "22"));
        lrTable.addAction(85, "statement_terminator", new Action("SHIFT", 78, null));
        lrTable.addAction(85, "punctuation_comma", new Action("SHIFT", 79, null));
        lrTable.addGoto(85, "Line_Terminator", 91);
        lrTable.addAction(86, "statement_terminator", new Action("SHIFT", 92, null));
        lrTable.addAction(87, "statement_terminator", new Action("REDUCE", -1, "34"));
        lrTable.addAction(87, "multiplication_operator", new Action("SHIFT", 94, null));
        lrTable.addGoto(87, "Expression_Tail", 93);
        lrTable.addAction(88, "statement_terminator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(88, "multiplication_operator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(89, "statement_terminator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(89, "multiplication_operator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(90, "begin", new Action("SHIFT", 95, null));
        lrTable.addAction(91, "identifier", new Action("REDUCE", -1, "28"));
        lrTable.addAction(91, "end", new Action("REDUCE", -1, "28"));
        lrTable.addAction(91, "int", new Action("REDUCE", -1, "28"));
        lrTable.addAction(91, "if", new Action("REDUCE", -1, "28"));
        lrTable.addAction(91, "else", new Action("REDUCE", -1, "28"));
        lrTable.addAction(91, "print_line", new Action("REDUCE", -1, "28"));
        lrTable.addAction(91, "display_line", new Action("REDUCE", -1, "28"));
        lrTable.addAction(91, "for", new Action("REDUCE", -1, "28"));
        lrTable.addAction(91, "while", new Action("REDUCE", -1, "28"));
        lrTable.addAction(92, "identifier", new Action("SHIFT", 98, null));
        lrTable.addAction(92, "number_literal", new Action("SHIFT", 97, null));
        lrTable.addGoto(92, "If_Argument", 96);
        lrTable.addAction(93, "statement_terminator", new Action("REDUCE", -1, "30"));
        lrTable.addAction(94, "identifier", new Action("SHIFT", 89, null));
        lrTable.addAction(94, "number_literal", new Action("SHIFT", 88, null));
        lrTable.addGoto(94, "Expression", 99);
        lrTable.addGoto(94, "Primary_Expression", 87);
        lrTable.addAction(95, "identifier", new Action("SHIFT", 22, null));
        lrTable.addAction(95, "int", new Action("SHIFT", 21, null));
        lrTable.addAction(95, "if", new Action("SHIFT", 14, null));
        lrTable.addAction(95, "else", new Action("SHIFT", 15, null));
        lrTable.addAction(95, "print_line", new Action("SHIFT", 16, null));
        lrTable.addAction(95, "display_line", new Action("SHIFT", 18, null));
        lrTable.addAction(95, "for", new Action("SHIFT", 19, null));
        lrTable.addAction(95, "while", new Action("SHIFT", 20, null));
        lrTable.addGoto(95, "Body_Sequence", 100);
        lrTable.addGoto(95, "Body", 6);
        lrTable.addGoto(95, "Declaration_Sequence", 10);
        lrTable.addGoto(95, "Declaration_Statement", 17);
        lrTable.addGoto(95, "If_Statement", 7);
        lrTable.addGoto(95, "Else_Statement", 8);
        lrTable.addGoto(95, "Print_Statement", 9);
        lrTable.addGoto(95, "Display_Statement", 11);
        lrTable.addGoto(95, "For_Statement", 12);
        lrTable.addGoto(95, "While_Statement", 13);
        lrTable.addAction(99, "statement_terminator", new Action("REDUCE", -1, "33"));
        lrTable.addAction(100, "end", new Action("SHIFT", 102, null));
        lrTable.addAction(102, "identifier", new Action("REDUCE", -1, "40"));
        lrTable.addAction(102, "end", new Action("REDUCE", -1, "40"));
        lrTable.addAction(102, "int", new Action("REDUCE", -1, "40"));
        lrTable.addAction(102, "if", new Action("REDUCE", -1, "40"));
        lrTable.addAction(102, "else", new Action("REDUCE", -1, "40"));
        lrTable.addAction(102, "print_line", new Action("REDUCE", -1, "40"));
        lrTable.addAction(102, "display_line", new Action("REDUCE", -1, "40"));
        lrTable.addAction(102, "for", new Action("REDUCE", -1, "40"));
        lrTable.addAction(102, "while", new Action("REDUCE", -1, "40"));
        lrTable.addAction(103, "identifier", new Action("SHIFT", 75, null));
        lrTable.addAction(103, "number_literal", new Action("SHIFT", 74, null));
        lrTable.addGoto(103, "If_Argument", 104);
        lrTable.addAction(104, "right_parenthesis_operator", new Action("SHIFT", 105, null));
        lrTable.addAction(105, "begin", new Action("SHIFT", 106, null));
        lrTable.addAction(106, "identifier", new Action("SHIFT", 22, null));
        lrTable.addAction(106, "int", new Action("SHIFT", 21, null));
        lrTable.addAction(106, "if", new Action("SHIFT", 14, null));
        lrTable.addAction(106, "else", new Action("SHIFT", 15, null));
        lrTable.addAction(106, "print_line", new Action("SHIFT", 16, null));
        lrTable.addAction(106, "display_line", new Action("SHIFT", 18, null));
        lrTable.addAction(106, "for", new Action("SHIFT", 19, null));
        lrTable.addAction(106, "while", new Action("SHIFT", 20, null));
        lrTable.addGoto(106, "Body_Sequence", 107);
        lrTable.addGoto(106, "Body", 6);
        lrTable.addGoto(106, "Declaration_Sequence", 10);
        lrTable.addGoto(106, "Declaration_Statement", 17);
        lrTable.addGoto(106, "If_Statement", 7);
        lrTable.addGoto(106, "Else_Statement", 8);
        lrTable.addGoto(106, "Print_Statement", 9);
        lrTable.addGoto(106, "Display_Statement", 11);
        lrTable.addGoto(106, "For_Statement", 12);
        lrTable.addGoto(106, "While_Statement", 13);
        lrTable.addAction(107, "end", new Action("SHIFT", 108, null));
        lrTable.addAction(108, "identifier", new Action("REDUCE", -1, "39"));
        lrTable.addAction(108, "end", new Action("REDUCE", -1, "39"));
        lrTable.addAction(108, "int", new Action("REDUCE", -1, "39"));
        lrTable.addAction(108, "if", new Action("REDUCE", -1, "39"));
        lrTable.addAction(108, "else", new Action("REDUCE", -1, "39"));
        lrTable.addAction(108, "print_line", new Action("REDUCE", -1, "39"));
        lrTable.addAction(108, "display_line", new Action("REDUCE", -1, "39"));
        lrTable.addAction(108, "for", new Action("REDUCE", -1, "39"));
        lrTable.addAction(108, "while", new Action("REDUCE", -1, "39"));
        lrTable.addAction(109, "end", new Action("SHIFT", 111, null));
        lrTable.addAction(110, "identifier", new Action("REDUCE", -1, "39"));
        lrTable.addAction(110, "end", new Action("REDUCE", -1, "39"));
        lrTable.addAction(110, "int", new Action("REDUCE", -1, "39"));
        lrTable.addAction(110, "if", new Action("REDUCE", -1, "39"));
        lrTable.addAction(110, "else", new Action("REDUCE", -1, "39"));
        lrTable.addAction(110, "print_line", new Action("REDUCE", -1, "39"));
        lrTable.addAction(110, "display_line", new Action("REDUCE", -1, "39"));
        lrTable.addAction(110, "equivalence_operator", new Action("REDUCE", -1, "39"));
        lrTable.addAction(110, "for", new Action("REDUCE", -1, "39"));
        lrTable.addAction(111, "identifier", new Action("REDUCE", -1, "38"));
        lrTable.addAction(111, "end", new Action("REDUCE", -1, "38"));
        lrTable.addAction(111, "int", new Action("REDUCE", -1, "38"));
        lrTable.addAction(111, "if", new Action("REDUCE", -1, "38"));
        lrTable.addAction(111, "else", new Action("REDUCE", -1, "38"));
        lrTable.addAction(111, "print_line", new Action("REDUCE", -1, "38"));
        lrTable.addAction(111, "display_line", new Action("REDUCE", -1, "38"));
        lrTable.addAction(111, "equivalence_operator", new Action("REDUCE", -1, "38"));
        lrTable.addAction(111, "for", new Action("REDUCE", -1, "38"));
        lrTable.addAction(112, "end", new Action("SHIFT", 114, null));
        lrTable.addAction(113, "identifier", new Action("REDUCE", -1, "39"));
        lrTable.addAction(113, "end", new Action("REDUCE", -1, "39"));
        lrTable.addAction(113, "int", new Action("REDUCE", -1, "39"));
        lrTable.addAction(113, "if", new Action("REDUCE", -1, "39"));
        lrTable.addAction(113, "else", new Action("REDUCE", -1, "39"));
        lrTable.addAction(113, "print_line", new Action("REDUCE", -1, "39"));
        lrTable.addAction(113, "display_line", new Action("REDUCE", -1, "39"));
        lrTable.addAction(113, "greater_than_operator", new Action("REDUCE", -1, "39"));
        lrTable.addAction(113, "equivalence_operator", new Action("REDUCE", -1, "39"));
        lrTable.addAction(114, "identifier", new Action("REDUCE", -1, "38"));
        lrTable.addAction(114, "end", new Action("REDUCE", -1, "38"));
        lrTable.addAction(114, "int", new Action("REDUCE", -1, "38"));
        lrTable.addAction(114, "if", new Action("REDUCE", -1, "38"));
        lrTable.addAction(114, "else", new Action("REDUCE", -1, "38"));
        lrTable.addAction(114, "print_line", new Action("REDUCE", -1, "38"));
        lrTable.addAction(114, "display_line", new Action("REDUCE", -1, "38"));
        lrTable.addAction(114, "greater_than_operator", new Action("REDUCE", -1, "38"));
        lrTable.addAction(114, "equivalence_operator", new Action("REDUCE", -1, "38"));






//		ArrayList<TokenTerminal> tokenList = lexer.lexIntoTokenList(args[1]);
//		Stack<TokenTerminal> symbolStack = new Stack
		System.out.println(tokenList);
		for(TokenTerminal tok: tokenList) {
			System.out.print(tok.getTerminal() + " ");
		}
		LRParser lrParser = new LRParser();
		lrParser.parse(lrTable,tokenList,productions);



	}

}
