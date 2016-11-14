package ide.impl.compiler.assembly.impl;

import gals.*;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.Assembler;
import lombok.Data;

import java.util.Stack;

@Data
public class AssemblerImpl extends Semantico implements Assembler {

    private final Stack<GeneralAssembler> assemblers;
    private String code = "";
    private Assembly assembly;
    private SimbolTable simbolTable;

    private final AssemblerListener assemblerListenerRemoveFromStack = new AssemblerListener() {
        @Override
        public void finalizedAssembler(GeneralAssembler assembler) {
            GeneralAssembler assemblerFromPop = assemblers.pop();
            System.out.println("Finalizou assembler " + assemblerFromPop);
        }
    };

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
        addCode();
        return assembly;
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
        System.out.println(action);
        String lexeme = token.getLexeme();
        switch (action) {
            case 8:
                addNewAssembler(lexeme);
                break;
            case 7:
                finalizeBuildProgramAssembly();
        }
        GeneralAssembler assembler = assemblers.peek();
        assembler.executeAction(action, lexeme);
    }

    private void addNewAssembler(String lexeme) {

        GeneralAssembler currentAssembler = null;
        if(!assemblers.empty())
            currentAssembler = assemblers.peek();

        GeneralAssembler newAssembler = createAssembler(lexeme);
        newAssembler.addListener( assemblerListenerRemoveFromStack );
        assemblers.push(newAssembler);

        if(currentAssembler != null)
            currentAssembler.addChild( newAssembler );

    }


    private void finalizeBuildProgramAssembly() {
        GeneralAssembler program = assemblers.peek();
        if(program instanceof ProgramaAssembler)
            assembly = program.build();
    }

    private GeneralAssembler createAssembler(String lexeme) {
        switch (lexeme){
            case "se":
                return new SeAssembler(simbolTable, lexeme);
            case "programa":
                return new ProgramaAssembler(simbolTable);
            default:
                return new FuncaoAssembler(simbolTable);
        }
    }

}
