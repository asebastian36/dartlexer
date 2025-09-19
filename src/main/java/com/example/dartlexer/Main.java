package com.example.dartlexer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String code = """
            // Programa de prueba
            void main() {
              int edad = 25;
              double pi = 3.1416;
              String nombre = "Ana";
              bool activo = true;

              if (edad > 18 && activo) {
                print('Hola $nombre, bienvenido!');
              }
              
              /* final  1 / 1 private void 
                comentario extenso
              */  
                
              /* Comentario
                 de bloque */
              var x = 10 ~/ 3; // división entera
              
              if
              private
              
              void
            }
            """;

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        System.out.println("=== TOKENS ENCONTRADOS ===");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}