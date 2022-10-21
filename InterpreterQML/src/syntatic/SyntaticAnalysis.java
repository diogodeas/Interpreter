
package syntatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import Lexico.Lexeme;
import Lexico.LexicalAnalysis;
import Lexico.TokenType;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;
    private Map<String,Variable> memory;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
       
    }

    public void start() {
        obj();
    }

    private void advance() {
        // System.out.println("Advanced (\"" + current.token + "\", " +
        //     current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        // System.out.println("Expected (..., " + type + "), found (\"" +
        //     current.token + "\", " + current.type + ")");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }

    private void obj(){
        eat(TokenType.NOME);
        eat(TokenType.OPEN_CUR);
        while(current.type == TokenType.OPEN_CUR){
            advance();
            if(current.type == TokenType.NOME){
                
            }

        }

    }
    
    // <list> ::= '[' [ <l-elem> { ',' <l-elem> } ] ']'
    private void procList() {
    }

    // <l-elem> ::= <l-single> | <l-spread> | <l-if> | <l-for>
    private void procLElem() {
    }

    // <l-single> ::= <expr>
    private void procLSingle() {
    }

    // <l-spread> ::= '...' <expr>
    private void procLSpread() {
    }

    // <l-if> ::= if '(' <expr> ')' <l-elem> [ else <l-elem> ]
    private void procLIf() {
    }

    // <l-for> ::= for '(' <name> in <expr> ')' <l-elem>
    private void procLFor() {
    }

    // <map> ::= '{' [ <m-elem> { ',' <m-elem> } ] '}'
    
}
