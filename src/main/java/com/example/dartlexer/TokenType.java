package com.example.dartlexer;

public enum TokenType {
    KEYWORD,        // Palabra reservada de Dart
    IDENTIFIER,     // Nombre de variable, función, clase, etc.
    NUMBER,         // Literal numérico (entero o decimal)
    STRING,         // Literal de cadena
    COMMENT,        // Comentario de línea o bloque
    OPERATOR,       // Operadores: +, -, ==, &&, etc.
    SYMBOL,         // Símbolos: { } ( ) ; , . etc.
    WHITESPACE,     // Espacios, saltos de línea (ignorables)
    UNKNOWN         // Carácter no reconocido
}