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
    public void build() {
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
    }

    private void createExpression() {
        getExpression().build();
        getLines().addAll( getExpression().getLines() );
    }

    private void branchToSenao() {
        String branchToSenao = getExpression().getInvertedBranchCommand() + " " + senao();
        getLines().add(branchToSenao);
    }

    private void branchToFimSe() {
        String branchFimSe = getExpression().getInvertedBranchCommand() + " " + fimSe();
        getLines().add( branchFimSe );
    }

    private void addSeCode() {
        getLines().addAll( seText.getLines() );
    }

    private void jumpToFimSe() {
        getLines().add( "JMP " + fimSe());
    }

    private void addSenaoBranch() {
        getLines().add(senao() + ":");
    }

    private void addSenaoCode() {
        getLines().addAll( senaoText.getLines() );
    }

    private void addFimSe() {
        getLines().add(fimSe() + ":");
    }

    private String fimSe() {
        return "FIM_" +  getAssemblyScope();
    }

    private String senao(){
        String scope = getAssemblyScope();
        String num = scope.substring(scope.length() - 1, scope.length());
        return "INICIO_"+scope.substring(0, scope.length()-1) + "NAO" + num;
    }

    public void convertToSenao() {
        this.senao = true;
    }

    @Override
    public void addLine(String s) {
        if(!getExpression().isClosed()){
            getExpression().addLine( s );
            return;
        }
        if(senao)
            senaoText.addLine(s);
        else
            seText.addLine(s);
    }

}
