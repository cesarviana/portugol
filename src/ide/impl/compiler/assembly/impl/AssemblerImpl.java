package ide.impl.compiler.assembly.impl;

import gals.*;
import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import ide.impl.compiler.assembly.Assembler;
import ide.impl.compiler.assembly.Assembly;
import lombok.Data;

import java.util.LinkedList;

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

    private SimbolTable simbolTable;
	private Assembly assembly;
	private String code = "";
	private int vectorPosition = 0;
	private String id;
	private String idThatWillReceiveAssigning;
	private String scope;
    private final LinkedList<String> states;
    private final LinkedList<String> idsOrValues;
	public AssemblerImpl() {
		assembly = new Assembly();
        states = new LinkedList<>();
        idsOrValues = new LinkedList<>();
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
		switch (action) {
            case 0:
                scope = token.getLexeme();
                if (PROGRAMA.equals(scope)) {
                    assembly.addText("_PRINCIPAL:");
                }
                break;
            case 300:
                states.add(READING);
                break;
            case 301:
                if (states.isEmpty())
                    return;
                if (states.peek() == READING) {
                    assembly.addText("LD $in_port");
                    assembly.addText("STO " + token.getLexeme());
                } else if (states.peek() == READING_VECTOR) {
                    assembly.addText("LDI " + vectorPosition);
                    assembly.addText("STO $indr");
                    assembly.addText("LD $in_port");
                    assembly.addText("STOV " + id);
                }
                states.pop();
                id = "";
                vectorPosition = 0;
                break;
            case 302:
                if (states.peek() == WRITING) {
                    command("LD",token.getLexeme());
                    assembly.addText("STO $out_port");
                } else if (states.peek() == WRITING_VECTOR) {
                    assembly.addText("LDI " + vectorPosition);
                    assembly.addText("STO $indr");
                    assembly.addText("LDV " + id);
                    assembly.addText("STO $out_port");
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
                idsOrValues.push(id);
                break;
            case 400:
                states.push(WRITING);
                break;
            case 600:
                idThatWillReceiveAssigning = id;
                states.push(ASSIGNING);
                break;
            case 601:
                idsOrValues.push(token.getLexeme());
                break;
            case 41:
                if (states.isEmpty())
                    return;
                // finaliza expressão
                removesFromTheStackTheVarThatWillReceiveTheExpressionResultDoYouWantAShortNameOfCourse();
                do {
                    String state = states.pollLast();
                    String idOrValue = isInt(token.getLexeme()) ? token.getLexeme() : idsOrValues.pollLast();
                    if (state == ASSIGNING) {
                        command("LD",idOrValue);
                    } else
                    if (state == ADDING) {
                        command("ADD",idOrValue);
                    } else
                    if (state == SUBTRACTING) {
                        command("SUB",idOrValue);
                    }
                } while (!states.isEmpty());
                assembly.addText("STO " + getVarName(idThatWillReceiveAssigning));
                idsOrValues.clear();
                break;
            case 500:
                states.push(ADDING);
                break;
            case 501:
                states.push(SUBTRACTING);
                break;
        }
	}

    private void removesFromTheStackTheVarThatWillReceiveTheExpressionResultDoYouWantAShortNameOfCourse() {
        idsOrValues.pollLast();
    }

    private String getVarName(String id) {
		return VarCompiler.instance(simbolTable.getScope(scope).getVar(id)).getName();
	}

	private void command(String command,String lexeme) {
		boolean inteiro = isInt(lexeme);
		command = inteiro ? command+"I" : command;
		if(!inteiro)
		    lexeme = getVarName(lexeme);
		assembly.addText(command + " " + lexeme);
	}

    private boolean isInt(String lexeme) {
        return lexeme.matches("[0-9]*");
    }

}
