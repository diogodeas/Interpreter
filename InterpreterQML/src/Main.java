import lexico.Lexeme;
import lexico.LexicalAnalysis;
import lexico.TokenType;
import syntatic.SyntaticAnalysis;

/*
* Autors:
* Diogo Emanuel Antunes Santos
* Pedro Henrique Rocha de Castro
* */
public class Main {
    public static void main(String[] args) {
        /*if (args.length != 1) {
            System.out.println("Usar Arquivo QT");
            return;
        }*/

        String leroy = "src/arquivo.qml";

        try (LexicalAnalysis l = new LexicalAnalysis(leroy)) {
            // O código a seguir é dado para testar o interpretador.
            // TODO: descomentar depois que o analisador léxico estiver OK.
            SyntaticAnalysis s = new SyntaticAnalysis(l);
            s.start();
            System.out.println("SIM");
            // // O código a seguir é usado apenas para testar o analisador léxico.
            // // TODO: depois de pronto, comentar o código abaixo.
            /*Lexeme lex;
            do {
                lex = l.nextToken();
                System.out.printf("%02d: (\"%s\", %s)\n", l.getLine(),
                    lex.token, lex.type);
            } while (lex.type != TokenType.END_OF_FILE &&
                     lex.type != TokenType.INVALID_TOKEN &&
                     lex.type != TokenType.UNEXPECTED_EOF);
        } catch (Exception e) {
            System.err.println("Internal error: " + e.getMessage());
            e.printStackTrace();*/
        }
    }
}