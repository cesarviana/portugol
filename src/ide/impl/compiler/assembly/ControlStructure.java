package ide.impl.compiler.assembly;

import ide.impl.compiler.assembly.impl.Assembly;
import ide.impl.compiler.assembly.impl.Expression;

public abstract class ControlStructure extends Assembly {
    private Expression expression;

    public ControlStructure() {
        this.expression = new Expression();
    }

    protected String renameScopeToBranch(String scope) {
        return scope.toUpperCase().replace("->", "_");
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
