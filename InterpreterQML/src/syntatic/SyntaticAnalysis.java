
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
        System.out.printf("Não");
        System.exit(1);
    }

    private void obj(){
        eat(TokenType.NOME);
        eat(TokenType.OPEN_CUR);
        while(current.type == TokenType.OPEN_CUR){
            advance();
            if(current.type == TokenType.NOME){
                advance();
                if(currente.type == TokenType.OPEN_CUR){
                    obj(); //duvida se pode chamar o objeto dentro do objeto assim
                }
                if(current.type == TokenType.COLON){
                    eat(TokenType.COLON);
                    procColon();
                }
            }
        }
    }
    
    private void procColon() {
        advance();
        if(current.type == TokenType.OPEN_BRA){
            arranjo();
        } else {
            valor;
        }
    }
    // <list> ::= '[' [ <l-elem> { ',' <l-elem> } ] ']'
    private void arranjo() {
        advance();
        while(current.type == TokenType.NOME){
            advance();
            if(current.type == TokenType.OPEN_CUR){
                obj();
            }
            else(current.type == TokenType.COMMA){
                advance();
            }
        }
        while(current.type == TokenType.NUMBER){
            advance();
        }
        if(current.type == TokenType.OPEN_dQUOTES){
            while(current.type == TokenType.NOME){
                advance();
            }
        }

    }

    // <l-elem> ::= <l-single> | <l-spread> | <l-if> | <l-for>
    private void valor() {
        while(current.type == TokenType.NOME || current.type == TokenType.NUMBER){
            advance();
        }
        if(current.type == TokenType.OPEN_dQUOTES){
            while(current.type == TokenType.NOME){
                advance();
            }
        }
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
