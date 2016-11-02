package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;

import java.util.*;

public abstract class GeneralAssembler {

    public static final String PROGRAMA = "programa";
    public static final String READING = "reading";
    public static final String READING_VECTOR = "reading_vector";
    public static final String ADDING = "adding";

    private static final String ASSIGNING = "assigning";

    public static final String WRITING = "writing";
    public static final String WRITING_VECTOR = "writing_vector";
    public static final String SUBTRACTING = "subtracting";
    public static final String DECLARING = "declaring";

    private SimbolTable simbolTable;
    private int vectorPosition = 0;
    private String id;
    private String idThatWillReceiveAssigning;
    protected static String scope;
    private final LinkedList<String> states;
    private final LinkedList<String> idsOrValues;
    private String indexWhereVectorWillReceiveAssigning;
    private boolean varToVector;
    private final Set<String> vectors;
    private boolean negative = false;

    private Assembly assemblyPart;
    private final List<GeneralAssembler> childrens;
    private final List<AssemblerListener> listeners;

    private final AssemblerListener childFinalizedListener = new AssemblerListener() {
        @Override
        public void finalizedAssembler(GeneralAssembler assembler) {
            boolean childFinalized = childrens.contains(assembler);
            if(childFinalized) {
                addChildAssembly(assembler);
                childrens.remove(assembler);
            }
        }

        private void addChildAssembly(GeneralAssembler assembler) {
            getAssemblyPart().addAssembly( assembler.build() );
        }
    };

    public GeneralAssembler(SimbolTable simbolTable) {
        this.simbolTable = simbolTable;
        states = new LinkedList<>();
        idsOrValues = new LinkedList<>();
        vectors = new HashSet<>();
        assemblyPart = new Assembly();
        childrens = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public void executeAction(int action, String lexeme){
        switch (action) {
            case 0:
                scope = lexeme;
                break;
            case 7:
                notifyFinalized(this);
                break;
            case 1:
                states.push(DECLARING);
                break;
            case 300:
                states.add(READING);
                break;
            case 301:
                if (states.isEmpty())
                    return;
                if (states.peek() == READING) {
                    addText("LD $in_port");
                    addText("STO " + getVarName(lexeme));
                } else if (states.peek() == READING_VECTOR) {
                    addText("LDI " + vectorPosition);
                    addText("STO $indr");
                    addText("LD $in_port");
                    addText("STOV " + getVarName(id));
                }
                states.pop();
                id = "";
                vectorPosition = 0;
                break;
            case 302:
                if (states.peek() == WRITING) {
                    command("LD", lexeme);
                    addText("STO $out_port");
                } else if (states.peek() == WRITING_VECTOR) {
                    addText("LDI " + vectorPosition);
                    addText("STO $indr");
                    addText("LDV " + getVarName(id));
                    addText("STO $out_port");
                }
                states.pop();
                id = "";
                vectorPosition = 0;
                break;
            case 14:
                if (states.isEmpty())
                    return;
                if (states.peek() == READING) {
                    states.pop();
                    states.push(READING_VECTOR);
                } else if (states.peek() == WRITING) {
                    states.pop();
                    states.push(WRITING_VECTOR);
                }
                vectorPosition = Integer.parseInt(lexeme);
                break;
            case 2:
                id = lexeme;
                if (states.peek() != DECLARING) {
                    if (negative)
                        idsOrValues.push("-" + id);
                    else
                        idsOrValues.push(id);
                } else {
                    states.clear();
                }
                negative = false;
                break;
            case 400:
                states.push(WRITING);
                break;
            case 600:
                idThatWillReceiveAssigning = id;
                states.push(ASSIGNING);
                storeVectorIndexToStackIfIsAssigningVector(lexeme);
                break;
            case 601:
                String signal = negative ? "-" : "";
                idsOrValues.push(signal + lexeme);
                negative = false;
                break;
            case 41:
                if (states.isEmpty())
                    return;
                boolean isOperation = states.contains(SUBTRACTING) || states.contains(ADDING);
                boolean vectorIsTheLast = "]".equals(lexeme);
                if (vectorIsTheLast && isOperation) {
                    consumesExpressionToAssemblyVectorAsLastOperand(lexeme);
                } else {
                    removesFromTheStackTheVarThatWillReceiveTheExpressionResult(lexeme);
                    consumeExpressionToAssembly(lexeme);
                    storeExpression(lexeme);
                    idsOrValues.clear();
                }
                break;
            case 500:
                states.push(ADDING);
                break;
            case 501:
                states.push(SUBTRACTING);
                break;
            case 502:
                negative = true;
                break;
            case 800:// vet[0 #800]
                String index = lexeme;
                boolean operation = states.contains(ADDING) || states.contains(SUBTRACTING);
                if (states.contains(ASSIGNING) && !operation) {   // we are right left of =
                    // x = vet[0]
                    addText("LDI " + index);
                    addText("STO $indr");
                } else {                            // we are left of =
                    // vet[0]...
                    indexWhereVectorWillReceiveAssigning = index;
                    varToVector = true;
                }
                vectors.add(getVarName(id));
                break;
        }
    }

    public String getVarName(String id) {
        Var var = simbolTable.getVar(id, scope);
        return VarCompiler.instance(var).getName();
    }

    private void consumesExpressionToAssemblyVectorAsLastOperand(String token) {
        do {
            String idOrValue = idsOrValues.pollLast();
            String state = states.pollLast();
            switch (state) {
                case ASSIGNING:
                    command("LD", idOrValue);
                    addText("STO 1000");
                    break;
                case ADDING:
                    command("LD", indexWhereVectorWillReceiveAssigning);
                    addText("STO $indr");
                    addText("LDV " + getVarName(idOrValue));
                    addText("STO 1001");
                    addText("LD 1000");
                    addText("ADD 1001");
            }
        } while (!states.isEmpty());
        addText("STO " + getVarName(idThatWillReceiveAssigning));
    }

    private void storeExpression(String lexeme) {
        if (varToVector) {
            ldToAcc(lexeme);
            storeAccValueToStack(lexeme);
            loadVectorIndexFromStack();
            storeVectorValue();
            states.poll();
        } else {
            addText("STO " + getVarName(idThatWillReceiveAssigning));
        }
    }

    private void consumeExpressionToAssembly(String lexeme) {
        do {
            String state = states.pollLast();
            String idOrValue = idsOrValues.pollLast();
            switch (state) {
                case ASSIGNING:
                    boolean loadingVector = isLoadingVector(lexeme, idOrValue);
                    String ld = loadingVector ? "LDV" : "LD";
                    command(ld, idOrValue);
                    break;
                case ADDING:
                    command("ADD", idOrValue);
                    break;
                case SUBTRACTING:
                    command("SUB", idOrValue);
                    break;
            }
        } while (!states.isEmpty());
    }

    private boolean isLoadingVector(String lexeme, String id) {
        if (isInt(id)) return false;
        boolean tokenVetor = "]".equals(lexeme);
        if (tokenVetor) return true;
        String varName = getVarName(id);
        return vectors.contains(varName);
    }

    private void storeAccValueToStack(String lexeme) {
        addText("STO 1001");
    }

    private void loadVectorIndexFromStack() {
        addText("LD 1000");
        addText("STO $indr");
    }

    private void storeVectorValue() {
        addText("LD 1001");
        addText("STOV " + getVarName(idThatWillReceiveAssigning));
        varToVector = false;
    }

    private void storeVectorIndexToStackIfIsAssigningVector(String lexeme) {
        varToVector = "]".equals(lexeme);
        if (varToVector) {
            addText("LDI " + indexWhereVectorWillReceiveAssigning);
            addText("STO 1000");
        }
    }

    private void removesFromTheStackTheVarThatWillReceiveTheExpressionResult(String lexeme) {
        boolean vectorToVar = states.contains(ASSIGNING) && "]".equals(lexeme);
        if (varToVector || vectorToVar)
            idsOrValues.pollLast();
    }

    public void ldToAcc(String lexeme) {
        boolean assigningFromVector = "]".equals(lexeme);
        if (assigningFromVector) {
            addText("LDV " + getVarName(id));
            return;
        }
    }

    public String getVarNameOrInt(String lexeme){
        if(!isInt(lexeme))
            return getVarName(lexeme);
        return lexeme;
    }

    public String command(String command, String lexeme) {
        String text = createCommand(command, lexeme);
        return text;
    }

    public String createCommand(String command, String lexeme) {
        boolean inteiro = isInt(lexeme);
        command = inteiro ? command + "I" : command;
        if (!inteiro)
            lexeme = getVarName(lexeme);
        String text = command + " " + lexeme;
        addText(text);
        return text;
    }

    private boolean isInt(String lexeme) {
        return lexeme.matches("-?[0-9]+");
    }

    public SimbolTable getSimbolTable() {
        return simbolTable;
    }

    public Assembly getAssemblyPart() {
        return assemblyPart;
    }

    public void addText(String s){
        getAssemblyPart().addLine(s);
    }

    public Assembly build(){
        return getAssemblyPart();
    }

    public void addListener(AssemblerListener assembler){
        this.listeners.add(assembler);
    }

    protected void notifyFinalized(GeneralAssembler assembler) {
        this.listeners.forEach(l->l.finalizedAssembler(assembler));
    }

    public void addChild(GeneralAssembler child) {
        this.childrens.add(child);
        child.addListener( childFinalizedListener );
    }

}
