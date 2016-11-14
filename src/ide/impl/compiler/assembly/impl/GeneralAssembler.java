package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;

import java.util.*;

public abstract class GeneralAssembler {

    public static final String READING = "reading";
    public static final String READING_VECTOR = "reading_vector";
    public static final String ADDING = "adding";

    private static final String ASSIGNING = "assigning";

    public static final String WRITING = "writing";
    public static final String WRITING_VECTOR = "writing_vector";
    public static final String SUBTRACTING = "subtracting";
    public static final String DECLARING = "declaring";

    private static SimbolTable simbolTable;
    private int vectorPosition = 0;
    private String id;
    private String idThatWillReceiveAssigning;
    protected static String scope;
    private String assemblyScope;
    protected static Stack<Scope> scopes = new Stack<>();
    private final LinkedList<String> states;
    protected final LinkedList<String> idsOrValues;
    private String indexWhereVectorWillReceiveAssigning;
    private boolean varToVector;
    private final Set<String> vectors;
    private boolean negative = false;

    private Assembly assemblyPart;
    private final List<GeneralAssembler> childrens;
    private final List<AssemblerListener> listeners;

    protected boolean finalized;

    private static final AssemblerListener scopesRemoverFromStack = new AssemblerListener() {
        @Override
        public void finalizedAssembler(GeneralAssembler assembler) {
            scopes.pop();
        }
    };

    private final AssemblerListener childFinalizedListener = new AssemblerListener() {
        @Override
        public void finalizedAssembler(GeneralAssembler assembler) {
            boolean childFinalized = childrens.contains(assembler);
            if(childFinalized) {
                addChildAssembly(assembler);
                childrens.remove(assembler);
            }
        }

        private void addChildAssembly(GeneralAssembler childAssembler) {
            Assembly thisPart = getAssemblyPart();
            Assembly childPart = childAssembler.build();
            thisPart.addAssembly( childPart );
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
        addListener( scopesRemoverFromStack );
    }

    public void executeAction(int action, String lexeme){
        switch (action) {
            case 8:
                scope = lexeme;
                if(!scopes.empty()){
                    Scope newChild = getCurrentScope().addChild( lexeme );
                    scopes.push( newChild );
                } else {
                    scopes.push(Scope.instance(lexeme));
                }
                this.assemblyScope = getCurrentScope().getId();
                break;
            case 7:
                notifyFinalizedAction(action,lexeme);
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
                    addLine("LD $in_port");
                    addLine("STO " + getVarName(lexeme));
                } else if (states.peek() == READING_VECTOR) {
                    addLine("LDI " + vectorPosition);
                    addLine("STO $indr");
                    addLine("LD $in_port");
                    addLine("STOV " + getVarName(id));
                }
                states.pop();
                id = "";
                vectorPosition = 0;
                break;
            case 302:
                if (states.peek() == WRITING) {
                    command("LD", lexeme);
                    addLine("STO $out_port");
                } else if (states.peek() == WRITING_VECTOR) {
                    addLine("LDI " + vectorPosition);
                    addLine("STO $indr");
                    addLine("LDV " + getVarName(id));
                    addLine("STO $out_port");
                }
                states.pollLast();
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
                    addLine("LDI " + index);
                    addLine("STO $indr");
                } else {                            // we are left of =
                    // vet[0]...
                    indexWhereVectorWillReceiveAssigning = index;
                    varToVector = true;
                }
                vectors.add(getVarName(id));
                break;
        }
    }

    protected void notifyFinalizedAction(int action, String lexeme) {
        finalizeAndNotify(this);
    }

    public static String getVarName(String id) {
        String currentScopeId = getCurrentScope().getId();
        Var var = simbolTable.getVar(id, currentScopeId);
        return VarCompiler.instance(var).getName();
    }

    public static Scope getCurrentScope() {
        if(scopes.isEmpty())
            return Scope.NULL;
        return scopes.peek();
    }

    private void consumesExpressionToAssemblyVectorAsLastOperand(String token) {
        do {
            String idOrValue = idsOrValues.pollLast();
            String state = states.pollLast();
            switch (state) {
                case ASSIGNING:
                    command("LD", idOrValue);
                    addLine("STO 1000");
                    break;
                case ADDING:
                    command("LD", indexWhereVectorWillReceiveAssigning);
                    addLine("STO $indr");
                    addLine("LDV " + getVarName(idOrValue));
                    addLine("STO 1001");
                    addLine("LD 1000");
                    addLine("ADD 1001");
            }
        } while (!states.isEmpty());
        addLine("STO " + getVarName(idThatWillReceiveAssigning));
    }

    private void storeExpression(String lexeme) {
        if (varToVector) {
            ldToAcc(lexeme);
            storeAccValueToStack(lexeme);
            loadVectorIndexFromStack();
            storeVectorValue();
            states.poll();
        } else {
            addLine("STO " + getVarName(idThatWillReceiveAssigning));
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
        addLine("STO 1001");
    }

    private void loadVectorIndexFromStack() {
        addLine("LD 1000");
        addLine("STO $indr");
    }

    private void storeVectorValue() {
        addLine("LD 1001");
        addLine("STOV " + getVarName(idThatWillReceiveAssigning));
        varToVector = false;
    }

    private void storeVectorIndexToStackIfIsAssigningVector(String lexeme) {
        varToVector = "]".equals(lexeme);
        if (varToVector) {
            addLine("LDI " + indexWhereVectorWillReceiveAssigning);
            addLine("STO 1000");
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
            addLine("LDV " + getVarName(id));
            return;
        }
    }

    public String getVarNameOrInt(String lexeme){
        if(!isInt(lexeme))
            return getVarName(lexeme);
        return lexeme;
    }

    public void command(String command, String lexeme) {
        if(!isInt(lexeme))
            lexeme = getVarName(lexeme);
        String text = createCommand(command, lexeme);
        addLine(text);
    }

    public static String createCommand(String command, String lexeme) {
        command = isInt(lexeme) ? command + "I" : command;
        return command + " " + lexeme;
    }

    private static boolean isInt(String lexeme) {
        return lexeme.matches("-?[0-9]+");
    }

    public SimbolTable getSimbolTable() {
        return simbolTable;
    }

    public Assembly getAssemblyPart() {
        return assemblyPart;
    }

    public void addLine(String s){
        getAssemblyPart().addLine(s);
    }

    public Assembly build(){
        getAssemblyPart().setAssemblyScope( this.assemblyScope );
        getAssemblyPart().build();
        return getAssemblyPart();
    }

    public void addListener(AssemblerListener assembler){
        this.listeners.add(assembler);
    }

    protected void finalizeAndNotify(GeneralAssembler assembler) {
        if(!this.finalized){
            this.finalized = true;
            this.listeners.forEach(l->l.finalizedAssembler(assembler));
        }
    }

    public void addChild(GeneralAssembler child) {
        this.childrens.add(child);
        child.addListener( childFinalizedListener );
    }

    @Override
    public String toString() {
        return this.assemblyScope;
    }
}
