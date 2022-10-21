package interpreter.expr;

import interpreter.value.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.util.Utils;
import interpreter.value.MapValue;
import interpreter.value.Value;

public class ListItem {

        private int line;

        protected ListItem(int line) {
            super(line);
        }

        public int getLine() {
            return line;
        }

        public List<Value<?>> items() {

        }


}
