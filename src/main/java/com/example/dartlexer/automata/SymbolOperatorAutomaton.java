package com.example.dartlexer.automata;

import com.example.dartlexer.*;
import java.util.Set;

public class SymbolOperatorAutomaton {

    private static final Set<String> OPERATORS = Set.of(
            "==", "!=", "<=", ">=", "++", "--", "+=", "-=", "*=", "/=",
            "&&", "||", "??", "?.", "...", "=>", "~/", "<<", ">>"
    );

    public static Token match(Lexer lexer) {
        int startCol = lexer.getColumn();
        char c1 = lexer.peek();
        lexer.advance();

        // Intentar operador de 2 caracteres
        if (!lexer.isEof()) {
            char c2 = lexer.peek();
            String twoChar = "" + c1 + c2;
            if (OPERATORS.contains(twoChar)) {
                lexer.advance();
                return new Token(TokenType.OPERATOR, twoChar, lexer.getLine(), startCol);
            }
        }

        // Operador o símbolo de 1 carácter
        String single = String.valueOf(c1);
        TokenType type = isOperatorChar(c1) ? TokenType.OPERATOR : TokenType.SYMBOL;
        return new Token(type, single, lexer.getLine(), startCol);
    }

    private static boolean isOperatorChar(char c) {
        return "+-*/=!<>%&|^.~?:@#".indexOf(c) >= 0;
    }
}