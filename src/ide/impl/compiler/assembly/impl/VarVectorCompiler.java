package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.Var;
import ide.impl.compiler.VarVector;

public class VarVectorCompiler extends VarCompiler {
    private VarVector vector;

    public VarVectorCompiler(Var var) {
        super(var);
        this.vector = (VarVector) var;
    }

    @Override
    public String initialization() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<vector.getSize(); i++){
            sb.append("0").append(",");
        }
        if(sb.length()>0)
            removeLastComma(sb);
        return sb.toString();
    }

    private void removeLastComma(StringBuilder sb) {
        sb.deleteCharAt(sb.length()-1);
    }
}
