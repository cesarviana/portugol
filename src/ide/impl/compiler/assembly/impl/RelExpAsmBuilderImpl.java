package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.assembly.Assembler;
import ide.impl.compiler.assembly.RelExpBuilder;

/**
 * Created by cassiano on 30/10/16.
 */
public class RelExpAsmBuilderImpl implements RelExpBuilder {
    private String scopeBne;
    private String leftOperand, rightOperand;
    private boolean watching = false;


    @Override
    public void build(Assembler assembler) {

        assembler.command("LD", leftOperand);
        assembler.addText("STO 1000");
        assembler.command("LD", rightOperand);
        assembler.addText("STO 1001");
        assembler.addText("LD 1000");
        assembler.addText("SUB 1001");
        assembler.addText("BNE " + scopeBne);

        assembler.popIdOrValue();
        assembler.popIdOrValue();

    }

    @Override
    public void addOperand(String operand) {
        if (!watching) return;
        if (leftOperand == null)
            leftOperand = operand;
        else
            rightOperand = operand;

    }

    @Override
    public void startWatching() {
        watching = true;
    }

    @Override
    public void stopWatching() {
        watching = false;
    }

    @Override
    public String useBranchIfNotEqual() {
        return scopeBne;
    }

    @Override
    public void setBranchIfNotEqual(String scope) {
        if (!watching) return;
        this.scopeBne = "FIM_" + scope.toUpperCase().replace("->", "_");
    }
}
