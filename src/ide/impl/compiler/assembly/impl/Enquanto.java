package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.assembly.ControlStructure;

public class Enquanto extends ControlStructure {

    private Assembly enquantoText;

    public Enquanto() {
        this.enquantoText = new Assembly();
    }

    @Override
    public void build() {
        addBegin();
        createExpression();
        jumpToEnd();
        addEnquantoCode();
        jumpToBegin();
        addEnd();
    }

    private void addBegin() {
        getLines().add(inicioEnquanto() + ":");
    }

    private void createExpression() {
        getExpression().build();
        getLines().addAll(getExpression().getLines());
    }

    private void jumpToEnd() {
        getLines().add(getExpression().getInvertedBranchCommand() + " " + fim());
    }

    private void addEnquantoCode() {
        getLines().addAll(enquantoText.getLines());
    }

    private void jumpToBegin() {
        getLines().add("JMP " + inicioEnquanto());
    }

    private String inicioEnquanto() {
        return "INICIO_" + getAssemblyScope();
    }

    @Override
    public void addLine(String line) {
        enquantoText.addLine(line);
    }
}
