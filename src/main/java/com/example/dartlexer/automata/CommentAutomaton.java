package com.example.dartlexer.automata;

import com.example.dartlexer.*;

public class CommentAutomaton {

    public static Token match(Lexer lexer) {
        if (lexer.peek() != '/') {
            return null;
        }

        if (lexer.isEof() || lexer.getPosition() + 1 >= lexer.getInput().length()) {
            lexer.advance();
            return new Token(TokenType.OPERATOR, "/", lexer.getLine(), lexer.getColumn());
        }

        char next = lexer.peekOffset(1);

        if (next == '/') {
            System.err.println(">>> 👉 Detectado // → comentario de línea");
            return matchLineComment(lexer);
        } else if (next == '*') {
            System.err.println(">>> 👉 Detectado /* → comentario de bloque");
            return matchBlockComment(lexer);
        } else {
            System.err.println(">>> 👉 Es operador de división");
            lexer.advance();
            return new Token(TokenType.OPERATOR, "/", lexer.getLine(), lexer.getColumn());
        }
    }

    private static Token matchLineComment(Lexer lexer) {
        int startLine = lexer.getLine();
        int startCol = lexer.getColumn();
        int startPos = lexer.getPosition();

        lexer.advance(); // consume '/'
        lexer.advance(); // consume '/'

        while (!lexer.isEof() && lexer.peek() != '\n') {
            lexer.advance();
        }

        // Consumir el salto de línea también
        if (!lexer.isEof() && lexer.peek() == '\n') {
            lexer.advance();
        }

        String comment = lexer.getInput().substring(startPos, lexer.getPosition());
        System.err.println(">>> Comentario de línea consumido: \"" + comment + "\" (desde " + startPos + " hasta " + lexer.getPosition() + ")");

        return new Token(TokenType.COMMENT, comment, startLine, startCol);
    }

    private static Token matchBlockComment(Lexer lexer) {
        int startLine = lexer.getLine();
        int startCol = lexer.getColumn();
        int startPos = lexer.getPosition();

        lexer.advance(); // consume '/'
        lexer.advance(); // consume '*'

        while (!lexer.isEof()) {
            if (lexer.peek() == '*' && lexer.peekOffset(1) == '/') {
                lexer.advance(); // consume '*'
                lexer.advance(); // consume '/'
                break;
            }
            if (lexer.peek() == '\n') {
                lexer.incrementLine();
            }
            lexer.advance();
        }

        String comment = lexer.getInput().substring(startPos, lexer.getPosition());

        return new Token(TokenType.COMMENT, comment, startLine, startCol);
    }
}