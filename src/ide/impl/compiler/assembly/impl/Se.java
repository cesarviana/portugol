package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.assembly.Assembler;
import ide.impl.compiler.assembly.ControlStructure;

/**
 * Created by cassiano on 30/10/16.
 */
public class Se extends ControlStructure {

    private boolean senao;
    private String seScopeStr;
    private AssemblyPart seText, senaoText;

    public Se(String seScopeStr){
        super();
        this.seScopeStr = renameScopeToBranch(seScopeStr);
        seText = new AssemblyPart();
        senaoText = new AssemblyPart();
    }

    @Override
    public AssemblyPart build() {
        createExpression();
        if(senao){
            branchToSenao();
        } else {
            branchToFimSe();
        }
        addSeCode();
        if(senao){
            jumpToFimSe();
            addSenaoBranch();
            addSenaoCode();
        }
        addFimSe();
        return this;
    }

    private void createExpression() {
        addText( expression.toString() );
    }

    private void branchToSenao() {
        addText( expression.getBranchCommand() + " " + senao() );
    }

    private void branchToFimSe() {
        addText( expression.getBranchCommand() + " " + fimSe() );
    }

    private void addSeCode() {
        addText( seText.toString() );
    }

    private void jumpToFimSe() {
        addText("JMP " + fimSe());
    }

    private void addSenaoBranch() {
        addText(senao() + ":");
    }

    private void addSenaoCode() {
        addText( senaoText.toString() );
    }

    private void addFimSe() {
        addText(fimSe());
    }

    private String fimSe() {
        return "FIM_" + seScopeStr;
    }

    private String senao(){
        return "CRIAR_SENAO";
    }

    public void convertToSenao() {
        this.senao = true;
    }

    @Override
    public void addText(String s) {
        if(senao)
            senaoText.addText(s);
        else
            seText.addText(s);
    }

}
