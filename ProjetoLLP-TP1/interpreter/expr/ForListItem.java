package interpreter.expr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.command.Command;
import interpreter.util.Utils;
import interpreter.value.MapValue;
import interpreter.value.Value;
public class ForListItem {
    private Variable variable;
    private Expr expr;
    private ListItem item;



    public ForListItem(int line, Variable var, Expr expr, ListItem item){
        super(line);
        super(var);
        this.expr = expr;
        this.item = item;
    }

    public List<Value<?>> items() {

    }
}
