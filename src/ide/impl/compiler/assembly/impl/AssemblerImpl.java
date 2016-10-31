package ide.impl.compiler.assembly.impl;

import gals.*;
import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import ide.impl.compiler.assembly.Assembler;
import ide.impl.compiler.assembly.Assembly;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import ide.impl.compiler.assembly.ControlStrucuture;
import lombok.Data;

@Data
public class AssemblerImpl extends Semantico implements Assembler {

    public static final String PROGRAMA = "programa";
    public static final String READING = "reading";
    public static final String READING_VECTOR = "reading_vector";
    public static final String ADDING = "adding";

    private static final String ASSIGNING = "assigning";

    public static final String WRITING = "writing";
    public static final String WRITING_VECTOR = "writing_vector";
    public static final String SUBTRACTING = "subtracting";
    public static final String DECLARING = "declaring";
    public static final String VAR_NAME_SCOPE_SEPARATOR = "_";

    private SimbolTable simbolTable;
    private Assembly assembly;
    private String code = "";
    private int vectorPosition = 0;
    private String id;
    private String idThatWillReceiveAssigning;
    private String scope;
    private final LinkedList<String> states;
    private final LinkedList<String> idsOrValues;
    private String indexWhereVectorWillReceiveAssigning;
    private boolean varToVector;
    private final Set<String> vectors;
    private boolean negative = false;
    private Stack<ControlStrucuture> controlStrucutures;

    public AssemblerImpl() {
        assembly = new Assembly();
        states = new LinkedList<>();
        idsOrValues = new LinkedList<>();
        vectors = new HashSet<>();
        controlStrucutures = new Stack<>();
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
        for (Scope scope : simbolTable.getScopes().values()) {
            for (Var var : scope.getVars().values()) {
                VarCompiler varCompiler = VarCompiler.instance(var);
                assembly.addData(varCompiler.getDataDeclaration());
            }
        }
    }

    private void addText() {
        if (!code.isEmpty()) {
            addCode();
        }
        addText("HLT 0");
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
                scope = token.getLexeme();
                if (PROGRAMA.equals(scope)) {
                    addText("_PRINCIPAL:");
                }
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
                    addText("STO " + getVarName(token.getLexeme()));
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
                    command("LD", token.getLexeme());
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
                vectorPosition = Integer.parseInt(token.getLexeme());
                break;
            case 2:
                id = token.getLexeme();
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
                storeVectorIndexToStackIfIsAssigningVector(token);
                break;
            case 601:
                String signal = negative ? "-" : "";
                idsOrValues.push(signal + token.getLexeme());
                negative = false;
                break;
            case 41:
                if (states.isEmpty())
                    return;
                boolean isOperation = states.contains(SUBTRACTING) || states.contains(ADDING);
                boolean vectorIsTheLast = "]".equals(token.getLexeme());
                if (vectorIsTheLast && isOperation) {
                    consumesExpressionToAssemblyVectorAsLastOperand(token);
                } else {
                    removesFromTheStackTheVarThatWillReceiveTheExpressionResult(token);
                    consumeExpressionToAssembly(token);
                    storeExpression(token);
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
                String index = token.getLexeme();
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
            case 910:
                scope = Scope.instance(scope).addChild(token.getLexeme()).getId();
                areOpeningSeScope(scope);
                break;
            case 912:
                possibleGotRelationalOperand(token);
                break;
            case 913:
                areRightAfterExpInsideSe();
                break;
            case 914:
                areClosingSeScope();
                scope = simbolTable.getScope(scope).getParent().getId();
                break;
            case 915:
                setRelationalOperatorOnRelationalExpression(token.getLexeme());
                break;
            case 916:
                convertSeToSenao();
                break;
        }
    }

    private void areOpeningSeScope(String scopeName) {
        ControlStrucuture relExp = new Se(scopeName);
        relExp.startWatching();
        controlStrucutures.push(relExp);
    }

    private void possibleGotRelationalOperand(Token token) {
        if (controlStrucutures.empty()) return;
        String operand = token.getLexeme();
        ControlStrucuture currentRelExp = controlStrucutures.peek();
        currentRelExp.addOperand(operand);
    }

    private void areClosingSeScope() {
        ControlStrucuture currentStructure = controlStrucutures.peek();
        addText(currentStructure.useBranch() + ":");
        controlStrucutures.pop();
    }

    private void areRightAfterExpInsideSe() {
        ControlStrucuture currentRelExp = controlStrucutures.peek();
        currentRelExp.build(this);
        currentRelExp.stopWatching();
    }

    private void setRelationalOperatorOnRelationalExpression(String operator) {
        ControlStrucuture currentRelExp = controlStrucutures.peek();
        currentRelExp.setOperator(operator);
    }

    private void convertSeToSenao(){
        ControlStrucuture se = controlStrucutures.peek();
        if(se instanceof Se)
            ((Se)se).convertToSenao();
    }

    private void consumesExpressionToAssemblyVectorAsLastOperand(Token token) {
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

    private void storeExpression(Token token) {
        if (varToVector) {
            String lexeme = token.getLexeme();
            ldToAcc(lexeme);
            storeAccValueToStack(lexeme);
            loadVectorIndexFromStack();
            storeVectorValue();
            states.poll();
        } else {
            addText("STO " + getVarName(idThatWillReceiveAssigning));
        }
    }

    private void consumeExpressionToAssembly(Token token) {
        do {
            String state = states.pollLast();
            String idOrValue = idsOrValues.pollLast();
            switch (state) {
                case ASSIGNING:
                    boolean loadingVector = isLoadingVector(token, idOrValue);
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

    private boolean isLoadingVector(Token token, String id) {
        if (isInt(id)) return false;
        String lexeme = token.getLexeme();
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

    private void storeVectorIndexToStackIfIsAssigningVector(Token token) {
        varToVector = "]".equals(token.getLexeme());
        if (varToVector) {
            addText("LDI " + indexWhereVectorWillReceiveAssigning);
            addText("STO 1000");
        }
    }

    private void removesFromTheStackTheVarThatWillReceiveTheExpressionResult(Token token) {
        boolean vectorToVar = states.contains(ASSIGNING) && "]".equals(token.getLexeme());
        if (varToVector || vectorToVar)
            idsOrValues.pollLast();
    }

    public String getVarName(String id) {
        Var var = simbolTable.getVar(id, scope);
        return VarCompiler.instance(var).getName();
    }

    public void ldToAcc(String lexeme) {
        boolean assigningFromVector = "]".equals(lexeme);
        if (assigningFromVector) {
            addText("LDV " + getVarName(id));
            return;
        }
    }

    @Override
    public void command(String command, String lexeme) {
        boolean inteiro = isInt(lexeme);
        command = inteiro ? command + "I" : command;
        if (!inteiro)
            lexeme = getVarName(lexeme);
        addText(command + " " + lexeme);
    }

    private boolean isInt(String lexeme) {
        return lexeme.matches("-?[0-9]+");
    }

    @Override
    public void addText(String s) {
        assembly.addText(s);
    }

    @Override
    public String popIdOrValue() {
        return idsOrValues.pop();
    }

}
