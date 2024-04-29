import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SmallLexer {
    public static ArrayList<String> lexIntoTokenList(String file) throws IOException {
        System.out.println("\n");
        System.out.println("Processing: " + file);
        String content = Files.readString(Paths.get(file));
        //intialize all Lexemes
        Identifier identifier = new Identifier();
        Comment comment = new Comment();
        StringLiteral stringLiteral = new StringLiteral();
        NumberLiteral numberLiteral = new NumberLiteral();

        LexemeProcessor checker = new LexemeProcessor();

        LexerContext currentInputState = new LexerContext(content);
        LexerContext lexeme_check = new LexerContext(content);
        //go through the whole string until there is nothing left to check
        while(!currentInputState.getNonConsumed().isEmpty()) {
            if (!currentInputState.getConsumed().isEmpty() && CharacterList.blankTokenDelimeterList.contains(currentInputState.getNonConsumed().charAt(0))) {
                currentInputState.setNonConsumed(currentInputState.getNonConsumed().stripLeading());
            }
            //checking for identifier
            else if (Character.isAlphabetic(currentInputState.getNonConsumed().charAt(0)) || currentInputState.getNonConsumed().charAt(0) == '$') {
                lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                if (!lexeme_check.getError()) {
                    currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                    checker.correctOutputWithGivenLexeme(currentInputState);
                    checker.printTokenDelimeters(currentInputState.getDelimeter());
                }
            } // checking for comment
            else if (currentInputState.getNonConsumed().charAt(0) == '-') {
                lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), comment);
                if (!lexeme_check.getError()) {
                    currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), comment);
                    checker.printTokenDelimeters(currentInputState.getDelimeter());
                    checker.correctOutputWithGivenLexeme(currentInputState);
                }
            } //checking for String Literal
            else if (currentInputState.getNonConsumed().charAt(0) == '"') {
                lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), stringLiteral);
                if (!lexeme_check.getError()) {
                    currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), stringLiteral);
                    checker.printTokenDelimeters(currentInputState.getDelimeter());
                    checker.correctOutputWithGivenLexeme(currentInputState);
                }
            } // checking for number literal
            else if (Character.isDigit(currentInputState.getNonConsumed().charAt(0))) {
                lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), numberLiteral);
                if (!lexeme_check.getError()) {
                    currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), numberLiteral);
                    checker.correctOutputWithGivenLexeme(currentInputState);
                    checker.printTokenDelimeters(currentInputState.getDelimeter());
                } else {
                    lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                    if (lexeme_check.getError()) {
                        currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                        if (currentInputState.getConsumed().charAt(0) != '$' || !Character.isAlphabetic(currentInputState.getConsumed().charAt(0))) {
                            System.out.println(currentInputState.getConsumed() + "             illegal ID starting with wrong character");
                            checker.printTokenDelimeters(currentInputState.getDelimeter());
                        }
                    }
                }
            } // if first character is token delimiter print out the output and then concatenate
            else if (CharacterList.realTokenDelimeters.contains(currentInputState.getNonConsumed().charAt(0))) {
                checker.printTokenDelimeters(currentInputState.getNonConsumed().charAt(0));
                currentInputState.setNonConsumed(currentInputState.getNonConsumed().substring(1));
            } else {
                lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                if (lexeme_check.getError()) {
                    currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                    if (currentInputState.getConsumed().charAt(0) != '$' || !Character.isAlphabetic(currentInputState.getConsumed().charAt(0))) {
                        System.out.println(currentInputState.getConsumed() + "             illegal ID starting with wrong character");
                        checker.printTokenDelimeters(currentInputState.getDelimeter());
                    }
                }
            }
        }
//        System.out.print(checker.getTokenList());
        return checker.getTokenList();
    }
   /* public static void main(String[] args) throws IOException {
	    String[] files = {"Input1.txt", "Input2.txt", "Input1error.txt", "Input2error.txt","Input3.txt" };
        for (String file : files) {
			System.out.println("\n");
            System.out.println("Processing: " + file);
            String content = Files.readString(Paths.get(file));
            //intialize all Lexemes
            Identifier identifier = new Identifier();
            Comment comment = new Comment();
            StringLiteral stringLiteral = new StringLiteral();
            NumberLiteral numberLiteral = new NumberLiteral();

            LexemeProcessor checker = new LexemeProcessor();

            LexerContext currentInputState = new LexerContext(content);
            LexerContext lexeme_check = new LexerContext(content);
            //go through the whole string until there is nothing left to check
            while(!currentInputState.getNonConsumed().isEmpty()) {
                if (!currentInputState.getConsumed().isEmpty() && CharacterList.blankTokenDelimeterList.contains(currentInputState.getNonConsumed().charAt(0))) {
                    currentInputState.setNonConsumed(currentInputState.getNonConsumed().stripLeading());
                }
                //checking for identifier
                else if (Character.isAlphabetic(currentInputState.getNonConsumed().charAt(0)) || currentInputState.getNonConsumed().charAt(0) == '$') {
                    lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                    if (!lexeme_check.getError()) {
                        currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                        checker.printTokenDelimeters(currentInputState.getDelimeter());
                        checker.correctOutputWithGivenLexeme(currentInputState);
                    }
                } // checking for comment
                else if (currentInputState.getNonConsumed().charAt(0) == '-') {
                    lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), comment);
                    if (!lexeme_check.getError()) {
                        currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), comment);
                        checker.printTokenDelimeters(currentInputState.getDelimeter());
                        checker.correctOutputWithGivenLexeme(currentInputState);
                    }
                } //checking for String Literal
                else if (currentInputState.getNonConsumed().charAt(0) == '"') {
                    lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), stringLiteral);
                    if (!lexeme_check.getError()) {
                        currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), stringLiteral);
                        checker.printTokenDelimeters(currentInputState.getDelimeter());
                        checker.correctOutputWithGivenLexeme(currentInputState);
                    }
                } // checking for number literal
                else if (Character.isDigit(currentInputState.getNonConsumed().charAt(0))) {
                    lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), numberLiteral);
                    if (!lexeme_check.getError()) {
                        currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), numberLiteral);
                        checker.printTokenDelimeters(currentInputState.getDelimeter());
                        checker.correctOutputWithGivenLexeme(currentInputState);
                    } else {
                        lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                        if (lexeme_check.getError()) {
                            currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                            if (currentInputState.getConsumed().charAt(0) != '$' || !Character.isAlphabetic(currentInputState.getConsumed().charAt(0))) {
                                System.out.println(currentInputState.getConsumed() + "             illegal ID starting with wrong character");
                                checker.printTokenDelimeters(currentInputState.getDelimeter());
                            }
                        }
                    }
                } // if first character is token delimiter print out the output and then concatenate
                else if (CharacterList.realTokenDelimeters.contains(currentInputState.getNonConsumed().charAt(0))) {
                    checker.printTokenDelimeters(currentInputState.getNonConsumed().charAt(0));
                    currentInputState.setNonConsumed(currentInputState.getNonConsumed().substring(1));
                } else {
                    lexeme_check = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                    if (lexeme_check.getError()) {
                        currentInputState = checker.check_lexeme(currentInputState.getNonConsumed(), identifier);
                        if (currentInputState.getConsumed().charAt(0) != '$' || !Character.isAlphabetic(currentInputState.getConsumed().charAt(0))) {
                            System.out.println(currentInputState.getConsumed() + "             illegal ID starting with wrong character");
                            checker.printTokenDelimeters(currentInputState.getDelimeter());
                        }
                    }
                }
            }
            System.out.print(checker.getTokenList());
        }
    }*/
}
