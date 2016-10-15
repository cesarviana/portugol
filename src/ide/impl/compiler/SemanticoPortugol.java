package ide.impl.compiler;

import gals.SemanticError;
import gals.Semantico;
import gals.Token;

public class SemanticoPortugol extends Semantico {

	private final SimbolTable table;
	private String scope = "";
	private String type = "";
	private String id = "";
	private String idAuxToUseVarVector = "";
	private boolean constant = false;
	private String varValue = "";
	private SemanticState previousState;
	private SemanticState state = SemanticState.DECLARING_VAR;
	private boolean atribuindo = false;
	private boolean vector = false;

	public SemanticoPortugol(SimbolTable simbolTable) {
		table = simbolTable;
		table.clear();
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
			changeStateTo(SemanticState.DECLARING_VAR);
			break;
		case 101:
			changeStateTo(SemanticState.DECLARING_FUNCTION);
			break;
		case 200:
			atribuindo = true;
			if(state != SemanticState.DECLARING_VAR)
				setVarUsed(id);
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
		case 10:// close scope
			closeCurrentScope();
			break;
		case 11: // Enter function on "{"
			state = previousState; // End declare function state
			type = ""; // Clear type on enter function
			break;
		case 13:
			vector = true;
			break;
		case 1:
			type = token.getLexeme();
			break;
		case 23:
			addVarIfIsDeclaringVar();
			state = null;
			break;
		case 21:
			addInitializedVarIfIsDeclaringVar();
			state = null;
			break;
		case 2:
			idAuxToUseVarVector = token.getLexeme();
			if (!atribuindo)
				id = token.getLexeme();
			break;
		case 22:
			table.addParam(createVar());
			break;
		case 3:
			type = "";
			break;
		case 4:
			constant = true;
			break;
		case 41:
			varValue = table.getVarValueIfIsVar(token.getLexeme(), scope);
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
			setVarUsed(idVarToUse);
			break;
		case 99:
			String idFunctionToUse = token.getLexeme();
			table.setFunctionUsed(idFunctionToUse, scope);
			break;
		case 8:
			table.convertToVector(id, scope);
			break;
		default:
			break;
		}
	}

	private void setVarUsed(String idVarToUse) {
		if(! table.matchVarRegex(idVarToUse)){
			idVarToUse = idAuxToUseVarVector;
			idAuxToUseVarVector = "";
		}
		table.setVarUsed(idVarToUse, scope);
	}

	private void addVarIfIsDeclaringVar() {
		if (state == SemanticState.DECLARING_VAR) {
			addVarToSimbolTable();
		}
	}

	private void addInitializedVarIfIsDeclaringVar() {
		if (state == SemanticState.DECLARING_VAR) {
			if (type != "") {
				addVarToSimbolTable();
			}
			table.initialize(id, scope);
			table.validateVarUse(id, scope);
			table.setValue(id, scope, varValue);
			varValue = "";
			atribuindo = false;
		}
	}

	private void changeStateTo(SemanticState newState) {
		previousState = state;
		state = newState;
		atribuindo = false;
	}

	private void addVarToSimbolTable() {
		table.addVar(createVar());
	}

	private Var createVar() {
		Var var = Var.instance(scope, type, id, constant);
		if (vector) {
			var = VarVector.instance(var);
		}
		return var;
	}

	private void onCaseClosePreviousCaseScopeAndOpenNew(String caso, Scope switchScope) {
		boolean isNotFirstCase = switchScope.getChilds().values().stream()
				.anyMatch((scope) -> scope.getId().endsWith("caso0"));
		if (isNotFirstCase)
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

	private void handleException(Exception e, int action, Token token) throws SemanticError {
		e.printStackTrace();
		throw new SemanticError(e.getMessage(), token.getPosition());
	}

	private enum SemanticState {
		DECLARING_VAR, DECLARING_FUNCTION
	}

}
