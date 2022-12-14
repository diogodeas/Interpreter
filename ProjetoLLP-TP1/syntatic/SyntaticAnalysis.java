
package syntatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.command.*;
import interpreter.expr.*;
import interpreter.util.Utils;
import interpreter.value.BoolValue;
import interpreter.value.NumberValue;
import interpreter.value.TextValue;
import interpreter.value.Value;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;
    private Map<String,Variable> memory;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
        this.memory = new HashMap<String,Variable>();
    }

    public Command start() {
        Command cmd = procCode();
        eat(TokenType.END_OF_FILE);
        return cmd;
    }

    private void advance() {
        //System.out.println("Advanced (\"" + current.token + "\", " +
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

    // <code> ::= { <cmd> }
    private BlocksCommand procCode() {
        int line = lex.getLine();
        List<Command> cmds = new ArrayList<Command>();
        while (current.type == TokenType.FINAL ||
                current.type == TokenType.VAR ||
                current.type == TokenType.PRINT ||
                current.type == TokenType.ASSERT ||
                current.type == TokenType.IF ||
                current.type == TokenType.WHILE ||
                current.type == TokenType.DO ||
                current.type == TokenType.FOR ||
                current.type == TokenType.NOT ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INC ||
                current.type == TokenType.DEC ||
                current.type == TokenType.OPEN_PAR ||
                current.type == TokenType.NULL ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.TEXT ||
                current.type == TokenType.READ ||
                current.type == TokenType.RANDOM ||
                current.type == TokenType.LENGTH ||
                current.type == TokenType.KEYS ||
                current.type == TokenType.VALUES ||
                current.type == TokenType.TOBOOL ||
                current.type == TokenType.TOINT ||
                current.type == TokenType.TOSTR ||
                current.type == TokenType.NAME ||
                current.type == TokenType.OPEN_BRA ||
                current.type == TokenType.OPEN_CUR) {
            Command c = procCmd();
            cmds.add(c);
        }

        BlocksCommand bc = new BlocksCommand(line, cmds);
        return bc;
    }

    // <cmd> ::= <decl> | <print> | <assert> | <if> | <while> | <dowhile> | <for> | <assign>
    private Command procCmd() {
        Command cmd = null;
        switch (current.type) {
            case FINAL:
            case VAR:
                cmd = procDecl();
                break;
            case PRINT:
                cmd = procPrint();
                break;
            case ASSERT:
                procAssert();
                break;
            case IF:
                procIf();
                break;
            case WHILE:
                cmd = procWhile();
                break;
            case DO:
                procDoWhile();
                break;
            case FOR:
                procFor();
                break;
            case NOT:
            case SUB:
            case INC:
            case DEC:
            case OPEN_PAR:
            case NULL:
            case FALSE:
            case TRUE:
            case NUMBER:
            case TEXT:
            case READ:
            case RANDOM:
            case LENGTH:
            case KEYS:
            case VALUES:
            case TOBOOL:
            case TOINT:
            case TOSTR:
            case NAME:
            case OPEN_BRA:
            case OPEN_CUR:
                cmd = procAssign();
                break;
            default:
                showError();
                break;
        }

        return cmd;
    }

    // <decl> ::= [ final ] var [ '?' ] <name> [ '=' <expr> ] { ',' <name> [ '=' <expr> ] } ';'
    private BlocksCommand procDecl() {
        int line = lex.getLine();
        List<Command> cmds = new ArrayList<Command>();

        boolean constant = false;
        if (current.type == TokenType.FINAL) {
            advance();
            constant = true;
        }

        eat(TokenType.VAR);

        boolean nullable = false;
        if (current.type == TokenType.NULLABLE) {
            advance();
            nullable = true;
        }

        Variable var = procDeclarationName(constant, nullable);

        if (current.type == TokenType.ASSIGN) {
            line = lex.getLine();
            advance();

            Expr rhs = procExpr();

            AssignCommand acmd = new AssignCommand(line, rhs, var);
            cmds.add(acmd);
        }

        while (current.type == TokenType.COMMA) {
            advance();

            var = procDeclarationName(constant, nullable);

            if (current.type == TokenType.ASSIGN) {
                advance();

                Expr rhs = procExpr();

                AssignCommand acmd = new AssignCommand(line, rhs, var);
                cmds.add(acmd);
            }
        }

        eat(TokenType.SEMICOLON);

        BlocksCommand bcmd = new BlocksCommand(line, cmds);
        return bcmd;
    }

    // <print> ::= print '(' [ <expr> ] ')' ';'
    private PrintCommand procPrint() {
        eat(TokenType.PRINT);
        int line = lex.getLine();
        eat(TokenType.OPEN_PAR);
        Expr expr = null;
        if (current.type == TokenType.NOT ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INC ||
                current.type == TokenType.DEC ||
                current.type == TokenType.OPEN_PAR ||
                current.type == TokenType.NULL ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.TEXT ||
                current.type == TokenType.READ ||
                current.type == TokenType.RANDOM ||
                current.type == TokenType.LENGTH ||
                current.type == TokenType.KEYS ||
                current.type == TokenType.VALUES ||
                current.type == TokenType.TOBOOL ||
                current.type == TokenType.TOINT ||
                current.type == TokenType.TOSTR ||
                current.type == TokenType.NAME ||
                current.type == TokenType.OPEN_BRA ||
                current.type == TokenType.OPEN_CUR) {
            expr = procExpr();
        }
        eat(TokenType.CLOSE_PAR);
        eat(TokenType.SEMICOLON);

        PrintCommand pc = new PrintCommand(line, expr);
        return pc;
    }

    // <assert> ::= assert '(' <expr> [ ',' <expr> ] ')' ';'
    private AssertCommand procAssert() {
        AssertCommand assertCMDS;
        Expr expr;
        Expr msg = null;
        eat(TokenType.ASSERT);
        int line = lex.getLine();
        eat(TokenType.OPEN_PAR);
        expr  = procExpr();
        if(current.type == TokenType.COMMA){
            eat(TokenType.COMMA);
            msg = procExpr();
        }

        eat(TokenType.CLOSE_PAR);
        eat(TokenType.SEMICOLON);
        assertCMDS = new AssertCommand(line,expr, msg);

        return assertCMDS;
    }

    // <if> ::= if '(' <expr> ')' <body> [ else <body> ]
    private IfCommand procIf() {
        eat(TokenType.IF);
        eat(TokenType.OPEN_PAR);
        Expr expr = procExpr();
        eat(TokenType.CLOSE_PAR);
        Command thenCmd = procBody();
        Command elseCmd = null;
        if (current.type == TokenType.ELSE) {
            eat(TokenType.ELSE);
            elseCmd = procBody();
        }
        return new IfCommand(lex.getLine(), expr, thenCmd, elseCmd);
    }

    // <while> ::= while '(' <expr> ')' <body>
    private WhileCommand procWhile() {
        eat(TokenType.WHILE);
        int line = lex.getLine();

        eat(TokenType.OPEN_PAR);
        Expr expr = procExpr();
        eat(TokenType.CLOSE_PAR);
        Command cmds = procBody();

        WhileCommand wcmd = new WhileCommand(line, expr, cmds);
        return wcmd;
    }

    // <dowhile> ::= do <body> while '(' <expr> ')' ';'
    private DoWhileCommand procDoWhile() {
        eat(TokenType.DO);
        Command cmd = procBody();
        eat(TokenType.WHILE);
        eat(TokenType.OPEN_PAR);
        Expr expr = procExpr();
        eat(TokenType.CLOSE_PAR);
        eat(TokenType.SEMICOLON);

        return new DoWhileCommand(lex.getLine(), cmd, expr);
    }

    // <for> ::= for '(' <name> in <expr> ')' <body>
    private ForCommand procFor() {
        eat(TokenType.FOR);
        eat(TokenType.OPEN_PAR);
        Variable var = procName();
        eat(TokenType.IN);
        Expr expr = procExpr();
        eat(TokenType.CLOSE_PAR);
        Command cmds  = procBody();

        return new ForCommand(lex.getLine(), var, expr, cmds);
    }

    // <body> ::= <cmd> | '{' <code> '}'
    private Command procBody() {
        Command cmds;
        if (current.type == TokenType.OPEN_CUR) {
            advance();
            cmds = procCode();
            eat(TokenType.CLOSE_CUR);
        } else {
            cmds = procCmd();
        }

        return cmds;
    }

    // <assign> ::= [ <expr> '=' ] <expr> ';'
    private AssignCommand procAssign() {
        Expr rhs = procExpr();
        SetExpr lhs = null;

        int line = lex.getLine();
        if (current.type == TokenType.ASSIGN) {
            advance();

            if (!(rhs instanceof SetExpr))
                Utils.abort(line);

            lhs = (SetExpr) rhs;
            rhs = procExpr();
        }

        eat(TokenType.SEMICOLON);

        AssignCommand acmd = new AssignCommand(line, rhs, lhs);
        return acmd;
    }

    // <expr> ::= <cond> [ '??' <cond> ]
    private Expr procExpr() {

        Expr leftExpr = procCond();
        BinaryOp operation = null;
        Expr rightExpr = null;
        if (current.type == TokenType.IF_NULL) {
            operation = BinaryOp.IF_NULL;
            advance();
            rightExpr = procCond();
        }
        if(operation != null){
            return new BinaryExpr(lex.getLine(), leftExpr, operation, rightExpr);
        }
        return leftExpr;

    }

    // <cond> ::= <rel> { ( '&&' | '||' ) <rel> }
    private Expr procCond() {
        Expr leftExpr = procRel();
        BinaryOp operation = null;
        while (current.type == TokenType.AND ||
                current.type == TokenType.OR) {
            
            if (current.type == TokenType.AND) {
                operation = BinaryOp.AND;
                advance();
            } else {
                operation = BinaryOp.OR;
                advance();
            }

            Expr rightExpr = procRel();
            return new BinaryExpr(lex.getLine(), leftExpr, operation, rightExpr);
        }

        return leftExpr;
    }

    // <rel> ::= <arith> [ ( '<' | '>' | '<=' | '>=' | '==' | '!=' ) <arith> ]
    private Expr procRel() {
        Expr left = procArith();

        if (current.type == TokenType.LOWER_THAN ||
                current.type == TokenType.GREATER_THAN ||
                current.type == TokenType.LOWER_EQUAL ||
                current.type == TokenType.GREATER_EQUAL ||
                current.type == TokenType.EQUAL ||
                current.type == TokenType.NOT_EQUAL) {
            BinaryOp op = null;
            switch (current.type) {
                case LOWER_THAN:
                    op = BinaryOp.LOWER_THAN;
                    advance();
                    break;
                case GREATER_THAN:
                    op = BinaryOp.GREATER_THAN;
                    advance();
                    break;
                case LOWER_EQUAL:
                    op = BinaryOp.LOWER_EQUAL;
                    advance();
                    break;
                case GREATER_EQUAL:
                    op = BinaryOp.GREATER_EQUAL;
                    advance();
                    break;
                case EQUAL:
                    op = BinaryOp.EQUAL;
                    advance();
                    break;
                default:
                    op = BinaryOp.NOT_EQUAL;
                    advance();
                    break;
            }
            if(op != null){
                int line = lex.getLine();
                Expr right = procArith();

                left = new BinaryExpr(line, left, op, right);
            }
        }
        return left;
    }

    // <arith> ::= <term> { ( '+' | '-' ) <term> }
    private Expr procArith() {
        Expr left = procTerm();

        while (current.type == TokenType.ADD ||
                current.type == TokenType.SUB) {
            BinaryOp op = null;
            if (current.type == TokenType.ADD) {
                op = BinaryOp.ADD;
                advance();
            } else {
                op = BinaryOp.SUB;
                advance();
            }
            int line = lex.getLine();

            Expr right = procTerm();

            left = new BinaryExpr(line, left, op, right);
        }

        return left;
    }

    // <term> ::= <prefix> { ( '*' | '/' | '%' ) <prefix> }
    private Expr procTerm() {
        Expr expr = procPrefix();
        BinaryOp op = null;
        while(current.type == TokenType.MUL || current.type == TokenType.DIV
        || current.type ==  TokenType.MOD){
            if (current.type == TokenType.MUL) {
                op = BinaryOp.MUL;
                advance();
            } else if(current.type ==  TokenType.DIV) {
                op = BinaryOp.DIV;
                advance();
            }
            else{
                op = BinaryOp.MOD;
                advance();
            }
            int line = lex.getLine();

            Expr right = procPrefix();

            expr = new BinaryExpr(line, expr, op, right);
        }

        return expr;
    }

    // <prefix> ::= [ '!' | '-' | '++' | '--' ] <factor>
    private Expr procPrefix() {
        UnaryOp op = null;
        if (current.type == TokenType.NOT ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INC ||
                current.type == TokenType.DEC) {
            switch (current.type) {
                case NOT:
                    op = UnaryOp.NOT;
                    advance();
                    break;
                case SUB:
                    op = UnaryOp.NEG;
                    advance();
                    break;
                case INC:
                    op = UnaryOp.PRE_INC;
                    advance();
                    break;
                default:
                    op = UnaryOp.POS_INC;
                    advance();
                    break;
            }
        }

        int line = lex.getLine();
        Expr expr = procFactor();

        if (op != null) {
            UnaryExpr ue = new UnaryExpr(line, expr, op);
            return ue;
        }

        return expr;
    }

    // <factor> ::= ( '(' <expr> ')' | <rvalue> ) [ '++' | '--' ]
    private Expr procFactor() {
        Expr expr = null;
        UnaryOp operation = null;
        UnaryExpr unaryExpr = null;
        if (current.type == TokenType.OPEN_PAR) {
            advance();
            expr = procExpr();
            eat(TokenType.CLOSE_PAR);
        } else {
            expr = procRValue();
        }

        if (current.type == TokenType.INC ||
                current.type == TokenType.DEC) {
            if (current.type == TokenType.INC) {
                operation = UnaryOp.POS_INC;
                advance();
            } else {
                operation = UnaryOp.POS_DEC;
                advance();
            }
        }
        if (operation != null) {
            unaryExpr = new UnaryExpr(lex.getLine(), expr, operation);
            return unaryExpr;
        }

        return expr;
        
    }

    // <rvalue> ::= <const> | <function> | <lvalue> | <list> | <map>
    private Expr procRValue() {
        Expr expr = null;
        switch (current.type) {
            case NULL:
            case FALSE:
            case TRUE:
            case NUMBER:
            case TEXT:
                expr = procConst();
                break;
            case READ:
            case RANDOM:
            case LENGTH:
            case KEYS:
            case VALUES:
            case TOBOOL:
            case TOINT:
            case TOSTR:
                expr = procFunction();
                break;
            case NAME:
                expr = procLValue();
                break;
            case OPEN_BRA:
                expr = procList();
                break;
            case OPEN_CUR:
                expr = procMap();
                break;
            default:
                showError();
                break;
        }

        return expr;
    }

    // <const> ::= null | false | true | <number> | <text>
    private ConstExpr procConst() {
        Value<?> v = null;
        switch (current.type) {
            case NULL:
                advance();
                v = null;
                break;
            case FALSE:
                advance();
                v = new BoolValue(false);
                break;
            case TRUE:
                advance();
                v = new BoolValue(true);
                break;
            case NUMBER:
                v = procNumber();
                break;
            case TEXT:
                v = procText();
                break;
            default:
                showError();
                break;
        }

        int line = lex.getLine();
        ConstExpr ce = new ConstExpr(line, v);
        return ce;
    }

    // <function> ::= ( read | random | length | keys | values | tobool | toint | tostr ) '(' <expr> ')'
    private FunctionExpr procFunction() {
        FunctionOp op = null;
        switch (current.type) {
            case READ:
                advance();
                op = FunctionOp.READ;
                break;
            case RANDOM:
                advance();
                op = FunctionOp.RANDOM;
                break;
            case LENGTH:
                advance();
                op = FunctionOp.LENGTH;
                break;
            case KEYS:
                advance();
                op = FunctionOp.KEYS;
                break;
            case VALUES:
                advance();
                op = FunctionOp.VALUES;
                break;
            case TOBOOL:
                advance();
                op = FunctionOp.TOBOOL;
                break;
            case TOINT:
                advance();
                op = FunctionOp.TOINT;
                break;
            case TOSTR:
                advance();
                op = FunctionOp.TOSTR;
                break;
            default:
                showError();
                break;
        }
        int line = lex.getLine();

        eat(TokenType.OPEN_PAR);
        Expr expr = procExpr();
        eat(TokenType.CLOSE_PAR);

        FunctionExpr fexpr = new FunctionExpr(line, op, expr);
        return fexpr;
    }

    // <lvalue> ::= <name> { '[' <expr> ']' }
    private SetExpr procLValue() {
        SetExpr base = procName();
        while (current.type == TokenType.OPEN_BRA) {
            advance();
            int line = lex.getLine();

            Expr index = procExpr();

            base = new AccessExpr(line, base, index);

            eat(TokenType.CLOSE_BRA);
        }

        return base;
    }

    // <list> ::= '[' [ <l-elem> { ',' <l-elem> } ] ']'
    private ListExpr procList() {
        eat(TokenType.OPEN_BRA);
        int line = lex.getLine();
        ListExpr list = new ListExpr(line);

        if(current.type != TokenType.CLOSE_BRA){
            ListItem item = procLElem();
            list.addItem(item);
            while(current.type == TokenType.COMMA){
                advance();
                list.addItem(procLElem());
            }
        }
        eat(TokenType.CLOSE_BRA);
        return list;
    }

    // <l-elem> ::= <l-single> | <l-spread> | <l-if> | <l-for>
    private ListItem procLElem() {
        ListItem li = null;
        switch (current.type) {
            case NOT:
            case SUB:
            case INC:
            case DEC:
            case OPEN_PAR:
            case NULL:
            case FALSE:
            case TRUE:
            case NUMBER:
            case TEXT:
            case READ:
            case RANDOM:
            case LENGTH:
            case KEYS:
            case VALUES:
            case TOBOOL:
            case TOINT:
            case TOSTR:
            case NAME:
            case OPEN_BRA:
            case OPEN_CUR:
                li = procLSingle();
                break;

            case SPREAD:
                li = procLSpread();
                break;

            case IF:
                li = procLIf();
                break;

            case FOR:
                li = procLFor();
                break;

            default:
                showError();
                break;
        }
        return li;
    }

    // <l-single> ::= <expr>
    private SingleListItem procLSingle() {
        int line  = lex.getLine();
        Expr expr = procExpr();

        return new SingleListItem(line, expr);

    }

    // <l-spread> ::= '...' <expr>
    private SpreadListItem procLSpread() {
        eat(TokenType.SPREAD);
        Expr expr = procExpr();


        return new SpreadListItem(lex.getLine(), expr);
    }

    // <l-if> ::= if '(' <expr> ')' <l-elem> [ else <l-elem> ]
    private IfListItem procLIf() {
        eat(TokenType.IF);
        eat(TokenType.OPEN_PAR);
        Expr expr = procExpr();
        ListItem thenItem = procLElem();
        ListItem elseItem = null;
        if (current.type == TokenType.ELSE) {
            eat(TokenType.ELSE);
            elseItem = procLElem();
        }
   
        return new IfListItem(lex.getLine(), expr, thenItem, elseItem);

    }

    // <l-for> ::= for '(' <name> in <expr> ')' <l-elem>
    private ForListItem procLFor() {
        int line = lex.getLine();
        eat(TokenType.FOR);
        eat(TokenType.OPEN_PAR);
        Variable var = procName();
        eat(TokenType.IN);
        Expr expr = procExpr();
        eat(TokenType.CLOSE_PAR);
        ListItem item = procLElem();
       
        return  new ForListItem(line, var, expr, item);

    }

    // <map> ::= '{' [ <m-elem> { ',' <m-elem> } ] '}'
    private MapExpr procMap() {
        eat(TokenType.OPEN_CUR);
        int line = lex.getLine();

        MapExpr mexpr = new MapExpr(line);

        if (current.type == TokenType.NOT ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INC ||
                current.type == TokenType.DEC ||
                current.type == TokenType.OPEN_PAR ||
                current.type == TokenType.NULL ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.TEXT ||
                current.type == TokenType.READ ||
                current.type == TokenType.RANDOM ||
                current.type == TokenType.LENGTH ||
                current.type == TokenType.KEYS ||
                current.type == TokenType.VALUES ||
                current.type == TokenType.TOBOOL ||
                current.type == TokenType.TOINT ||
                current.type == TokenType.TOSTR ||
                current.type == TokenType.NAME ||
                current.type == TokenType.OPEN_BRA ||
                current.type == TokenType.OPEN_CUR) {
            MapItem item = procMElem();
            mexpr.addItem(item);

            while (current.type == TokenType.COMMA) {
                advance();
                item = procMElem();
                mexpr.addItem(item);
            }
        }
        eat(TokenType.CLOSE_CUR);

        return mexpr;
    }

    // <m-elem> ::= <expr> ':' <expr>
    private MapItem procMElem() {
        Expr key = procExpr();
        eat(TokenType.COLON);
        Expr value = procExpr();

        MapItem item = new MapItem(key, value);
        return item;
    }

    private Variable procDeclarationName(boolean constant, boolean nullable) {
        String name = current.token;
        eat(TokenType.NAME);
        int line = lex.getLine();

        if (memory.containsKey(name))
            Utils.abort(line);

        Variable var;
        if (nullable) {
            var = new UnsafeVariable(line, name, constant);
        } else {
            var = new SafeVariable(line, name, constant);
        }

        memory.put(name, var);

        return var;
    }

    private Variable procName() {
        String name = current.token;
        eat(TokenType.NAME);
        int line = lex.getLine();

        if (!memory.containsKey(name))
            Utils.abort(line);

        Variable var = memory.get(name);
        return var;
    }

    private NumberValue procNumber() {
        String txt = current.token;
        eat(TokenType.NUMBER);

        int n;
        try {
            n = Integer.parseInt(txt);
        } catch (Exception e) {
            n = 0;
        }

        NumberValue nv = new NumberValue(n);
        return nv;
    }

    private TextValue procText() {
        String txt = current.token;
        eat(TokenType.TEXT);

        TextValue tv = new TextValue(txt);
        return tv;
    }
}
