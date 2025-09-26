package com.example.dartlexer.automata;

import com.example.dartlexer.*;

public class IdentifierAutomaton {

    public static Token match(Lexer lexer) {
        int startCol = lexer.getColumn();
        StringBuilder lexeme = new StringBuilder();

        char c = lexer.peek();
        while (Character.isLetterOrDigit(c) || c == '_') {
            lexeme.append(c);
            lexer.advance();
            if (lexer.isEof()) break;
            c = lexer.peek();
        }

        String text = lexeme.toString();
        TokenType type = lexer.isKeyword(text) ? TokenType.KEYWORD : TokenType.IDENTIFIER;

        return new Token(type, text, lexer.getLine(), startCol);
    }
}