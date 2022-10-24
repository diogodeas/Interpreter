package interpreter.expr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.util.Utils;
import interpreter.value.MapValue;
import interpreter.value.Value;
public class SingleListItem extends ListItem{

    private Expr expr;

    public SingleListItem(int line, Expr expr) {
        super(line);
        this.expr = expr;
    }
    public List<Value<?>> items() {
        List<Value<?>> singleList = new ArrayList<>();
        singleList.add(expr.expr());

        return singleList;
    }
}
