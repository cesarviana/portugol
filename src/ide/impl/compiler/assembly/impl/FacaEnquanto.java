package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.assembly.ControlStructure;

public class FacaEnquanto extends ControlStructure {
    private Assembly facaEnquantoText;

    public FacaEnquanto() {
        this.facaEnquantoText = new Assembly();
    }

    @Override
    public void build() {
        addBegin();
        addFacaEnquantoCode();
        createExpression();
        jumpToBegin();
    }

    private void jumpToBegin() {
        getLines().add(getExpression().getBranchCommand() + " " + inicioFacaEnquanto());
    }

    private void addBegin() {
        getLines().add(inicioFacaEnquanto() + ":");
    }

    private void createExpression() {
        getExpression().build();
        getLines().addAll(getExpression().getLines());
    }

    private void addFacaEnquantoCode() {
        getLines().addAll(facaEnquantoText.getLines());
    }

    private String inicioFacaEnquanto() {
        return "INICIO_" + getAssemblyScope();
    }

    @Override
    public void addLine(String line) {
        facaEnquantoText.addLine(line);
    }
}
