package ide.impl.compiler.assembly;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.impl.AssemblerImpl;

public interface Assembler {

    Assembly assembly();

    static Assembler instance() {
        return new AssemblerImpl();
    }

    void setSimbolTable(SimbolTable simbolTable);

    void setCode(String code);

    String command(String command, String lexeme);

    String createCommand(String command, String lexeme);

    void addText(String s);

    String popIdOrValue();
}
