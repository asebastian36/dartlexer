package com.example.dartlexer.automata;

import com.example.dartlexer.*;

public class NumberAutomaton {

    public static Token match(Lexer lexer) {
        int startCol = lexer.getColumn();
        StringBuilder lexeme = new StringBuilder();

        // Parte entera
        while (!lexer.isEof() && Character.isDigit(lexer.peek())) {
            lexeme.append(lexer.peek());
            lexer.advance();
        }

        // ¿Punto decimal?
        if (!lexer.isEof() && lexer.peek() == '.') {
            lexeme.append(lexer.peek());
            lexer.advance();
            while (!lexer.isEof() && Character.isDigit(lexer.peek())) {
                lexeme.append(lexer.peek());
                lexer.advance();
            }
        }

        // ¿Notación científica? (e o E, opcional + o -)
        if (!lexer.isEof() && (lexer.peek() == 'e' || lexer.peek() == 'E')) {
            lexeme.append(lexer.peek());
            lexer.advance();
            if (!lexer.isEof() && (lexer.peek() == '+' || lexer.peek() == '-')) {
                lexeme.append(lexer.peek());
                lexer.advance();
            }
            while (!lexer.isEof() && Character.isDigit(lexer.peek())) {
                lexeme.append(lexer.peek());
                lexer.advance();
            }
        }

        return new Token(TokenType.NUMBER, lexeme.toString(), lexer.getLine(), startCol);
    }
}