package ide.impl.compiler;

import gals.SemanticError;
import gals.Semantico;
import gals.Token;

public class SemanticoPortugol extends Semantico {

	private final SimbolTable table;
	private String scope = "";
	private String type = "";
	private String id = "";

	public SemanticoPortugol(SimbolTable simbolTable) {
		table = simbolTable;
	}

	@Override
	public void executeAction(int action, Token token) throws SemanticError {
		try {
			tryExecute(action, token);
		} catch (Exception e) {
			handleException(e,action,token);
		}
	}

	private void tryExecute(int action, Token token) throws SemanticError {
		switch (action) {
		case 0:
			scope = token.getLexeme();
			break;
		case 1:
			type = token.getLexeme();
			break;
		case 2:
			id = token.getLexeme();
			if(type!=""){
				table.addVar(Var.instance(scope, type, id));
			} else {
				System.out.println("USAR VARIAVEL");
			}
			break;
		case 3:
			type = "";
			break;
		case 41:
			id = token.getLexeme();
			break;
		case 42:
			onAtribuitionInitializeVarAndClearType();
			break;
		case 43:
			table.validateVarUse(token);
		default:
			break;
		}
	}

	private void onAtribuitionInitializeVarAndClearType() {
		table.initialize(id);
		type = "";
	}
	
	private void handleException(Exception e, int action, Token token) throws SemanticError {
		throw new SemanticError(e.getMessage(), token.getPosition());
	}
}
