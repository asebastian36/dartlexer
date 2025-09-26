package com.example.dartlexer;

import com.example.dartlexer.automata.*;
import java.util.*;
import java.util.function.Function;

public class Lexer {
    private final String input;
    private int position;
    private int line;
    private int column;

    private static final BinarySearchTree KEYWORDS = initKeywordTree();

    private static BinarySearchTree initKeywordTree() {
        BinarySearchTree tree = new BinarySearchTree();
        String[] keywords = {
                "abstract", "as", "assert", "async", "await", "break", "case", "catch",
                "class", "const", "continue", "default", "deferred", "do", "dynamic",
                "else", "enum", "export", "extends", "extension", "external", "factory",
                "false", "final", "finally", "for", "Function", "get", "hide", "if",
                "implements", "import", "in", "interface", "is", "late", "library",
                "mixin", "new", "null", "on", "operator", "part", "required", "rethrow",
                "return", "set", "show", "static", "super", "switch", "sync", "this",
                "throw", "true", "try", "typedef", "var", "void", "while", "with", "yield"
        };
        for (String kw : keywords) {
            tree.insert(kw);
        }
        return tree;
    }

    private static final Map<Character, Function<Lexer, Token>> DISPATCH_TABLE = initDispatchTable();

    private static Map<Character, Function<Lexer, Token>> initDispatchTable() {
        Map<Character, Function<Lexer, Token>> map = new HashMap<>();

        // Letras y _
        for (char c = 'a'; c <= 'z'; c++) map.put(c, IdentifierAutomaton::match);
        for (char c = 'A'; c <= 'Z'; c++) map.put(c, IdentifierAutomaton::match);
        map.put('_', IdentifierAutomaton::match);

        // Dígitos
        for (char c = '0'; c <= '9'; c++) map.put(c, NumberAutomaton::match);

        // Comillas
        map.put('"', StringAutomaton::match);
        map.put('\'', StringAutomaton::match);

        // Símbolos y operadores (incluyendo /, pero lo sobrescribiremos después)
        String symbols = "+-*/=!<>%&|(){}[],;:.^~?@#";
        for (char c : symbols.toCharArray()) {
            map.put(c, SymbolOperatorAutomaton::match);
        }

        // ¡Sobrescribimos '/' para que use CommentAutomaton!
        map.put('/', CommentAutomaton::match);

        return map;
    }
    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (!isEof()) {
            System.err.println("=== INICIO CICLO tokenize - posición: " + position + ", char: '" + (isEof() ? "" : peek()) + "'");
            skipWhitespace();
            if (isEof()) break;

            char current = peek();
            System.err.println(">>> Carácter actual: '" + current + "' en pos=" + position + ", línea=" + line + ", col=" + column);

            Function<Lexer, Token> automaton = DISPATCH_TABLE.get(current);

            if (automaton != null) {
                System.err.println(">>> Llamando autómata para: '" + current + "'");
                Token token = automaton.apply(this);
                if (token != null) {
                    System.err.println(">>> Token generado: " + token);
                    tokens.add(token);
                } else {
                    System.err.println(">>> Autómata devolvió null, agregando UNKNOWN");
                    tokens.add(new Token(TokenType.UNKNOWN, String.valueOf(current), line, column));
                    advance();
                }
            } else {
                System.err.println(">>> Sin autómata para '" + current + "', agregando UNKNOWN");
                tokens.add(new Token(TokenType.UNKNOWN, String.valueOf(current), line, column));
                advance();
            }
        }
        return tokens;
    }

    public char peek() {
        if (position >= input.length()) return '\0';
        return input.charAt(position);
    }

    public char peekOffset(int offset) {
        int idx = position + offset;
        char c = idx < input.length() ? input.charAt(idx) : '\0';
        System.err.println(">>> [peekOffset] position=" + position + ", offset=" + offset + " → idx=" + idx + " → char='" + c + "'");
        return c;
    }
    public void advance() {
        System.err.println(">>> [advance] posición antes: " + position + ", char: '" + peek() + "'");
        if (peek() == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        position++;
        System.err.println(">>> [advance] posición después: " + position);
    }

    public void skipWhitespace() {
        System.err.println(">>> [skipWhitespace] posición inicial: " + position);
        while (!isEof() && Character.isWhitespace(peek())) {
            System.err.println(">>> Saltando whitespace: '" + peek() + "' en pos=" + position);
            if (peek() == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
            position++;
        }
        System.err.println(">>> [skipWhitespace] posición final: " + position);
    }

    public boolean isEof() {
        return position >= input.length();
    }

    public boolean isKeyword(String word) {
        return KEYWORDS.contains(word);
    }

    public String getInput() { return input; }
    public int getPosition() { return position; }
    public int getLine() { return line; }
    public int getColumn() { return column; }
    public void incrementLine() { line++; }
}