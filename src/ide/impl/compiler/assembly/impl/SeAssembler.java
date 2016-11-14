package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.ControlStructure;

public class SeAssembler extends ControlStrucutresAssembler {

    private boolean closedSeScope;
    private Se se;
    private boolean convetedToSenao;

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
        notifyFinalizedAction(action, lexeme);
    }

    private void areClosingSeScope() {
        closedSeScope = true;
    }

    @Override
    protected void notifyFinalizedAction(int action,String lexeme) {
        buildSeIfClosedSeScopeAndNotComingSenao(action, lexeme);
    }

    private void buildSeIfClosedSeScopeAndNotComingSenao(int action, String lexeme){
        if(action==8 || action ==914 || convetedToSenao)
            return;
        boolean notComingSenao = !"SENAO".equalsIgnoreCase(lexeme);
        if(closedSeScope && notComingSenao){
            finalizeAndNotify(this);
        }
    }

    private void areClosingSenaoScopeSoFinalize(){
        finalizeAndNotify(this);
    }

    private void areOpeningSenaoScopeSoConvertSeToSenao(){
        se.convertToSenao();
        convetedToSenao = true;
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
