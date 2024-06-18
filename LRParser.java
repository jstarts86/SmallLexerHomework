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
                System.out.println("Syntax error at token: " + currentToken.getToken() + " in state: " + currentState);
                if (lrTable.hasShift(currentState)) {
                    System.out.println("Error Type: Shifting Error");
                } else if (lrTable.hasReduce(currentState)) {
                    System.out.println("Error Type: Reducing Error");
                }
                return;
            }

            System.out.println(interleaveStacks(stateStack, symbolStack));

            switch (action.actionType) {
                case "SHIFT":
                    System.out.print("[S] ");
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

    private String interleaveStacks(Stack<Integer> stateStack, Stack<TokenTerminal> symbolStack) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < stateStack.size(); i++) {
            result.append(stateStack.get(i));
            if (i < symbolStack.size()) {
                result.append(" ").append(symbolStack.get(i).getTerminal());
            }
            if (i < stateStack.size() - 1) {
                result.append(" ");
            }
        }
        return result.toString();
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
        System.out.print("[R] ");
    }

	public static void main(String[] args) throws IOException {
		SmallLexer lexer = new SmallLexer();
		ArrayList<TokenTerminal> tokenList = lexer.lexIntoTokenList(args[0]);
        tokenList.add(new TokenTerminal("$", "$"));
		List<Production> productions = new ArrayList<Production>();
	        productions.add(new Production(
            new NonTerminal("Program_Statement'"),
            Arrays.asList(new NonTerminal("Program_Statement"))
        ));
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
            Arrays.asList(new Terminal("print_line"), new Terminal("left_parenthesis_operator"), new Terminal("string_literal"), new Terminal("right_parenthesis_operator"), new Terminal("statement_terminator"))
        ));
        productions.add(new Production(
            new NonTerminal("Print_Statement"),
            Arrays.asList(new Terminal("print_line"), new Terminal("left_parenthesis_operator"), new Terminal("identifier"), new Terminal("right_parenthesis_operator"), new Terminal("statement_terminator"))
        ));
        productions.add(new Production(
            new NonTerminal("Display_Statement"),
            Arrays.asList(new Terminal("display_line"), new Terminal("left_parenthesis_operator"), new Terminal("string_literal"), new Terminal("right_parenthesis_operator"), new Terminal("statement_terminator"))
        ));
        productions.add(new Production(
            new NonTerminal("Display_Statement"),
            Arrays.asList(new Terminal("display_line"), new Terminal("left_parenthesis_operator"), new Terminal("identifier"), new Terminal("right_parenthesis_operator"), new Terminal("statement_terminator"))
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
            Arrays.asList(new NonTerminal("Arithmetic_Operator"), new NonTerminal("Expression"))
        ));
        productions.add(new Production(
            new NonTerminal("Expression_Tail"),
            Arrays.asList(new Terminal("eps"))
        ));
        productions.add(new Production(
            new NonTerminal("Arithmetic_Operator"),
            Arrays.asList(new Terminal("multiplication_operator"))
        ));
        productions.add(new Production(
            new NonTerminal("Arithmetic_Operator"),
            Arrays.asList(new Terminal("minus_operator"))
        ));
        productions.add(new Production(
            new NonTerminal("Arithmetic_Operator"),
            Arrays.asList(new Terminal("plus_operator"))
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
            Arrays.asList(new Terminal("for"), new Terminal("left_parenthesis_operator"), new Terminal("int"), new Terminal("identifier"), new Terminal("assignment_operator"), new NonTerminal(
                    "Expression"), new Terminal("statement_terminator"), new NonTerminal("If_Argument"), new NonTerminal("Conditional_Operator"), new NonTerminal("If_Argument"),
                    new Terminal("statement_terminator"), new Terminal("identifier"), new Terminal("plus_operator"),new Terminal("plus_operator"), new Terminal("right_parenthesis_operator"),
                    new Terminal("begin"), new NonTerminal("Body_Sequence"), new Terminal("end"))
        ));
        productions.add(new Production(
            new NonTerminal("While_Statement"),
            Arrays.asList(new Terminal("while"), new Terminal("left_parenthesis_operator"), new NonTerminal("If_Argument"), new NonTerminal("Conditional_Operator"), new NonTerminal("If_Argument"), new Terminal("right_parenthesis_operator"), new Terminal("begin"), new NonTerminal("Body_Sequence"), new Terminal("end"))
        ));

        LRTable lrTable = new LRTable();
        lrTable.addAction(1, "$", new Action("ACCEPT", -1, null));
        lrTable.addAction(23, "$", new Action("REDUCE", -1, "1"));




        lrTable.addAction(0, "program", new Action("SHIFT", 2, null));
        lrTable.addGoto(0, "Program_Statement", 1);
        // Skipping invalid integer conversion at state 1, header $, value acc
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
        lrTable.addGoto(23, "$", -1);
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
        lrTable.addAction(53, "multiplication_operator", new Action("SHIFT", 72, null));
        lrTable.addAction(53, "minus_operator", new Action("SHIFT", 73, null));
        lrTable.addAction(53, "plus_operator", new Action("SHIFT", 74, null));
        lrTable.addGoto(53, "Expression_Tail", 70);
        lrTable.addGoto(53, "Arithmetic_Operator", 71);
        lrTable.addAction(54, "statement_terminator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(54, "punctuation_comma", new Action("REDUCE", -1, "31"));
        lrTable.addAction(54, "multiplication_operator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(54, "minus_operator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(54, "plus_operator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(55, "statement_terminator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(55, "punctuation_comma", new Action("REDUCE", -1, "32"));
        lrTable.addAction(55, "multiplication_operator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(55, "minus_operator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(55, "plus_operator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(56, "end", new Action("SHIFT", 75, null));
        lrTable.addAction(57, "identifier", new Action("SHIFT", 78, null));
        lrTable.addAction(57, "number_literal", new Action("SHIFT", 77, null));
        lrTable.addGoto(57, "If_Argument", 76);
        lrTable.addAction(58, "identifier", new Action("REDUCE", -1, "38"));
        lrTable.addAction(58, "number_literal", new Action("REDUCE", -1, "38"));
        lrTable.addAction(59, "identifier", new Action("REDUCE", -1, "39"));
        lrTable.addAction(59, "number_literal", new Action("REDUCE", -1, "39"));
        lrTable.addAction(60, "identifier", new Action("REDUCE", -1, "40"));
        lrTable.addAction(60, "number_literal", new Action("REDUCE", -1, "40"));
        lrTable.addAction(61, "assignment_operator", new Action("SHIFT", 79, null));
        lrTable.addAction(62, "identifier", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "end", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "int", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "if", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "else", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "print_line", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "display_line", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "for", new Action("REDUCE", -1, "25"));
        lrTable.addAction(62, "while", new Action("REDUCE", -1, "25"));
        lrTable.addAction(63, "statement_terminator", new Action("SHIFT", 80, null));
        lrTable.addAction(64, "statement_terminator", new Action("SHIFT", 81, null));
        lrTable.addAction(66, "statement_terminator", new Action("SHIFT", 83, null));
        lrTable.addAction(67, "assignment_operator", new Action("SHIFT", 84, null));
        lrTable.addAction(68, "identifier", new Action("SHIFT", 78, null));
        lrTable.addAction(68, "number_literal", new Action("SHIFT", 77, null));
        lrTable.addGoto(68, "If_Argument", 85);
        lrTable.addAction(69, "statement_terminator", new Action("REDUCE", -1, "18"));
        lrTable.addAction(69, "punctuation_comma", new Action("REDUCE", -1, "18"));
        lrTable.addAction(70, "statement_terminator", new Action("REDUCE", -1, "30"));
        lrTable.addAction(70, "punctuation_comma", new Action("REDUCE", -1, "30"));
        lrTable.addAction(71, "identifier", new Action("SHIFT", 55, null));
        lrTable.addAction(71, "number_literal", new Action("SHIFT", 54, null));
        lrTable.addGoto(71, "Expression", 86);
        lrTable.addGoto(71, "Primary_Expression", 53);
        lrTable.addAction(72, "identifier", new Action("REDUCE", -1, "35"));
        lrTable.addAction(72, "number_literal", new Action("REDUCE", -1, "35"));
        lrTable.addAction(73, "identifier", new Action("REDUCE", -1, "36"));
        lrTable.addAction(73, "number_literal", new Action("REDUCE", -1, "36"));
        lrTable.addAction(74, "identifier", new Action("REDUCE", -1, "37"));
        lrTable.addAction(74, "number_literal", new Action("REDUCE", -1, "37"));
        lrTable.addAction(75, "identifier", new Action("REDUCE", -1, "21"));
        lrTable.addAction(75, "end", new Action("REDUCE", -1, "21"));
        lrTable.addAction(75, "int", new Action("REDUCE", -1, "21"));
        lrTable.addAction(75, "if", new Action("REDUCE", -1, "21"));
        lrTable.addAction(75, "else", new Action("REDUCE", -1, "21"));
        lrTable.addAction(75, "print_line", new Action("REDUCE", -1, "21"));
        lrTable.addAction(75, "display_line", new Action("REDUCE", -1, "21"));
        lrTable.addAction(75, "for", new Action("REDUCE", -1, "21"));
        lrTable.addAction(75, "while", new Action("REDUCE", -1, "21"));
        lrTable.addAction(76, "right_parenthesis_operator", new Action("SHIFT", 87, null));
        lrTable.addAction(77, "right_parenthesis_operator", new Action("REDUCE", -1, "23"));
        lrTable.addAction(78, "right_parenthesis_operator", new Action("REDUCE", -1, "24"));
        lrTable.addAction(79, "identifier", new Action("REDUCE", -1, "41"));
        lrTable.addAction(79, "number_literal", new Action("REDUCE", -1, "41"));
        lrTable.addAction(80, "identifier", new Action("REDUCE", -1, "26"));
        lrTable.addAction(80, "end", new Action("REDUCE", -1, "26"));
        lrTable.addAction(80, "int", new Action("REDUCE", -1, "26"));
        lrTable.addAction(80, "if", new Action("REDUCE", -1, "26"));
        lrTable.addAction(80, "else", new Action("REDUCE", -1, "26"));
        lrTable.addAction(80, "print_line", new Action("REDUCE", -1, "26"));
        lrTable.addAction(80, "display_line", new Action("REDUCE", -1, "26"));
        lrTable.addAction(80, "for", new Action("REDUCE", -1, "26"));
        lrTable.addAction(80, "while", new Action("REDUCE", -1, "26"));
        lrTable.addAction(81, "identifier", new Action("REDUCE", -1, "27"));
        lrTable.addAction(81, "end", new Action("REDUCE", -1, "27"));
        lrTable.addAction(81, "int", new Action("REDUCE", -1, "27"));
        lrTable.addAction(81, "if", new Action("REDUCE", -1, "27"));
        lrTable.addAction(81, "else", new Action("REDUCE", -1, "27"));
        lrTable.addAction(81, "print_line", new Action("REDUCE", -1, "27"));
        lrTable.addAction(81, "display_line", new Action("REDUCE", -1, "27"));
        lrTable.addAction(81, "for", new Action("REDUCE", -1, "27"));
        lrTable.addAction(81, "while", new Action("REDUCE", -1, "27"));
        lrTable.addAction(82, "right_parenthesis_operator", new Action("SHIFT", 88, null));
        lrTable.addAction(83, "identifier", new Action("REDUCE", -1, "29"));
        lrTable.addAction(83, "end", new Action("REDUCE", -1, "29"));
        lrTable.addAction(83, "int", new Action("REDUCE", -1, "29"));
        lrTable.addAction(83, "if", new Action("REDUCE", -1, "29"));
        lrTable.addAction(83, "else", new Action("REDUCE", -1, "29"));
        lrTable.addAction(83, "print_line", new Action("REDUCE", -1, "29"));
        lrTable.addAction(83, "display_line", new Action("REDUCE", -1, "29"));
        lrTable.addAction(83, "for", new Action("REDUCE", -1, "29"));
        lrTable.addAction(83, "while", new Action("REDUCE", -1, "29"));
        lrTable.addAction(84, "identifier", new Action("SHIFT", 92, null));
        lrTable.addAction(84, "number_literal", new Action("SHIFT", 91, null));
        lrTable.addGoto(84, "Expression", 89);
        lrTable.addGoto(84, "Primary_Expression", 90);
        lrTable.addAction(85, "right_parenthesis_operator", new Action("SHIFT", 93, null));
        lrTable.addAction(86, "statement_terminator", new Action("REDUCE", -1, "33"));
        lrTable.addAction(86, "punctuation_comma", new Action("REDUCE", -1, "33"));
        lrTable.addAction(87, "begin", new Action("REDUCE", -1, "22"));
        lrTable.addAction(88, "statement_terminator", new Action("SHIFT", 94, null));
        lrTable.addAction(89, "statement_terminator", new Action("SHIFT", 95, null));
        lrTable.addAction(90, "statement_terminator", new Action("REDUCE", -1, "34"));
        lrTable.addAction(90, "multiplication_operator", new Action("SHIFT", 72, null));
        lrTable.addAction(90, "minus_operator", new Action("SHIFT", 73, null));
        lrTable.addAction(90, "plus_operator", new Action("SHIFT", 74, null));
        lrTable.addGoto(90, "Expression_Tail", 96);
        lrTable.addGoto(90, "Arithmetic_Operator", 97);
        lrTable.addAction(91, "statement_terminator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(91, "multiplication_operator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(91, "minus_operator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(91, "plus_operator", new Action("REDUCE", -1, "31"));
        lrTable.addAction(92, "statement_terminator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(92, "multiplication_operator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(92, "minus_operator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(92, "plus_operator", new Action("REDUCE", -1, "32"));
        lrTable.addAction(93, "begin", new Action("SHIFT", 98, null));
        lrTable.addAction(94, "identifier", new Action("REDUCE", -1, "28"));
        lrTable.addAction(94, "end", new Action("REDUCE", -1, "28"));
        lrTable.addAction(94, "int", new Action("REDUCE", -1, "28"));
        lrTable.addAction(94, "if", new Action("REDUCE", -1, "28"));
        lrTable.addAction(94, "else", new Action("REDUCE", -1, "28"));
        lrTable.addAction(94, "print_line", new Action("REDUCE", -1, "28"));
        lrTable.addAction(94, "display_line", new Action("REDUCE", -1, "28"));
        lrTable.addAction(94, "for", new Action("REDUCE", -1, "28"));
        lrTable.addAction(94, "while", new Action("REDUCE", -1, "28"));
        lrTable.addAction(95, "identifier", new Action("SHIFT", 42, null));
        lrTable.addAction(95, "number_literal", new Action("SHIFT", 41, null));
        lrTable.addGoto(95, "If_Argument", 99);
        lrTable.addAction(96, "statement_terminator", new Action("REDUCE", -1, "30"));
        lrTable.addAction(97, "identifier", new Action("SHIFT", 92, null));
        lrTable.addAction(97, "number_literal", new Action("SHIFT", 91, null));
        lrTable.addGoto(97, "Expression", 100);
        lrTable.addGoto(97, "Primary_Expression", 90);
        lrTable.addAction(98, "identifier", new Action("SHIFT", 22, null));
        lrTable.addAction(98, "int", new Action("SHIFT", 21, null));
        lrTable.addAction(98, "if", new Action("SHIFT", 14, null));
        lrTable.addAction(98, "else", new Action("SHIFT", 15, null));
        lrTable.addAction(98, "print_line", new Action("SHIFT", 16, null));
        lrTable.addAction(98, "display_line", new Action("SHIFT", 18, null));
        lrTable.addAction(98, "for", new Action("SHIFT", 19, null));
        lrTable.addAction(98, "while", new Action("SHIFT", 20, null));
        lrTable.addGoto(98, "Body_Sequence", 101);
        lrTable.addGoto(98, "Body", 6);
        lrTable.addGoto(98, "Declaration_Sequence", 10);
        lrTable.addGoto(98, "Declaration_Statement", 17);
        lrTable.addGoto(98, "If_Statement", 7);
        lrTable.addGoto(98, "Else_Statement", 8);
        lrTable.addGoto(98, "Print_Statement", 9);
        lrTable.addGoto(98, "Display_Statement", 11);
        lrTable.addGoto(98, "For_Statement", 12);
        lrTable.addGoto(98, "While_Statement", 13);
        lrTable.addAction(99, "assignment_operator", new Action("SHIFT", 61, null));
        lrTable.addAction(99, "less_than_operator", new Action("SHIFT", 58, null));
        lrTable.addAction(99, "greater_than_operator", new Action("SHIFT", 59, null));
        lrTable.addAction(99, "equivalence_operator", new Action("SHIFT", 60, null));
        lrTable.addGoto(99, "Conditional_Operator", 102);
        lrTable.addAction(100, "statement_terminator", new Action("REDUCE", -1, "33"));
        lrTable.addAction(101, "end", new Action("SHIFT", 103, null));
        lrTable.addAction(102, "identifier", new Action("SHIFT", 106, null));
        lrTable.addAction(102, "number_literal", new Action("SHIFT", 105, null));
        lrTable.addGoto(102, "If_Argument", 104);
        lrTable.addAction(103, "identifier", new Action("REDUCE", -1, "43"));
        lrTable.addAction(103, "end", new Action("REDUCE", -1, "43"));
        lrTable.addAction(103, "int", new Action("REDUCE", -1, "43"));
        lrTable.addAction(103, "if", new Action("REDUCE", -1, "43"));
        lrTable.addAction(103, "else", new Action("REDUCE", -1, "43"));
        lrTable.addAction(103, "print_line", new Action("REDUCE", -1, "43"));
        lrTable.addAction(103, "display_line", new Action("REDUCE", -1, "43"));
        lrTable.addAction(103, "for", new Action("REDUCE", -1, "43"));
        lrTable.addAction(103, "while", new Action("REDUCE", -1, "43"));
        lrTable.addAction(104, "statement_terminator", new Action("SHIFT", 107, null));
        lrTable.addAction(105, "statement_terminator", new Action("REDUCE", -1, "23"));
        lrTable.addAction(106, "statement_terminator", new Action("REDUCE", -1, "24"));
        lrTable.addAction(107, "identifier", new Action("SHIFT", 108, null));
        lrTable.addAction(108, "plus_operator", new Action("SHIFT", 109, null));
        lrTable.addAction(109, "plus_operator", new Action("SHIFT", 110, null));
        lrTable.addAction(110, "right_parenthesis_operator", new Action("SHIFT", 111, null));
        lrTable.addAction(111, "begin", new Action("SHIFT", 112, null));
        lrTable.addAction(112, "identifier", new Action("SHIFT", 22, null));
        lrTable.addAction(112, "int", new Action("SHIFT", 21, null));
        lrTable.addAction(112, "if", new Action("SHIFT", 14, null));
        lrTable.addAction(112, "else", new Action("SHIFT", 15, null));
        lrTable.addAction(112, "print_line", new Action("SHIFT", 16, null));
        lrTable.addAction(112, "display_line", new Action("SHIFT", 18, null));
        lrTable.addAction(112, "for", new Action("SHIFT", 19, null));
        lrTable.addAction(112, "while", new Action("SHIFT", 20, null));
        lrTable.addGoto(112, "Body_Sequence", 113);
        lrTable.addGoto(112, "Body", 6);
        lrTable.addGoto(112, "Declaration_Sequence", 10);
        lrTable.addGoto(112, "Declaration_Statement", 17);
        lrTable.addGoto(112, "If_Statement", 7);
        lrTable.addGoto(112, "Else_Statement", 8);
        lrTable.addGoto(112, "Print_Statement", 9);
        lrTable.addGoto(112, "Display_Statement", 11);
        lrTable.addGoto(112, "For_Statement", 12);
        lrTable.addGoto(112, "While_Statement", 13);
        lrTable.addAction(113, "end", new Action("SHIFT", 114, null));
        lrTable.addAction(114, "identifier", new Action("REDUCE", -1, "42"));
        lrTable.addAction(114, "end", new Action("REDUCE", -1, "42"));
        lrTable.addAction(114, "int", new Action("REDUCE", -1, "42"));
        lrTable.addAction(114, "if", new Action("REDUCE", -1, "42"));
        lrTable.addAction(114, "else", new Action("REDUCE", -1, "42"));
        lrTable.addAction(114, "print_line", new Action("REDUCE", -1, "42"));
        lrTable.addAction(114, "display_line", new Action("REDUCE", -1, "42"));
        lrTable.addAction(114, "for", new Action("REDUCE", -1, "42"));
        lrTable.addAction(114, "while", new Action("REDUCE", -1, "42"));





		LRParser lrParser = new LRParser();
		lrParser.parse(lrTable,tokenList,productions);



	}

}
