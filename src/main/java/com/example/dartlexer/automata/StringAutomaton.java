package com.example.dartlexer.automata;

import com.example.dartlexer.*;

public class StringAutomaton {

    public static Token match(Lexer lexer) {
        char quote = lexer.peek();
        int startLine = lexer.getLine();
        int startCol = lexer.getColumn();
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(quote);
        lexer.advance(); // consume comilla inicial

        while (!lexer.isEof()) {
            char c = lexer.peek();

            if (c == quote) {
                lexeme.append(c);
                lexer.advance();
                break;
            }

            if (c == '\\') { // Carácter de escape
                lexeme.append(c);
                lexer.advance();
                if (!lexer.isEof()) {
                    lexeme.append(lexer.peek());
                    lexer.advance();
                }
                continue;
            }

            if (c == '\n') {
                lexer.incrementLine();
            }

            lexeme.append(c);
            lexer.advance();
        }

        return new Token(TokenType.STRING, lexeme.toString(), startLine, startCol);
    }
}