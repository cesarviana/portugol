package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import ide.impl.compiler.assembly.Assembler;
import ide.impl.compiler.assembly.Assembly;
import lombok.Data;

@Data
public class AssemblerImpl implements Assembler {
    private SimbolTable simbolTable;
    private Assembly assembly;
    public AssemblerImpl() {
        assembly = new Assembly();
    }

    @Override
    public void setSimbolTable(SimbolTable table){
        this.simbolTable = table;
    }

    @Override
    public Assembly assembly() {
        for(Scope scope : simbolTable.getScopes().values()){
            for(Var var : scope.getVars().values()){
                VarCompiler varCompiler = VarCompiler.instance(var);
                assembly.addData( varCompiler.getDataDeclaration() );
            }
        }
        return assembly;
    }

}
