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
	public static final String PROGRAMA = "programa";
	public static final String READING = "reading";
	public static final String READING_VECTOR = "reading_vector";

	private static final String ASSIGNING = "assigning";

	public static final String WRITING = "writing";
	public static final String WRITING_VECTOR = "writing_vector";

	private SimbolTable simbolTable;
	private Assembly assembly;
	private String code = "";
	private String state = "";
	private int vectorPosition = 0;
	private String id;
	private String idThatWillReceiveAssigning;
	private String scope;

	public AssemblerImpl() {
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
			state = READING;
			break;
		case 301:
			if (state == READING) {
				assembly.addText("LD $in_port");
				assembly.addText("STO " + token.getLexeme());
			} else if (state == READING_VECTOR) {
				assembly.addText("LDI " + vectorPosition);
				assembly.addText("STO $indr");
				assembly.addText("LD $in_port");
				assembly.addText("STOV " + id);
			}
			state = "";
			id = "";
			vectorPosition = 0;
			break;
		case 302:
			if (state == WRITING) {
				ldToAcc(token);
				assembly.addText("STO $out_port");
			} else if (state == WRITING_VECTOR) {
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
			if (state == READING) {
				state = READING_VECTOR;
			} else if (state == WRITING) {
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
		case 600:
			idThatWillReceiveAssigning = id;
			state = ASSIGNING;
			break;
		case 41:
			ldToAcc(token);
			assembly.addText("STO " + getVarName(idThatWillReceiveAssigning));
			state = "";
			break;
		}
	}

	private String getVarName(String id) {
		return VarCompiler.instance(simbolTable.getScope(scope).getVar(id)).getName();
	}

	private void ldToAcc(Token token) {
		String lexeme = token.getLexeme();
		boolean inteiro = lexeme.matches("[0-9]*");
		String command = inteiro ? "LDI" : "LD";
		if(!inteiro) lexeme = getVarName(lexeme);
		assembly.addText(command + " " + lexeme);
	}
}
