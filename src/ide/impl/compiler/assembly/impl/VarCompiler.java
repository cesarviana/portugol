package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.Scope;
import ide.impl.compiler.Var;
import ide.impl.compiler.VarVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VarCompiler {
    private Var var;

    public VarCompiler(Var var) {
        this.var = var;
    }

    public static VarCompiler instance(Var var) {
        if (var instanceof VarVector) {
            return new VarVectorCompiler(var);
        }
        return new VarCompiler(var);
    }

    public String getDataDeclaration() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(" : ").append(initialization());
        return sb.toString();
    }

    public String getName() {
        Scope scope = var.getScope();
        String varId = var.getId();
        String scopeId = scope.getId();
        if (scope.isGlobalScope())
            return varId;
        String varName = scopeId.replace("->", "_") + "_" + varId;
        return varName;
    }

    public String initialization() {
        return "0";
    }
}
