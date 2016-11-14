package ide.impl.compiler.assembly;

import ide.impl.compiler.assembly.impl.Assembly;
import ide.impl.compiler.assembly.impl.Expression;

public abstract class ControlStructure extends Assembly {
    private Expression expression;

    public ControlStructure() {
        this.expression = Expression.instance();
    }

    protected String renameScopeToBranch(String scope) {
        return scope.toUpperCase().replace("->", "_");
    }

    public Expression getExpression() {
        return expression;
    }

    public String fim() {
        return "FIM_" +  getAssemblyScope();
    }

    public void addEnd() {
        getLines().add(fim() + ":");
    }
}
