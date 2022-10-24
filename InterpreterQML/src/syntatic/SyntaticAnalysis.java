package syntatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import lexico.Lexeme;
import lexico.LexicalAnalysis;
import lexico.TokenType;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;
    //private Map<String,Variable> memory;

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
        eat(TokenType.OPEN_CUR);
        obj();
    }

    private void repete(){
        if(current.type == TokenType.OPEN_BRA) {
            eat(TokenType.OPEN_BRA);
            arranjo();
        }
        else if(current.type == TokenType.OPEN_CUR) {
            eat(TokenType.OPEN_CUR);
            obj();

        }
        else if(current.type == TokenType.CLOSE_BRA){
            eat(TokenType.OPEN_BRA);
        } else if (current.type == TokenType.COMMA) {
            eat(TokenType.COMMA);
        } else {
            showError();
        }
    }

    private void obj(){
        while(current.type == TokenType.NOME){
            advance();
            if(current.type == TokenType.OPEN_CUR) {
                repete();
            }
            else if(current.type == TokenType.COLON) {
                eat(TokenType.COLON);
                if(current.type == TokenType.OPEN_BRA) {
                    repete();
                } else {
                    valor();
                }
            }  else if (current.type == TokenType.COMMA) {
                eat(TokenType.COMMA);
            }
            else {
                showError();
            }
        }
        if(current.type == TokenType.CLOSE_CUR){
            eat(TokenType.CLOSE_CUR);
        }else if (current.type != null) {
            showError();
        }


    }
    
    // <list> ::= '[' [ <l-elem> { ',' <l-elem> } ] ']'
    private void arranjo() {
        while(current.type == TokenType.NOME || current.type == TokenType.TEXTO || current.type == TokenType.NUMBER || current.type == TokenType.COMMA) {
            if (current.type == TokenType.NOME) {
                eat(TokenType.NOME);
                repete();
            } else if (current.type == TokenType.TEXTO) {
                advance();
                if (current.type == TokenType.COMMA) {
                    advance();
                }
            } else if (current.type == TokenType.NUMBER) {
                advance();
                if (current.type == TokenType.COMMA) {
                    eat(TokenType.COMMA);
                }
            }
            else if (current.type == TokenType.COMMA) {
                eat(TokenType.COMMA);
            }
            else {
                showError();
            }
        }
        if(current.type == TokenType.CLOSE_BRA){
            eat(TokenType.CLOSE_BRA);
        }
    }

    // <l-elem> ::= <l-single> | <l-spread> | <l-if> | <l-for>
    private void valor() {
        advance();
    }


    
}
