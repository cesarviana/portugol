package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.assembly.RelExpAsmBuilder;

/**
 * Created by cassiano on 30/10/16.
 */
public class RelExpAsmBuilderImpl implements RelExpAsmBuilder {
    private String scopeBne;
    private String leftOperand, rightOperand;
    private boolean watching = false;
    private String relationalOperator;

    private StringBuilder sb;

    public RelExpAsmBuilderImpl() {
        sb = new StringBuilder();
    }

    @Override
    public String build() {

        add("LD " + leftOperand);
        add("STO 1000");
        add("LD " + rightOperand);
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");
        add("BNE " + scopeBne, false);

        String result = sb.toString();
        return result;
    }

    @Override
    public void addOperand(String operand) {
        if (!watching) return;
        if (leftOperand == null)
            leftOperand = operand;
        else
            rightOperand = operand;

    }

    public void setRelationalOperator(String relationalOperator) {
        if (!watching) return;
        this.relationalOperator = relationalOperator;
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

    public void add(String text) {
        sb.append(text).append("\n");
    }

    public void add(String text, boolean breakLine) {
        sb.append(text);
        if(breakLine)
            sb.append("\n");
    }
}
