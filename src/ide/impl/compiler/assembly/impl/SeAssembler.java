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
            case 7:
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
        buildSeIfClosedSeScopeAndNotComingSenao();
    }

    private void buildSeIfClosedSeScopeAndNotComingSenao(){
        if(!closedSeScope || convetedToSenao)
            return;
        String nextLexeme = nextLexeme();
        boolean notComingSenao = !"SENAO".equalsIgnoreCase(nextLexeme);
        if(notComingSenao){
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
