package interpreter.expr;

import interpreter.value.Value;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.value.Value;

public class IfListItem {

    private Expr expr;
    private ListItem thenItem;
    private ListItem elseItem;

    public IfListItem(int line, Expr expr, ListItem thenItem){
        super(line);
        this.expr = expr;
        this.thenItem = thenItem;
        this.elseItem = null;
    }

    public List<Value<?>> items(){

    }

}
