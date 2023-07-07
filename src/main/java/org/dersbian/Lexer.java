package org.dersbian;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lexer {
    private final String input;
    private int index = 0;
    private int line = 1;
    private int column = 1;
    private final int inputLen;
    private final List<Token> tokens;
    private final Set<String> keyWords = new HashSet<>(List.of("var", "val", "function", "return"));

    public Lexer(@NotNull String input) {
        this.input = input;
        this.inputLen = input.length();
        this.tokens = new ArrayList<>();
    }

    public void tokenize() {
        while (index < inputLen) {
            char currentChar = input.charAt(index);
            switch (currentChar) {
                case '+', '-', '*', ':', '(', ')', '{', '}', '=', '<', '>', ',', '[', ']', ';' -> {
                    tokens.add(new Token(TokenType.OPERATOR, Character.toString(currentChar), line, column));
                    index++;
                    column++;
                }
                case '\n' -> {
                    line++;
                    index++;
                    column = 1;
                }
                case '/' -> gestisciCommentoOSlash(currentChar);
                case '"' -> gestisciString();
                default ->{
                    if (Character.isDigit(currentChar)) {
                        gestisciNumeri();
                    } else if (Character.isLetter(currentChar) || currentChar == '_') {
                        gestisciIdentificatoriOKeyWord();
                    } else {
                        index++;
                        column++;
                    }
                }
            }
        }

        // Add EOF token
        tokens.add(new Token(TokenType.EOF, "", line, column));
    }

    private void gestisciIdentificatoriOKeyWord() {
        int startIndex = index;
        while (index < input.length() &&
                (Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
            index++;
            column++;
        }
        String identifier = input.substring(startIndex, index);
        if (keyWords.contains(identifier)) {
            tokens.add(new Token(TokenType.KEYWORD, identifier, line, column - identifier.length()));
        } else {
            tokens.add(new Token(TokenType.IDENTIFIER, identifier, line, column - identifier.length()));
        }
    }

    private void gestisciNumeri() {
        int startIndex = index;
        while (index < inputLen && Character.isDigit(input.charAt(index))) {
            index++;
            column++;
        }

        boolean isFloat = gestisciFloat();
        String number = input.substring(startIndex, index);
        if(isFloat){
            tokens.add(new Token(TokenType.FLOAT, number, line, column - number.length()));
        } else {
            tokens.add(new Token(TokenType.INTERO, number, line, column - number.length()));
        }
    }

    private boolean gestisciFloat() {
        boolean isFloats = false;
        if(input.charAt(index) == '.'){
            isFloats = true;
            index++;
            column++;
            while (index < inputLen && Character.isDigit(input.charAt(index))) {
                index++;
                column++;
            }
            if((input.charAt(index) == 'e' || input.charAt(index) == 'E') &&( input.charAt(index+1) == '+' || input.charAt(index+1) == '-')){
                index+=2;
                column+=2;
                while (index < inputLen && Character.isDigit(input.charAt(index))) {
                    index++;
                    column++;
                }
            }
        }
        return isFloats;
    }

    private void gestisciString() {
        int startIndex = index;
        index++; // Move past the opening double quote
        column++;

        while (index < input.length() && input.charAt(index) != '"') {
            if (input.charAt(index) == '\n') {
                line++;
                column = 1;
            }
            index++;
            column++;
        }

        if (index < input.length() && input.charAt(index) == '"') {
            index++; // Move past the closing double quote
            column++;
            String stringLiteral = input.substring(startIndex, index);
            tokens.add(new Token(TokenType.STRING, stringLiteral, line, column - stringLiteral.length()));
        } /*else {
            // Unterminated string literal, handle error or exception
        }*/
    }

    private void gestisciCommentoOSlash(char currentChar) {
        if (index + 1 < input.length() && input.charAt(index + 1) == '/') {
            // Ignore the rest of the line as a comment
            while (index < input.length() && input.charAt(index) != '\n') {
                index++;
            }
            line++;
            column = 1;
        } else {
            tokens.add(new Token(TokenType.OPERATOR, Character.toString(currentChar), line, column));
            index++;
            column++;
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
