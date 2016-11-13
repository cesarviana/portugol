package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.ControlStructure;

public class FacaEnquantoAssembler extends ControlStrucutresAssembler {

    private final ControlStructure facaEnquanto;
    private boolean iniciouEnquantoExpression;

    public FacaEnquantoAssembler(SimbolTable simbolTable) {
        super(simbolTable);
        this.facaEnquanto = new FacaEnquanto();
    }

    @Override
    public void executeAction(int action, String lexeme) {
        if (!iniciouEnquantoExpression)
            blockExpressionToAvoidGetWrongOperandsWhileItsInsideFacaScopeDoYouWannaAFuchShortNameIKnowItsCoolIsntThereSomeWayICanPutAMemeHereSorry();
        switch (action) {
            case 18:
                iniciouEnquantoExpression = true;
                setExpressionOpenedToReceiveOperands();
                break;
        }
        super.executeAction(action, lexeme);

    }

    private void setExpressionOpenedToReceiveOperands() {
        getExpression().open();
    }

    private void blockExpressionToAvoidGetWrongOperandsWhileItsInsideFacaScopeDoYouWannaAFuchShortNameIKnowItsCoolIsntThereSomeWayICanPutAMemeHereSorry() {
        if (!getExpression().isClosed()) {
            getExpression().close();
        }

    }

    @Override
    public ControlStructure getControlStructure() {
        return facaEnquanto;
    }

    @Override
    public Assembly getAssemblyPart() {
        return facaEnquanto;
    }

}
