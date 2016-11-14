package ide.impl.compiler.assembly.impl;

import gals.*;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.Assembler;
import lombok.Data;
import lombok.Getter;

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
        clearStaticControls();
        parse();
        executeLastStep();
    }

    private void clearStaticControls() {
        GeneralAssembler.clearScopeStack();
        step1 = null;
        step2 = null;
    }

    private void parse() {
        Lexico lexico = new Lexico(code);
        Sintatico sintatico = new Sintatico();
        try {
            sintatico.parse(lexico, this);
        } catch (LexicalError | SemanticError | SyntaticError error) {
            System.err.println(error.getMessage());
        }
    }

    public class Step {
        private int action;
        @Getter
        private String lexeme;
        public Step(int action, String lexeme) {
            this.action = action;
            this.lexeme = lexeme;
        }
    }

    @Getter
    private static Step step1, step2;

    @Override
    public void executeAction(int action, Token token) throws SemanticError {
        addSteps(action, token);
        executeStepIfHasTwo();
    }

    private void addSteps(int action, Token token) {
        String lexeme = token.getLexeme();
        if(step1 == null) {
            step1 = new Step(action, lexeme);
        } else if(step2==null) {
            step2 = new Step( action, lexeme );
        } else {
            step1 = step2;
            step2 = new Step( action, lexeme );
        }
    }

    private void executeStepIfHasTwo(){
        if(step1!=null && step2 != null)
            execute(step1);
    }

    private void executeLastStep(){
        execute(step2);
    }

    private void execute(Step step) {
        switch (step.action) {
            case 8:
                addNewAssembler(step.lexeme);
                break;
            case 7:
                finalizeBuildProgramAssembly();
        }
        GeneralAssembler assembler = assemblers.peek();
        assembler.executeAction(step.action, step.lexeme);
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
            case "enquanto":
                return new EnquantoAssembler(simbolTable);
            case "faca":
                return new FacaEnquantoAssembler(simbolTable);
            default:
                return new FuncaoAssembler(simbolTable);
        }
    }

}
