package interpreter.expr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.util.Utils;
import interpreter.value.ListValue;
import interpreter.value.MapValue;
import interpreter.value.Value;
public class SpreadListItem extends ListItem{

    private Expr expr;

    public SpreadListItem(int line, Expr expr) {
        super(line);
        this.expr = expr;
    }
    public List<Value<?>> items() {
        List<Value<?>>  spreadList = new ArrayList<>();
        if(this.expr.expr() instanceof ListValue){
            ListValue listv = (ListValue) this.expr.expr();
            spreadList.addAll(listv.value());
        }
        else{
            Utils.abort(super.getLine());
        }
        return spreadList;
    }
}
