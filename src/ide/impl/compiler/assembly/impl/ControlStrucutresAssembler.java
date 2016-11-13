package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.ControlStructure;

public abstract class ControlStrucutresAssembler extends GeneralAssembler {

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
                clearStackToAvoidExcedents();// the right should be convert the Expression from Assembly to a Assembler, thus it would have its own stack
                break;
            case 913:
                getExpression().close();
        }
    }

    private void clearStackToAvoidExcedents() {
        if(!getExpression().isClosed())
            idsOrValues.pop();
    }

    protected void setRelationalOperatorOnRelationalExpression(String operator) {
        getExpression().setOperator(operator);
    }

    private void possibleGotRelationalOperand(String lexeme) {
        String varNameOrInt = getVarNameOrInt(lexeme);
        Expression expression = getExpression();
        expression.addOperand(varNameOrInt);
    }

    public Expression getExpression() {
        return getControlStructure().getExpression();
    }

    public abstract ControlStructure getControlStructure();

}
