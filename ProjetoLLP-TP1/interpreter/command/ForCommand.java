package interpreter.command;

import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.util.Utils;
import interpreter.value.BoolValue;
import interpreter.value.Value;

public class ForCommand extends  Command{
    private Variable variable;
    private Expr expr;
    private Command cmds;



    public ForCommand(int var, Expr expr, Command cmds){
        super(var);
        this.expr = expr;
        this.cmds = cmds;

    }

    @Override
    public void execute() {

        while (true) {
            Value<?> v = expr.expr();
            if (!(v instanceof BoolValue))
                Utils.abort(super.getLine());

            BoolValue bv = (BoolValue) v;
            boolean b = bv.value();

            if (!b || !variable.isConstant()){ //se a expressao for falsa ou a variável inválida.
                break;
            }

            cmds.execute();
        }
    }
}
