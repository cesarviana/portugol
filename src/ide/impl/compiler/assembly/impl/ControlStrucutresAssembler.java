package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.ControlStructure;

public abstract class ControlStrucutresAssembler extends GeneralAssembler {
    private boolean closedSeScope;
    private Expression expression;
    private ControlStructure lastClosedSeScope;

    public ControlStrucutresAssembler(SimbolTable simbolTable){
        super(simbolTable);
    }

    public void executeAction(int action, String lexeme) {
        super.executeAction(action,lexeme);
        switch (action){
            case 915:
                setRelationalOperatorOnRelationalExpression(lexeme);
                break;
            case 912:
                possibleGotRelationalOperand(lexeme);
                break;
        }
    }

    protected void setRelationalOperatorOnRelationalExpression(String operator) {
        expression.setOperator(operator);
    }

    private void possibleGotRelationalOperand(String lexeme) {
        getExpression().addOperand(getVarNameOrInt(lexeme));
    }

    public Expression getExpression() {
        return expression;
    }

}
