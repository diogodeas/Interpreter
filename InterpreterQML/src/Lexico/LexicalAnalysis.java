package lexico;

import java.io.FileInputStream;
import java.io.PushbackInputStream;
import lexico.LexicalException;
import javax.lang.model.util.ElementScanner14;

public class LexicalAnalysis implements AutoCloseable {

    private int line;
    private SymbolTable st;
    private PushbackInputStream input;

    public LexicalAnalysis(String filename) {
        try {
            input = new PushbackInputStream(new FileInputStream(filename), 2);
        } catch (Exception e) {
            throw new LexicalException("Unable to open file");
        }

        st = new SymbolTable();
        line = 1;
    }

    public void close() {
        try {
            input.close();
        } catch (Exception e) {
            throw new LexicalException("Unable to close file");
        }
    }

    public int getLine() {
        return this.line;
    }

    public Lexeme nextToken() {

        Lexeme lex = new Lexeme("", TokenType.END_OF_FILE);

        int state = 1;
        while (state != 6 && state != 5) {
            int c = getc();
            //System.out.printf("  [%02d, %03d ('%c')]\n", state, c, (char) c);
            switch (state) {
                case 1:
                    if (c == ' ' || c == '\t' || c == '\r') {
                        state = 1;
                    } else if (c == '\n') {
                        this.line++;
                    }
                    else if( c == '{' ||  c == '}' ||  c == ':' ||  c == '[' ||  c == ']' ||
                    c == ',' ){
                        lex.token +=(char)c;
                        state = 6;
                    }
                    else if( c == '"'){
                        state =  2;
                    }
                    else if (Character.isLetter(c)) {
                        lex.token += (char) c;
                        state = 4;
                    } else if (Character.isDigit(c)) {
                        lex.token += (char) c;
                        state = 3;
                    }
                    else if (c == -1) {
                        lex.type = TokenType.END_OF_FILE;
                        state = 5;
                    } else {
                        lex.token += (char) c;
                        lex.type = TokenType.INVALID_TOKEN;
                        state = 5;
                    }
                    break;
                case 2:
                    if(c != '"'){
                        lex.token +=(char)c;
                        state= 2;
                    }
                    else{
                        lex.type = TokenType.TEXTO;
                        state = 5;
                    }
                    break;
                case 3:
                    if(Character.isDigit(c)){
                        lex.token += (char) c;
                        state = 3;
                    }
                    else{
                        lex.type = TokenType.NUMBER;
                        state=5;
                        ungetc(c);
                    }
                    break;
                case 4:
                    if (Character.isLetter(c) || Character.isDigit(c) ||
                    c ==  '.') {
                        lex.token += (char) c;
                        state = 4;
                    } else {
                        ungetc(c);
                        lex.type = TokenType.NOME;
                        state = 6;
                    }
                    break;
              
                default:
                    throw new LexicalException("Unreachable");
            }
        }

        if (state == 6)
            lex.type = st.find(lex.token);
        return lex;
    }

    private int getc() {
        try {
            return input.read();
        } catch (Exception e) {
            throw new LexicalException("Unable to read file");
        }
    }
    

    private void ungetc(int c) {
        if (c != -1) {
            try {
                input.unread(c);
            } catch (Exception e) {
                throw new LexicalException("Unable to ungetc");
            }
        }
    }
}
