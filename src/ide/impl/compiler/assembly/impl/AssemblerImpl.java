package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;
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
    public Assembly assembly() {
        return assembly;
    }
}
