package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;

public class ProgramaAssembler extends GeneralAssembler {
    public ProgramaAssembler(SimbolTable simbolTable) {
        super(simbolTable);
    }

    Assembly assembly = new Assembly();

    private void addData() {
        assembly.addLine(".data");
        for (Scope scope : getSimbolTable().getScopes().values()) {
            for (Var var : scope.getVars().values()) {
                VarCompiler varCompiler = VarCompiler.instance(var);
                assembly.addLine(varCompiler.getDataDeclaration());
            }
        }
    }

    @Override
    public Assembly build() {
        addData();
        assembly.addLine(".text");
        assembly.addAssembly( getAssemblyPart() );
        assembly.addLine("HLT 0");
        return assembly;
    }
}
