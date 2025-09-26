package com.example.dartlexer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Selecciona un archivo .dart");
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos Dart", "dart"));

            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File inputFile = chooser.getSelectedFile();
                String outputFileName = inputFile.getAbsolutePath().replace(".dart", "_tokens.txt");
                File outputFile = new File(outputFileName);

                try {
                    String code = new String(Files.readAllBytes(inputFile.toPath()));
                    Lexer lexer = new Lexer(code);
                    List<Token> tokens = lexer.tokenize();

                    try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
                        for (Token token : tokens) {
                            writer.println(token);
                        }
                    }

                    JOptionPane.showMessageDialog(null,
                            "¡Listo!\nTokens guardados en:\n" + outputFileName,
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "Error: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}