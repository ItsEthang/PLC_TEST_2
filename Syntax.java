import java.io.File; // Import the File class

import java.io.*;

public class Syntax extends Lexeme {

    public static void main(String[] args) {
        try {
            in_fp = new File("test4.in");
            reader = new FileReader(in_fp);
            program();
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // * <program> → !b <stmtlist> !e
    static void program() {
        System.out.println("Enter <program>");
        // get the first character to get started
        getChar();
        // use lex() to read the next char
        lex();
        if (nextToken == BEGIN) {
            lex();
            stmtlist();
        } else {
            error();
        }
        // lex();
        if (nextToken == NEW_LINE) {
            lex();
        }
        if (nextToken == END) {
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
        if (nextToken == SEMI_COLON) {
            lex();
            stmtlist();
        }
        System.out.println("Exit <stmtlist>");
    }

    // * <stmt> → <if_stmt> | <while> | <declr> | <for>
    static void stmt() {
        System.out.println("Enter <stmt>");
        switch (nextToken) {
            case NEW_LINE:
                lex();
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
            default:
                error();
                break;
        }
        System.out.println("Exit <stmt>");
    }

    // * <if_stmt> → `$i``(`<bool_expr>`)`<stmt>
    static void if_stmt() {
        System.out.println("Enter <if_stmt>");
        if (nextToken != IF_KEY) {
            error();
        } else {
            lex();
            if (nextToken != LEFT_PAREN) {
                error();
            } else {
                lex();
                bool_expr();
                if (nextToken != RIGHT_PAREN) {
                    error();
                } else {
                    lex();
                    stmt();
                }
            }
        }
        System.out.println("Exit <if_stmt>");
    }

    // <while> → `$w``(`<bool_expr>`)`<stmt>
    static void while_l() {
        System.out.println("Enter <while_l>");
        if (nextToken != WHILE_KEY) {
            error();
        } else {
            lex();
            if (nextToken != LEFT_PAREN) {
                error();
            } else {
                lex();
                bool_expr();
                if (nextToken != RIGHT_PAREN) {
                    error();
                } else {
                    lex();
                    stmt();
                }
            }
        }
        System.out.println("Exit <while_l>");
    }

    // * <for> → `$f` `(` INT_TYPE IDEN `>>` INT_LIT `)` <stmt>
    static void for_l() {
        System.out.println("Enter <for_l>");
        if (nextToken != FOR_KEY) {
            error();
        } else {
            lex();
            if (nextToken != LEFT_PAREN) {
                error();
            } else {
                lex();
                if (nextToken != INT_TYPE) {
                    error();
                } else {
                    lex();
                    if (nextToken != IDEN) {
                        error();
                    } else {
                        lex();
                        if (nextToken != UNTIL) {
                            error();
                        } else {
                            lex();
                            if (nextToken != INT_LIT) {
                                error();
                            } else {
                                lex();
                                if (nextToken != RIGHT_PAREN) {
                                    error();
                                } else {
                                    lex();
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

    // * <declr> → INT_TYPE VAR_IDEN `=` <expr>
    static void declr() {
        System.out.println("Enter <declr>");
        if (nextToken != INT_TYPE) {
            error();
        } else {
            lex();
            if (nextToken != IDEN) {
                error();
            } else {
                lex();
                if (nextToken == ASSIGN_OP) {
                    lex();
                    expr();
                }
            }
        }
        System.out.println("Exit <declr>");
    }

    // * <expr> → <term> { (`*`|`+`) <term>}
    static void expr() {
        System.out.println("Enter <expr>");
        term();
        while (nextToken == MULT_OP || nextToken == ADD_OP) {
            lex();
            term();
        }
        System.out.println("Exit <expr>");
    }

    // * <term> → <factor> { (`\`|`-`|`%`) <factor>}
    static void term() {
        System.out.println("Enter <term>");
        factor();
        while (nextToken == DIV_OP || nextToken == SUB_OP || nextToken == MOD_OP) {
            lex();
            factor();
        }
        System.out.println("Exit <term>");
    }

    // * <factor> → VAR_IDEN | INT_LIT | `(` <expr> `)`
    static void factor() {
        System.out.println("Enter <factor>");
        if (nextToken == IDEN || nextToken == INT_LIT) {
            lex();
        } else {
            if (nextToken == LEFT_PAREN) {
                lex();
                expr();
                if (nextToken == RIGHT_PAREN)
                    lex();
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
        while (nextToken == NOT_EQUAL || nextToken == EQUAL_TO) {
            lex();
            brel();
        }
        System.out.println("Exit <bool_expr>");
    }

    // * <brel> → <bexpr> { (`>=`|`<=`|`<`|`>`) <bexpr>}
    static void brel() {
        System.out.println("Enter <brel>");
        bexpr();
        while (nextToken == GREATER_EQUAL || nextToken == LESS_EQUAL || nextToken == LESS_THAN
                || nextToken == GREATER_THAN) {
            lex();
            bexpr();
        }
        System.out.println("Exit <brel>");
    }

    // * <bexpr> → <bterm> { (`*`|`+`) <bterm>}
    static void bexpr() {
        System.out.println("Enter <bexpr>");
        bterm();
        while (nextToken == MULT_OP || nextToken == ADD_OP) {
            lex();
            bterm();
        }
        System.out.println("Exit <bexpr>");
    }

    // * <bterm> → <bfactor> { (`\`|`-`|`%`) <bfactor>}
    static void bterm() {
        System.out.println("Enter <bterm>");
        bfactor();
        while (nextToken == DIV_OP || nextToken == SUB_OP || nextToken == MOD_OP) {
            lex();
            bfactor();
        }
        System.out.println("Exit <bterm>");
    }

    // * <bfactor> → VAR_IDEN | INT_LIT | `(` <bool_expr> `)`
    static void bfactor() {
        System.out.println("Enter <bfactor>");
        if (nextToken == IDEN || nextToken == INT_LIT) {
            lex();
        } else {
            if (nextToken == LEFT_PAREN) {
                lex();
                bool_expr();
                if (nextToken == RIGHT_PAREN)
                    lex();
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
