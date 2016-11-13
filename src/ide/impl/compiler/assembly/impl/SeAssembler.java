package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.ControlStructure;

public class SeAssembler extends ControlStrucutresAssembler {

    private boolean closedSeScope;
    private Se se;

    public SeAssembler(SimbolTable simbolTable, String lexeme) {
        super(simbolTable);
        se = new Se(lexeme);
    }

    public void executeAction(int action, String lexeme) {
        super.executeAction(action, lexeme);
        switch (action){
            case 914:
                areClosingSeScope();
                break;
            case 916:
                areOpeningSenaoScopeSoConvertSeToSenao();
                break;
            case 917:
                areClosingSenaoScopeSoFinalize();
        }
        buildSeIfClosedSeScopeAndNotComingSenao(action,lexeme);
    }

    private void areClosingSeScope() {
        closedSeScope = true;
    }

    private void buildSeIfClosedSeScopeAndNotComingSenao(int action, String lexeme){
        if(action==8 || action ==914)
            return;
        boolean notComingSenao = !"SENAO".equalsIgnoreCase(lexeme);
        if(closedSeScope && notComingSenao){
            notifyFinalized(this);
        }
    }

    private void areClosingSenaoScopeSoFinalize(){
        notifyFinalized(this);
    }

    private void areOpeningSenaoScopeSoConvertSeToSenao(){
        se.convertToSenao();
    }

    @Override
    public Assembly getAssemblyPart() {
        return se;
    }

    @Override
    public ControlStructure getControlStructure() {
        return se;
    }
}
