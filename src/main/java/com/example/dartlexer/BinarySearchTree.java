package com.example.dartlexer;

public class BinarySearchTree {
    private Node root;

    private static class Node {
        String word;
        Node left, right;

        Node(String word) {
            this.word = word;
        }
    }

    public void insert(String word) {
        root = insertRec(root, word);
    }

    private Node insertRec(Node node, String word) {
        if (node == null) {
            return new Node(word);
        }
        int cmp = word.compareTo(node.word);
        if (cmp < 0) {
            node.left = insertRec(node.left, word);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, word);
        }
        // Si son iguales, no insertamos duplicados
        return node;
    }

    public boolean contains(String word) {
        return containsRec(root, word);
    }

    private boolean containsRec(Node node, String word) {
        if (node == null) return false;
        int cmp = word.compareTo(node.word);
        if (cmp == 0) return true;
        if (cmp < 0) return containsRec(node.left, word);
        return containsRec(node.right, word);
    }
}