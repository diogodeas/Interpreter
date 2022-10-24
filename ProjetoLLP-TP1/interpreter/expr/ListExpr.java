package interpreter.expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.util.Utils;
import interpreter.value.ListValue;
import interpreter.value.MapValue;
import interpreter.value.Value;

public class ListExpr extends Expr{

    private List<ListItem> list;

    public ListExpr(int line) {
        super(line);
        this.list = new ArrayList<ListItem>();
    }

    public void addItem(ListItem item) {
        list.add(item);
    }
    public Value<?> expr() {
        List<Value<?>> listExpr = new ArrayList<Value<?>>();

        for(ListItem item : list){
            listExpr.addAll(item.items());
        }
        ListValue lv = new ListValue(listExpr);

        return lv;
    }
}
