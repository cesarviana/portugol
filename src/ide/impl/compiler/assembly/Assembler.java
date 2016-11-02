package ide.impl.compiler.assembly;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.impl.AssemblerImpl;
import ide.impl.compiler.assembly.impl.Assembly;

public interface Assembler {

    static Assembler instance() {
        return new AssemblerImpl();
    }

    Assembly assembly();

    void setSimbolTable(SimbolTable simbolTable);

    void setCode(String code);

}
