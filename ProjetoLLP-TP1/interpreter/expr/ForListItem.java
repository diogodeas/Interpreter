package interpreter.expr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.command.Command;
import interpreter.util.Utils;
import interpreter.value.ListValue;
import interpreter.value.MapValue;
import interpreter.value.Value;
public class ForListItem extends  ListItem{
    private Variable variable;
    private Expr expr;
    private ListItem item;



    public ForListItem(int line, Variable var, Expr expr, ListItem item){
        super(line);
       this.variable = var;
        this.expr = expr;
        this.item = item;
    }

    public List<Value<?>> items() {
        List<Value<?>> forListItem = new ArrayList<Value<?>>();
        Value<?> value = expr.expr();

        if (!(value instanceof ListValue)){
            Utils.abort(super.getLine());
        }


        ListValue listValue= (ListValue)   value;

        for (Value<?> variavel : listValue.value()) {
            variable.setValue(variavel);
            forListItem.addAll(item.items());
        }

        return forListItem;
    }
}
