package ide.impl.compiler.assembly.impl;

import gals.*;
import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import ide.impl.compiler.assembly.Assembler;
import ide.impl.compiler.assembly.Assembly;
import lombok.Data;

@Data
public class AssemblerImpl extends Semantico implements Assembler {
    public static final String INICIO = "inicio";
    public static final String READING = "reading";
    public static final String READING_VECTOR = "reading_vector";
    public static final String WRITING = "writing";
    public static final String WRITING_VECTOR = "writing_vector";
    public static final String ADDING = "adding";
    public static final String ASSIGNING = "assigning";
    private SimbolTable simbolTable;
    private Assembly assembly;
    private String code = "";
    private String state = "";
    private int vectorPosition = 0;
    private String id;

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
                state = READING;
                break;
            case 301:
                if(state == READING){
                    assembly.addText("LD $in_port");
                    assembly.addText("STO " + token.getLexeme());
                } else if( state == READING_VECTOR ) {
                    assembly.addText("LDI " + vectorPosition);
                    assembly.addText("STO $indr");
                    assembly.addText("LD $in_port");
                    assembly.addText("STOV " + id );
                }
                state = "";
                id = "";
                vectorPosition = 0;
                break;
            case 302:
                if(state == WRITING){
                    String lexeme = token.getLexeme();
                    String command = lexeme.matches("[0-9]*") ? "LDI" : "LD";
                    assembly.addText(command + " " + lexeme);;
                    assembly.addText("STO $out_port");
                } else if(state == WRITING_VECTOR){
                    assembly.addText("LDI " + vectorPosition);
                    assembly.addText("STO $indr");
                    assembly.addText("LDV " + id);
                    assembly.addText("STO $out_port");
                }
                state = "";
                id = "";
                vectorPosition = 0;
                break;
            case 14:
                if(state == READING) {
                    state = READING_VECTOR;
                } else if(state == WRITING) {
                    state = WRITING_VECTOR;
                }
                vectorPosition = Integer.parseInt(token.getLexeme());
                break;
            case 2:
                id = token.getLexeme();
                break;
            case 400:
                state = WRITING;
                break;
            case 499:
                String lexeme = token.getLexeme();
                boolean immediate = lexeme.matches("[0-9]*");
                if(state == ASSIGNING){
                    String command = immediate ? "LDI" : "LD";
                    assembly.addText(command + " " + lexeme);
                } else if( state == ADDING ) {
                    String command = immediate ? "ADDI" : "ADD";
                    assembly.addText(command + " " + immediate);
                }
                break;
            case 500:
                state = ADDING;
                break;
            case 498:

                break;
        }
    }
}
