package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.ControlStructure;

public class SeAssembler extends ControlStrucutresAssembler {

    public static final int CLOSING_SE_SCOPE = 914;
    public static final int CLOSING_SENAO_SCOPE = 917;
    private boolean closedSeScope;
    private Se se;

    public SeAssembler(SimbolTable simbolTable, String lexeme) {
        super(simbolTable);
        se = new Se(lexeme);
    }

    public void executeAction(int action, String lexeme) {
        super.executeAction(action, lexeme);
        switch (action){
            case 913:
                areAfterExpInsideSe();
                break;
            case 914:
                areClosingSeScope();
                break;
            case 916:
                areOpeningSenaoScopeSoConvertSeToSenao();
                break;
            case 917:
                buildSenao();
        }
        buildSeIfClosedSeScopeAndNotComingSenao(action,lexeme);
    }

    private void areClosingSeScope() {
        closedSeScope = true;
    }

    private void buildSeIfClosedSeScopeAndNotComingSenao(int action, String lexeme){
        System.err.println("Implementar build se");
//        if(action== CLOSING_SE_SCOPE || action == CLOSING_SENAO_SCOPE)
//            return;
//        boolean notComingSenao = !"SENAO".equalsIgnoreCase(lexeme);
//        if(closedSeScope && notComingSenao){
//            lastClosedSeScope.build();
//            lastClosedSeScope = null;
//            closedSeScope = false;
//        }
        notifyFinalized(this);
    }

    private void buildSenao(){
        System.err.println("Implementar build senão");
    }

    private void areAfterExpInsideSe() {
        System.err.println("Implementar conversão do se pra senão");
//        ControlStructure currentRelExp = controlStructures.peek();
//        currentRelExp.setExpression( getExpression() );
    }

    private void areOpeningSenaoScopeSoConvertSeToSenao(){
        System.err.println("Implementar conversão do se pra senão");
    }

    @Override
    public Assembly getAssemblyPart() {
        return se.build();
    }
}
