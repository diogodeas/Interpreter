package interpreter.expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.util.Utils;
import interpreter.value.ListValue;
import interpreter.value.MapValue;
import interpreter.value.Value;

public class ListExpr {

    private List<ListItem> list;

    public ListExpr(int line) {
        super(line);
        list = List<ListItem>();
    }

    public void addItem(ListItem item) {
        list.add(item);
    }
    public Value<?> expr() {
        List<Value<?>, Value<?>> l = new HashList<Value<?>, Value<?>>();

        for (ListItem item : list) {
            Value<?> key = item.key.expr();
            if (key == null)
                Utils.abort(super.getLine());

            Value<?> value = item.value.expr();

            l.put(key, value);
        }

        ListValue mv = new ListValue(l);
        return mv;
    }
}
