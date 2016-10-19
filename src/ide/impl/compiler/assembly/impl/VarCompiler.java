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
        if(var instanceof VarVector){
            return new VarVectorCompiler(var);
        }
        return new VarCompiler(var);
    }

    public String getDataDeclaration(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(" : ").append( initialization() );
        return sb.toString();
    }

    private String getName() {
        List<String> names = new ArrayList<>();
        Scope scope = var.getScope();
        while( scope != Scope.NULL ){
            names.add( scope.getId() );
            scope = scope.getParent();
        }
        StringBuilder sb = new StringBuilder();
        Collections.reverse( names );
        names.forEach(scopeName->{sb.append(scopeName).append("_");});
        return sb.append(var.getId()).toString();
    }

    public String initialization() {
        return "0";
    }
}
