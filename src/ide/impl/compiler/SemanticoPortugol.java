package ide.impl.compiler;

import gals.SemanticError;
import gals.Semantico;
import gals.Token;

public class SemanticoPortugol extends Semantico {

	private final SimbolTable table;
	private String scope = "";
	private String type = "";
	private String id = "";
	private boolean constant = false;
	private String varValue = "";
	private SemanticState previousState;
	private SemanticState state;
	private boolean atribuindo = false;
	
	public SemanticoPortugol(SimbolTable simbolTable) {
		table = simbolTable;
	}

	@Override
	public void executeAction(int action, Token token) throws SemanticError {
		try {
			tryExecute(action, token);
		} catch (Exception e) {
			handleException(e, action, token);
		}	
	}

	private void tryExecute(int action, Token token) throws SemanticError {
		switch (action) {
		case 100:
			previousState = state;
			state = SemanticState.DECLARING_VAR;
			break;
		case 101:
			previousState = state;
			state = SemanticState.DECLARING_FUNCTION;
			break;
		case 200:
			atribuindo = true;
			break;
		case 0:
			scope = token.getLexeme();
			break;
		case 9:
			String controlStructure = token.getLexeme();
			onOpenControlStructureChangeScopeAndClearType(controlStructure);
			break;
		case 92:
			String caso = token.getLexeme();
			Scope scopeSwitch = table.getScope(scope).getParent();
			onCaseClosePreviousCaseScopeAndOpenNew(caso, scopeSwitch);
			break;
		case 10://close scope
			closeCurrentScope();
			break;
		case 11: // Enter function on "{"
			state = previousState; // End declare function state
			type=""; // Clear type on enter function
			break;
		case 1:
			type = token.getLexeme();
			break;
		case 23: // add variable
			if(state==SemanticState.DECLARING_VAR){
				table.addVar(Var.instance(scope, type, id, constant));
			}
			break;
		case 21: 
			if(state==SemanticState.DECLARING_VAR){
				if(type!=""){
					table.addVar(Var.instance(scope, type, id, constant));
				} 
				table.initialize(id, scope);
				table.validateVarUse(id, scope);
				table.setValue(id, scope, varValue);
				varValue="";
				atribuindo=false;
			}
			break;
		case 2: // Get id. Store to declare variable if not attributing
			if(!atribuindo) 
				id = token.getLexeme();
			break;
		case 22:
			table.addParam(Var.instance(scope, type, id, constant));
			break;		
		case 3:
			type = "";
			break;
		case 4:
			constant = true;
			break;
		case 41:
			varValue = table.getVarValueIfIsVar( token.getLexeme(), scope );
			break;
		case 5:
			String functionId = token.getLexeme();
			table.addFunction(Function.instance(type, functionId));
			break;
		case 51:
			table.addScope(Scope.instance(token.getLexeme()));
			break;
		case 7:
			id = token.getLexeme();
			Var param = Var.instance(scope, type, id);
			table.addParam(param);
			break;
		case 6:
			String idVarToUse = token.getLexeme();
			table.setVarUsed(idVarToUse, scope);
			break;
		case 62:
			String idFunctionToUse = token.getLexeme();
			table.setFunctionUsed(idFunctionToUse, scope);
			break;
		case 8:
			table.convertToVector(id,scope);
			break;
		default:
			break;
		}
	}

	private void onCaseClosePreviousCaseScopeAndOpenNew(String caso, Scope switchScope) {
		boolean isNotFirstCase = switchScope.getChilds().values().stream().anyMatch((scope)->scope.getId().endsWith("caso0"));
		if(isNotFirstCase)
			closeCurrentScope();
		onOpenControlStructureChangeScopeAndClearType(caso);
	}

	private String closeCurrentScope() {
		return scope = table.getScope(scope).getParent().getId();
	}

	private void onOpenControlStructureChangeScopeAndClearType(String controlStructureScopeName) {
		Scope parentScope = table.getScope(scope);
		Scope controlStructureScope = parentScope.addChild(controlStructureScopeName);
		scope = controlStructureScope.getId();
		table.addScope(controlStructureScope);
		type = "";
	}


	private void handleException(Exception e, int action, Token token)
			throws SemanticError {
		e.printStackTrace();
		throw new SemanticError(e.getMessage(), token.getPosition());
	}
	
	private enum SemanticState {
		DECLARING_VAR {
			@Override
			void tryExecute(int action, Token token) {
				
			}
		}, DECLARING_FUNCTION {
			@Override
			void tryExecute(int action, Token token) {	
			}
		}, GENERAL {
			@Override
			void tryExecute(int action, Token token) {	
			}
			
		};
		
		abstract void tryExecute(int action, Token token);
	}	

}
