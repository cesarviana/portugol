package ide.impl.compiler.assembly;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.impl.AssemblerImpl;

public interface Assembler {
    public Assembly assembly();

    static Assembler instance() {
        return new AssemblerImpl();
    }

    void setSimbolTable(SimbolTable simbolTable);
}
