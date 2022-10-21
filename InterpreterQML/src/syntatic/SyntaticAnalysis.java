
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
        code();
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
        System.out.printf("Não");
        System.exit(1);
    }

    private void code(){
        eat(TokenType.NOME);
        obj();
    }
    
    private void repete(){
        eat(TokenType.NOME);
        if(current.type == TokenType.OPEN_BRA) {
            arranjo();
        }
        else if(current.type == TokenType.OPEN_CUR) {
            obj();
        }
        else if(current.type == current.type == TokenType.CLOSE_BRA){
            advance();
        }
        else {
            showError();
        }
    }

    private void obj(){
        eat(TokenType.OPEN_CUR);
        while(current.type == TokenType.NOME){
            advance();
            if(current.type == TokenType.OPEN_CUR) {
                inicial();
            }
            else if(current.type == TokenType.COLON) {
                eat(TokenType.OPEN_COLON);
                if(current.type == TokenType.OPEN_BRA) {
                    inicial();
                } else {
                    valor();
                }
            } else {
                showError();
            }
        }
    }
    
    // <list> ::= '[' [ <l-elem> { ',' <l-elem> } ] ']'
    private void arranjo() {
        eat(TokenType.OPEN_BRA);
        if(current.type == TokenType.NOME) {
            repete();
        } else if(current.type == TokenType.TEXTO) {
            advance ();
        }
    }

    // <l-elem> ::= <l-single> | <l-spread> | <l-if> | <l-for>
    private void valor() {
        eat(TokenType.OPEN_CUR);
        advance();
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
