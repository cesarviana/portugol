package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.Assembler;

public class AssemblerImpl implements Assembler {
    private SimbolTable simbolTable;

    public AssemblerImpl(SimbolTable simbolTable) {
        this.simbolTable = simbolTable;
    }

    @Override
    public void generate() {

    }
}
