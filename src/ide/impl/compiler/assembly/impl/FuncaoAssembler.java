package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;

public class FuncaoAssembler extends GeneralAssembler {
    private String scope;
    public FuncaoAssembler(SimbolTable simbolTable) {
        super(simbolTable);
    }

    @Override
    public void executeAction(int action, String lexeme) {
        super.executeAction(action, lexeme);
        if(action==8 && scope==null){
            scope = "_" + lexeme.toUpperCase();
            scope = scope.equals("_INICIO") ? "_PRINCIPAL" : scope;
        }
    }

    @Override
    public Assembly build() {
        Assembly assembly = new Assembly();
        assembly.addLine(scope + ":");
        assembly.addAssembly( getAssemblyPart() );

        if( ! scope.equals("_PRINCIPAL") )
            assembly.addLine( "RETURN 0 ");

        return assembly;
    }
}
