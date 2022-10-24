
package interpreter.expr;

import java.util.*;

import interpreter.util.Utils;
import interpreter.value.*;

public class FunctionExpr extends Expr {

    private FunctionOp op;
    private Expr expr;

    private static Scanner input = new Scanner(System.in);

    public FunctionExpr(int line, FunctionOp op, Expr expr) {
        super(line);

        this.op = op;
        this.expr = expr;
    }

    @Override
    public Value<?> expr() {
        Value<?> v = expr.expr();

        switch (op) {
            case READ:
                return readOp(v);
            case RANDOM:
                return randomOp(v);
            case LENGTH:
                return lengthOp(v);
            case KEYS:
                return keysOp(v);
            case VALUES:
                return valuesOp(v);
            case TOBOOL:
                return toBoolOp(v);
            case TOINT:
                return toIntOp(v);
            case TOSTR:
                return toStrOp(v);
            default:
                Utils.abort(super.getLine());
                return null;
        }
    }

    private TextValue readOp(Value<?> v) {
        System.out.print(v);
        String text = input.nextLine().trim();
        return text.isEmpty() ? null : new TextValue(text);
    }

    private NumberValue randomOp(Value<?> v) {
        int altr;
        Random random = new Random();
        NumberValue rv = (NumberValue) v;
        altr = random.nextInt(rv.value());
        return (new NumberValue(altr));
    }

    private NumberValue lengthOp(Value<?> v) {
        int lev;
        if(v instanceof ListValue) {
            var tam = (ListValue) v.value();
            lev = tam.value().size();
            return new NumberValue(lev);
        } else {
            return null;
        }
    }

    private ListValue keysOp(Value<?> v) {
        if(v instanceof MapValue) {
            var kv = (MapValue) v.value();
            var keys = kv.value().keySet().stream().toList();
            return new ListValue(keys);
        } else {
            return null;
        }
    }

    private ListValue valuesOp(Value<?> v) {
        if(v instanceof MapValue) {
            var vop = (MapValue) v.value();
            var vops = vop.value().values().stream().toList();
            return new ListValue(vops);
        } else {
            return null;
        }
    }

    private BoolValue toBoolOp(Value<?> v) {
        boolean boolValue;
        if (v == null) {
            boolValue = false;
        } else if (v instanceof BoolValue) {
            BoolValue bv = (BoolValue) v;
            boolean b1 = bv.value();

            boolValue = b1;
        } else if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            int n = nv.value();
            if(n!=0)
                boolValue = true;
            else
                boolValue = false;
        } else if (v instanceof ListValue) {
            ListValue lv = (ListValue) v;
            List<Value<?>> l = new ArrayList<Value<?>>(lv.value());
            List<Value<?>> aux = new ArrayList<Value<?>>();
            if(l.equals(aux)) {
                boolValue = false;
            }
            else{
                boolValue = true;
            }


        }else if (v instanceof MapValue) {
            MapValue mv = (MapValue) v;
            Map<Value<?>, Value<?>> map = new HashMap<Value<?>, Value<?>>(mv.value());
            Map<Value<?>, Value<?>> aux = new HashMap<Value<?>, Value<?>>();
            if(map.equals(aux)){
                boolValue = false;
            }

            else{
                boolValue = true;
            }
        }else {
            boolValue = false;
        }

        return new BoolValue(boolValue);
    }

    private NumberValue toIntOp(Value<?> v) {
        int n;
        if (v == null) {
            n = 0;
        } else if (v instanceof BoolValue) {
            BoolValue bv = (BoolValue) v;
            boolean b = bv.value();

            n = b ? 1 : 0;
        } else if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            n = nv.value();
        } else if (v instanceof TextValue) {
            TextValue sv = (TextValue) v;
            String s = sv.value();

            try {
                n = Integer.parseInt(s);
            } catch (Exception e) {
                n = 0;
            }
        } else {
            n = 0;
        }

        return new NumberValue(n);
    }

    private TextValue toStrOp(Value<?> v) {
        String textValue = "null";
        if (v instanceof BoolValue) {
            BoolValue bv = (BoolValue) v;
            boolean b = bv.value();

            if(b){
                textValue = "true";
            }

            else{
                textValue = "false";
            }

        } else if (v == null) {
            textValue = "null";
        } else if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            int n = nv.value();
            textValue = String.valueOf(n);
        } else if (v instanceof TextValue) {
            TextValue sv = (TextValue) v;
            textValue = sv.value();
        } else if (v instanceof ListValue) {
            ListValue lv = (ListValue) v;
            textValue = lv.toString();
        } else if (v instanceof MapValue) {
            MapValue mv = (MapValue) v;
            textValue = mv.toString();
        }

        return new TextValue(textValue);

    }

}
