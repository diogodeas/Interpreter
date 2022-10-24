package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.BoolValue;
import interpreter.value.Value;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.value.Value;

import java.util.ArrayList;
import java.util.List;

public class IfListItem extends ListItem {

    private Expr expr;
    private ListItem thenItem;
    private ListItem elseItem;

    public IfListItem(int line, Expr expr, ListItem thenItem, ListItem elseItem){
        super(line);
        this.expr = expr;
        this.thenItem = thenItem;
        this.elseItem = elseItem;
    }

    public List<Value<?>> items(){
        Value<?> value= expr.expr();
        List<Value<?>> ifListV = new ArrayList<Value<?>>();
        BoolValue bv = (BoolValue) value;
        boolean b = bv.value();
        if(!(value instanceof BoolValue)){
            Utils.abort(super.getLine());
        }
        if(b)
            ifListV.addAll(this.thenItem.items());
        else
            ifListV.addAll(this.elseItem.items());

        return ifListV;

    }

}
