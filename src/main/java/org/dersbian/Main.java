package org.dersbian;

import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        String code = """
            x = 2
            var x
            var y
            var f = function(x, y) { sin(x) * sin(y) + x * y; }
            der(f, x) //commento
            var g = function(x, y) { 2 * (x + der(f, y)); }
            var r{3}; //commento
            var J{12, 12};
            var dot = function(u{:}, v{:}) -> scalar {
                      return u[i] * v[i];
            }
            var u_str = "stringa"
            var u_mod = 10
            var norm = function(u{:}) -> scalar { return sqrt(dot(u, u)); }
            <end>
            """;
        Lexer lexer = new Lexer(code);
        lexer.tokenize();
        List<Token> tokens = lexer.getTokens();

        tokens.forEach(System.out::println);
    }
}