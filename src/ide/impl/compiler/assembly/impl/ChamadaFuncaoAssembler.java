package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.Function;
import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;

public class ChamadaFuncaoAssembler extends GeneralAssembler {

    public ChamadaFuncaoAssembler(SimbolTable simbolTable) {
        super(simbolTable);
    }

    private ChamadaFuncaoAssembly chamadaFuncao = new ChamadaFuncaoAssembly();

    @Override
    public void executeAction(int action, String lexeme) {
        super.executeAction(action,lexeme);
        switch (action){
            case 8:
                Scope function = getSimbolTable().getScope(lexeme);
                assert function instanceof Function;
                chamadaFuncao.setFunction((Function) function);
                break;
            case 302:
                chamadaFuncao.addArg(lexeme);
                break;
        }
    }

    @Override
    public Assembly getAssemblyPart() {
        return chamadaFuncao;
    }
}
