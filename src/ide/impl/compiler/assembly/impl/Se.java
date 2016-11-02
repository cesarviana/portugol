package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.assembly.ControlStructure;

/**
 * Created by cassiano on 30/10/16.
 */
public class Se extends ControlStructure {

    private boolean senao;
    private String seScopeStr;
    private Assembly seText, senaoText;

    public Se(String seScopeStr){
        super();
        this.seScopeStr = renameScopeToBranch(seScopeStr);
        seText = new Assembly();
        senaoText = new Assembly();
    }

    @Override
    public Assembly build() {
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
        addLine( getExpression().toString() );
    }

    private void branchToSenao() {
        addLine( getExpression().getBranchCommand() + " " + senao() );
    }

    private void branchToFimSe() {
        addLine( getExpression().getBranchCommand() + " " + fimSe() );
    }

    private void addSeCode() {
        addLine( seText.toString() );
    }

    private void jumpToFimSe() {
        addLine("JMP " + fimSe());
    }

    private void addSenaoBranch() {
        addLine(senao() + ":");
    }

    private void addSenaoCode() {
        addLine( senaoText.toString() );
    }

    private void addFimSe() {
        addLine(fimSe());
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
    public void addLine(String s) {
        if(senao)
            senaoText.addLine(s);
        else
            seText.addLine(s);
    }

}
