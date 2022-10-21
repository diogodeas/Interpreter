package interpreter.command;
import interpreter.expr.Expr;
import interpreter.expr.ListItem;
import interpreter.expr.SetExpr;
import interpreter.value.BoolValue;
import interpreter.value.Value;
public class AssertCommand {
    private Expr expr;
    private Expr msg;

    public AssertCommand(int line, Expr expr, ListItem thenItem){
        super(line);
        this.expr = expr;
        this.msg = null;
    }
    public void execute() {
        Value<?> v = expr.expr();
        BoolValue bv = (BoolValue) v;
        boolean b = bv.value();
    }

}
