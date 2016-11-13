package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.ControlStructure;

public class EnquantoAssembler extends ControlStrucutresAssembler {

    private final ControlStructure enquanto;

    public EnquantoAssembler(SimbolTable simbolTable) {
        super(simbolTable);
        this.enquanto = new Enquanto();
    }

    @Override
    public void executeAction(int action, String lexeme) {
        super.executeAction(action, lexeme);
    }

    @Override
    public ControlStructure getControlStructure() {
        return enquanto;
    }

    @Override
    public Assembly getAssemblyPart() {
        return enquanto;
    }

}
