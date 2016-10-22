package ide.impl.compiler.assembly.impl;

import gals.*;
import ide.impl.compiler.Scope;
import ide.impl.compiler.SemanticoPortugol;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import ide.impl.compiler.assembly.Assembler;
import ide.impl.compiler.assembly.Assembly;
import lombok.Data;

@Data
public class AssemblerImpl extends Semantico implements Assembler {
    public static final String INICIO = "inicio";
    private SimbolTable simbolTable;
    private Assembly assembly;
    private String code = "";
    private boolean vaiEscrever;

    public AssemblerImpl() {
        assembly = new Assembly();
    }

    @Override
    public void setSimbolTable(SimbolTable table){
        this.simbolTable = table;
    }

    @Override
    public void setCode(String code){
        this.code = code;
    }

    @Override
    public Assembly assembly() {
        addData();
        addText();
        return assembly;
    }

    private void addData() {
        for(Scope scope : simbolTable.getScopes().values()){
            for(Var var : scope.getVars().values()){
                VarCompiler varCompiler = VarCompiler.instance(var);
                assembly.addData( varCompiler.getDataDeclaration() );
            }
        }
    }

    private void addText() {
        if(!code.isEmpty()) {
            addCode();
        }
        assembly.addText("HLT 0");
    }

    private void addCode() {
        Lexico lexico = new Lexico(code);
        Sintatico sintatico = new Sintatico();
        try {
            sintatico.parse(lexico, this);
        } catch (LexicalError lexicalError) {
            lexicalError.printStackTrace();
        } catch (SyntaticError syntaticError) {
            syntaticError.printStackTrace();
        } catch (SemanticError semanticError) {
            semanticError.printStackTrace();
        }
    }

    @Override
    public void executeAction(int action, Token token) throws SemanticError {
        switch (action){
            case 0 :
                if(INICIO.equals(token.getLexeme())){
                    assembly.addText("_PRINCIPAL:");
                }
                break;
            case 300 :
                assembly.addText("LD $in_port");
                break;
            case 301:
                assembly.addText("STO " + token.getLexeme());
                break;
            case 302:
                String lexeme = token.getLexeme();
                String command = lexeme.matches("[0-9]*") ? "LDI" : "LD";
                assembly.addText(command + " " + lexeme);;
                assembly.addText("STO $out_port");
                break;
        }
    }
}
