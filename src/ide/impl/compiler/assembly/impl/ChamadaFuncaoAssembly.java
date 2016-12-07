package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.Function;
import ide.impl.compiler.Var;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChamadaFuncaoAssembly extends Assembly {

    private List<String> args;
    private Function function;

    public ChamadaFuncaoAssembly() {
        this.args = new ArrayList<>();
    }
    @Override
    public void build() {
        Iterator<Var> params = function.getParams().iterator();
        Iterator<String> args = this.args.iterator();
        while(args.hasNext()){
            Var param = params.next();
            String arg = args.next();
            getLines().add( GeneralAssembler.createCommand("LD", GeneralAssembler.getVarName(arg) ) );
            getLines().add( GeneralAssembler.createCommand("STO", function.getId() + "_" + param.getId() ) );
        }
        getLines().add( "CALL " + "_"+function.getId().toUpperCase() );
    }

    public void addArg(String arg){
        this.args.add(arg);
    }

    public void setFunction(Function function){
        this.function = function;
    }
}
