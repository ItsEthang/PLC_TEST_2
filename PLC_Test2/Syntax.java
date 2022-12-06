import java.io.File; // Import the File class
import java.io.*;

public class Syntax extends Lexeme {

    static int index = 0;
    static int currentToken;

    public static void main(String[] args) {

        try {
            in_fp = new File("test3.in");
            reader = new FileReader(in_fp);
            getChar();
            do {
                lex();
            } while (nextToken != END);
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Token List: ");
        System.out.println(tokenList);

        // Don't run the program if lexeme error exists
        if (!lexemeError()) {
            program();
        } else {
            System.out.println("Lexeme Error Detected. Fail to run the program");
        }

    }

    // lexeme error check
    static boolean lexemeError() {
        for (int i = 0; i < tokenList.size(); i++) {
            if (tokenList.get(i) == INVALID) {
                return true;
            }
        }
        return false;
    }

    // get the next token in the list
    static void getLextToken() {
        index += 1;
        currentToken = tokenList.get(index);
    }

    // * <program> → !b <stmtlist> !e
    static void program() {
        // assign the first token
        currentToken = tokenList.get(index);
        System.out.println("Enter <program>");
        if (currentToken == BEGIN) {
            getLextToken();
            stmtlist();
        } else {
            error();
        }
        // getLextToken();
        if (currentToken == NEW_LINE) {
            getLextToken();
        }
        if (currentToken == END) {
            System.out.println("Program Had Successfully Ended");
        } else {
            error();
        }
        System.out.println("Exit <program>");
    }

    // * <stmtlist> → <stmt> | <stmt>; <stmtlist>
    static void stmtlist() {
        System.out.println("Enter <stmtlist>");
        stmt();
        if (currentToken == SEMI_COLON) {
            getLextToken();
            stmtlist();
        }
        System.out.println("Exit <stmtlist>");
    }

    // * <stmt> → <if_stmt> | <while> | <declr> | <for>
    static void stmt() {
        System.out.println("Enter <stmt>");
        switch (currentToken) {
            case NEW_LINE:
                getLextToken();
                stmt();
                break;
            case IF_KEY:
                if_stmt();
                break;
            case WHILE_KEY:
                while_l();
                break;
            case INT_TYPE:
                declr();
                break;
            case FOR_KEY:
                for_l();
                break;
            case IDEN:
                assign();
                break;
            default:
                error();
                break;
        }
        System.out.println("Exit <stmt>");
    }

    // * <if_stmt> → `$i``(`<bool_expr>`)`<stmt>
    static void if_stmt() {
        System.out.println("Enter <if_stmt>");
        if (currentToken != IF_KEY) {
            error();
        } else {
            getLextToken();
            if (currentToken != LEFT_PAREN) {
                error();
            } else {
                getLextToken();
                bool_expr();
                if (currentToken != RIGHT_PAREN) {
                    error();
                } else {
                    getLextToken();
                    stmt();
                }
            }
        }
        System.out.println("Exit <if_stmt>");
    }

    // <while> → `$w``(`<bool_expr>`)`<stmt>
    static void while_l() {
        System.out.println("Enter <while_l>");
        if (currentToken != WHILE_KEY) {
            error();
        } else {
            getLextToken();
            if (currentToken != LEFT_PAREN) {
                error();
            } else {
                getLextToken();
                bool_expr();
                if (currentToken != RIGHT_PAREN) {
                    error();
                } else {
                    getLextToken();
                    stmt();
                }
            }
        }
        System.out.println("Exit <while_l>");
    }

    // * <for> → `$f` `(` INT_TYPE IDEN `>>` INT_LIT `)` <stmt>
    static void for_l() {
        System.out.println("Enter <for_l>");
        if (currentToken != FOR_KEY) {
            error();
        } else {
            getLextToken();
            if (currentToken != LEFT_PAREN) {
                error();
            } else {
                getLextToken();
                if (currentToken != INT_TYPE) {
                    error();
                } else {
                    getLextToken();
                    if (currentToken != IDEN) {
                        error();
                    } else {
                        getLextToken();
                        if (currentToken != UNTIL) {
                            error();
                        } else {
                            getLextToken();
                            if (currentToken != INT_LIT) {
                                error();
                            } else {
                                getLextToken();
                                if (currentToken != RIGHT_PAREN) {
                                    error();
                                } else {
                                    getLextToken();
                                    stmt();
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Exit <for_l>");
    }

    // <declr> → INT_TYPE VAR_IDEN ( `=` <expr> )
    static void declr() {
        System.out.println("Enter <declr>");
        if (currentToken != INT_TYPE) {
            error();
        } else {
            getLextToken();
            if (currentToken != IDEN) {
                error();
            } else {
                getLextToken();
                if (currentToken == ASSIGN_OP) {
                    getLextToken();
                    expr();
                }
            }
        }
        System.out.println("Exit <declr>");
    }

    // <assign> → VAR_IDEN `=` <expr>
    static void assign() {
        System.out.println("Enter <assign>");
        if (currentToken != IDEN) {
            error();
        } else {
            getLextToken();
            if (currentToken != ASSIGN_OP) {
                error();
            } else {
                getLextToken();
                expr();
            }
        }
        System.out.println("Exit <assign>");
    }

    // * <expr> → <term> { (`*`|`+`) <term>}
    static void expr() {
        System.out.println("Enter <expr>");
        term();
        while (currentToken == MULT_OP || currentToken == ADD_OP) {
            getLextToken();
            term();
        }
        System.out.println("Exit <expr>");
    }

    // * <term> → <factor> { (`\`|`-`|`%`) <factor>}
    static void term() {
        System.out.println("Enter <term>");
        factor();
        while (currentToken == DIV_OP || currentToken == SUB_OP || currentToken == MOD_OP) {
            getLextToken();
            factor();
        }
        System.out.println("Exit <term>");
    }

    // * <factor> → VAR_IDEN | INT_LIT | `(` <expr> `)`
    static void factor() {
        System.out.println("Enter <factor>");
        if (currentToken == IDEN || currentToken == INT_LIT) {
            getLextToken();
        } else {
            if (currentToken == LEFT_PAREN) {
                getLextToken();
                expr();
                if (currentToken == RIGHT_PAREN)
                    getLextToken();
                else
                    error();
            } else {
                error();
            }
        }
        System.out.println("Exit <factor>");
    }

    // * <bool_expr> → <brel> { (`!=`|`==`) <brel>}
    static void bool_expr() {
        System.out.println("Enter <bool_expr>");
        brel();
        while (currentToken == NOT_EQUAL || currentToken == EQUAL_TO) {
            getLextToken();
            brel();
        }
        System.out.println("Exit <bool_expr>");
    }

    // * <brel> → <bexpr> { (`>=`|`<=`|`<`|`>`) <bexpr>}
    static void brel() {
        System.out.println("Enter <brel>");
        bexpr();
        while (currentToken == GREATER_EQUAL || currentToken == LESS_EQUAL || currentToken == LESS_THAN
                || currentToken == GREATER_THAN) {
            getLextToken();
            bexpr();
        }
        System.out.println("Exit <brel>");
    }

    // * <bexpr> → <bterm> { (`*`|`+`) <bterm>}
    static void bexpr() {
        System.out.println("Enter <bexpr>");
        bterm();
        while (currentToken == MULT_OP || currentToken == ADD_OP) {
            getLextToken();
            bterm();
        }
        System.out.println("Exit <bexpr>");
    }

    // * <bterm> → <bfactor> { (`\`|`-`|`%`) <bfactor>}
    static void bterm() {
        System.out.println("Enter <bterm>");
        bfactor();
        while (currentToken == DIV_OP || currentToken == SUB_OP || currentToken == MOD_OP) {
            getLextToken();
            bfactor();
        }
        System.out.println("Exit <bterm>");
    }

    // * <bfactor> → VAR_IDEN | INT_LIT | `(` <bool_expr> `)`
    static void bfactor() {
        System.out.println("Enter <bfactor>");
        if (currentToken == IDEN || currentToken == INT_LIT) {
            getLextToken();
        } else {
            if (currentToken == LEFT_PAREN) {
                getLextToken();
                bool_expr();
                if (currentToken == RIGHT_PAREN)
                    getLextToken();
                else
                    error();
            } else {
                error();
            }
        }
        System.out.println("Exit <bfactor>");
    }

    static void error() {
        System.out.println("Syntax Error Detected");
    }

}
