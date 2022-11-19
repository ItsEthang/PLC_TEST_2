import java.io.File; // Import the File class
import java.io.*;

public class Lexeme {

    /* Global declarations */
    /* Variables */
    static int charClass;
    // static ArrayList<Character> lexeme = new ArrayList<Character>();
    static char lexeme[] = new char[100];
    static char nextChar;
    static int lexLen;
    static int token;
    static int nextToken;
    static File in_fp;
    static Reader reader;
    static int data;
    static String string;

    /* Character classes */
    final static int LETTER = 0;
    final static int DIGIT = 1;
    final static int UNDERSCORE = 2;
    final static int NEW_LINE = 3;
    final static int SEMI_COLON = 4;
    final static int UNKNOWN = 99;
    final static int BEGIN = 100;
    final static int END = 101;
    final static int INVALID = 444;

    // Int literals and types
    final static int INT_LIT = 10;
    final static int INT_TYPE_BYTE = 11;
    final static int INT_TYPE_WORD = 12;
    final static int INT_TYPE_DWORD = 13;
    final static int INT_TYPE_QWORD = 14;
    final static int INT_TYPE = 15;
    final static int IDEN = 19;

    // Operators
    final static int ASSIGN_OP = 20;
    final static int ADD_OP = 21;
    final static int SUB_OP = 22;
    final static int DIV_OP = 23;
    final static int MULT_OP = 24;
    final static int MOD_OP = 25;
    final static int LEFT_PAREN = 26;
    final static int RIGHT_PAREN = 27;

    // Comparisons
    final static int EQUAL_TO = 30;
    final static int NOT_EQUAL = 31;
    final static int LESS_THAN = 32;
    final static int GREATER_THAN = 33;
    final static int LESS_EQUAL = 34;
    final static int GREATER_EQUAL = 35;
    final static int UNTIL = 36;

    // If, for, while key words
    final static int IF_KEY = 40;
    final static int FOR_KEY = 41;
    final static int WHILE_KEY = 42;

    static String keywords[] = new String[] { "#b", "#w", "#d", "#q", "$i", "$f", "$w" };

    public static void main(String[] args) {
        try {
            in_fp = new File("test1.in");
            reader = new FileReader(in_fp);
            getChar();
            do {
                lex();
            } while (nextToken != END);
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // for resetting the char array
    static void clearArr(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = '\0';
        }
    }

    // add character to the char array lexeme
    static void addChar() {
        if (lexLen <= 98) {
            lexeme[lexLen++] = nextChar;
        } else {
            System.out.print("Error - lexeme is too long \n");
        }
    }

    // read the next character in the file
    static void getChar() {
        try {
            data = reader.read();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        if (data != -1) {
            nextChar = (char) data;
            // checking for the character's class
            if (Character.isDigit(nextChar)) {
                charClass = DIGIT;
            } else if (Character.isLetter(nextChar)) {
                charClass = LETTER;
            } else if (nextChar == '_') {
                charClass = UNDERSCORE;
            } else if (nextChar == '\n') {
                charClass = NEW_LINE;
            } else if (nextChar == ';') {
                charClass = SEMI_COLON;
            } else {
                charClass = UNKNOWN;
            }
        } else {
            charClass = END;
        }
    }

    static int lookup(char ch) {
        switch (ch) {
            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;
            case '+':
                addChar();
                nextToken = ADD_OP;
                break;
            case '-':
                addChar();
                nextToken = SUB_OP;
                break;
            case '*':
                addChar();
                nextToken = MULT_OP;
                break;
            case '/':
                addChar();
                nextToken = DIV_OP;
                break;
            case '%':
                addChar();
                nextToken = MOD_OP;
                break;
            case '=':
                // add the current character into lexeme string first
                addChar();
                // get the next character
                getChar();
                if (nextChar == '=') {
                    // add the valid char input to lexeme
                    addChar();
                    nextToken = EQUAL_TO;
                } else if (Character.isSpaceChar(nextChar)) {
                    // if next char is a space then it is a simple case
                    nextToken = ASSIGN_OP;
                } else {
                    addChar();
                    nextToken = INVALID;
                }
                break;
            case '>':
                // add the current character into lexeme string first
                addChar();
                // get the next character
                getChar();
                if (nextChar == '=') {
                    // add the valid char input to lexeme
                    addChar();
                    nextToken = GREATER_EQUAL;
                } else if (Character.isSpaceChar(nextChar)) {
                    // if next char is a space then it is a simple case
                    nextToken = GREATER_THAN;
                } else if (nextChar == '>') {
                    addChar();
                    nextToken = UNTIL;
                } else {
                    addChar();
                    nextToken = INVALID;
                }
                break;
            case '<':
                // add the current character into lexeme string first
                addChar();
                // get the next character
                getChar();
                if (nextChar == '=') {
                    // add the valid char input to lexeme
                    addChar();
                    nextToken = LESS_EQUAL;
                } else if (Character.isSpaceChar(nextChar)) {
                    // if next char is a space then it is a simple case
                    nextToken = LESS_THAN;
                } else {
                    addChar();
                    nextToken = INVALID;
                }
                break;
            case '!':
                // add the current character into lexeme string first
                addChar();
                // get the next character
                getChar();
                if (nextChar == '=') {
                    // add the valid char input to lexeme
                    addChar();
                    nextToken = NOT_EQUAL;
                } else if (nextChar == 'b') {
                    // add the valid char input to lexeme
                    addChar();
                    nextToken = BEGIN;
                } else if (nextChar == 'e') {
                    // add the valid char input to lexeme
                    addChar();
                    nextToken = END;
                } else {
                    addChar();
                    nextToken = INVALID;
                }
                break;
            case '$':
                // add the current character into lexeme string first
                addChar();
                // get the next character
                getChar();
                if (nextChar == 'i') {
                    // add the valid char input to lexeme
                    addChar();
                    nextToken = IF_KEY;
                } else if (nextChar == 'f') {
                    addChar();
                    nextToken = FOR_KEY;
                } else if (nextChar == 'w') {
                    addChar();
                    nextToken = WHILE_KEY;
                } else {
                    addChar();
                    nextToken = INVALID;
                }
                break;
            case '#':
                // add the current character into lexeme string first
                addChar();
                // get the next character
                getChar();
                if (nextChar == 'b') {
                    // add the valid char input to lexeme
                    addChar();
                    nextToken = INT_TYPE;
                } else if (nextChar == 'w') {
                    addChar();
                    nextToken = INT_TYPE;
                } else if (nextChar == 'd') {
                    addChar();
                    nextToken = INT_TYPE;
                } else if (nextChar == 'q') {
                    addChar();
                    nextToken = INT_TYPE;
                } else {
                    addChar();
                    nextToken = INVALID;
                }
                break;
            default:
                addChar();
                nextToken = INVALID;
                break;
        }
        return nextToken;
    }

    static void getNonBlank() {
        while (Character.isSpaceChar(nextChar)) {
            getChar();
        }
    }

    static int lex() {
        lexLen = 0;
        clearArr(lexeme);
        getNonBlank();
        int idLen = 0;
        switch (charClass) {
            // identifier
            case LETTER:
                addChar();
                idLen += 1;
                getChar();
                while (charClass == LETTER || charClass == UNDERSCORE) {
                    addChar();
                    idLen += 1;
                    getChar();
                }
                if ((idLen >= 6) && (idLen <= 8)) {
                    nextToken = IDEN;
                } else {
                    nextToken = INVALID;
                }
                break;
            case UNDERSCORE:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == UNDERSCORE) {
                    addChar();
                    getChar();
                }
                nextToken = IDEN;
                break;
            // Int literals
            case DIGIT:
                addChar();
                getChar();
                while (charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;
            case NEW_LINE:
                nextToken = NEW_LINE;
                addChar();
                getChar();
                break;
            case SEMI_COLON:
                nextToken = SEMI_COLON;
                addChar();
                getChar();
                break;
            /* Everything Else */
            case UNKNOWN:
                lookup(nextChar);
                getChar();
                break;
            /* EOF */
            case END:
                nextToken = END;
                lexeme[0] = 'E';
                lexeme[1] = 'N';
                lexeme[2] = 'D';
                lexeme[3] = '\0';
                break;
        } /* End of switch */
        string = new String(lexeme);
        System.out.println("Next token is: " + nextToken + ", Next lexeme is: " + string);
        return nextToken;
    }
}
