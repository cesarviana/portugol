package ide.impl.compiler.assembly.impl;

import gals.*;
import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import ide.impl.compiler.assembly.Assembler;
import lombok.Data;

import java.util.Stack;

@Data
public class AssemblerImpl extends Semantico implements Assembler {

    private final Stack<GeneralAssembler> assemblers;
    private String code = "";
    private Assembly assembly;
    private SimbolTable simbolTable;

    public AssemblerImpl() {
        assemblers = new Stack<>();
        assembly = new Assembly();
    }

    @Override
    public void setSimbolTable(SimbolTable table) {
        this.simbolTable = table;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public Assembly assembly() {
        addData();
        addText();
        return assembly;
    }

    private void addData() {
        assembly.addLine(".data");
        for (Scope scope : simbolTable.getScopes().values()) {
            for (Var var : scope.getVars().values()) {
                VarCompiler varCompiler = VarCompiler.instance(var);
                assembly.addLine(varCompiler.getDataDeclaration());
            }
        }
    }

    private void addText() {
        assembly.addLine(".text");
        if (!code.isEmpty()) {
            addCode();
        }
        assembly.addLine("HLT 0");
    }

    private void addCode() {
        Lexico lexico = new Lexico(code);
        Sintatico sintatico = new Sintatico();
        try {
            sintatico.parse(lexico, this);
        } catch (LexicalError | SemanticError | SyntaticError error) {
            System.err.println(error.getMessage());
        }
    }

    @Override
    public void executeAction(int action, Token token) throws SemanticError {
        switch (action) {
            case 0:
                assemblers.push( createAssembler(token.getLexeme()) );
                break;
        }

        assemblers.peek().executeAction(action, token.getLexeme());
    }

    private GeneralAssembler createAssembler(String lexeme) {
        switch (lexeme){
            case "se":
                return new SeAssembler(simbolTable);
            case "programa":
                return new ProgramaAssembler(simbolTable);
            default:
                return new FuncaoAssembler(simbolTable);
        }
    }

}
