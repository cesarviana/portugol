package ide.impl.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ide.impl.compiler.registryControl.Registry;
import ide.impl.compiler.registryControl.FuncitonRegistry;
import ide.impl.compiler.registryControl.VarRegistry;

public class SimbolTable {

	private final Map<String, Function> functions;
	private final Map<String, Scope> scopes;
	private final List<Registry> registries;

	public static SimbolTable instance() {
		return new SimbolTable();
	}

	private SimbolTable() {
		functions = new HashMap<>();
		registries = new ArrayList<>();
		scopes = new HashMap<>();
	}

	public void addVar(Var var) {
		System.out.println("SimbolTable.addVar() = " + var);
		mustNotExistsInParentScopesInsideTheFunction(var);
		validadeNameOfAFunction(var);
		addVarToScope(var);
		addRegistry(VarRegistry.instance(var));
	}

	private void mustNotExistsInParentScopesInsideTheFunction(Var var) {
		Scope scopeToTest = getScope(var.getScopeStr());
		while (scopeToTest != Scope.NULL) {
			if (scopeToTest.getVars().containsKey(var.getId()))
				throw new CompilerException(
						"A variável \"" + var.getId() + "\" já existe no escopo " + scopeToTest.getId() + ".");
			scopeToTest = scopeToTest.getParent();
		}
	}

	private void validadeNameOfAFunction(Var var) {
		if (functions.values().stream().anyMatch(f -> f.getId().equals(var.toString())))
			throw new CompilerException("Já há uma função com o nome " + var);
	}

	private void addVarToScope(Var var) {
		getScope(var.getScopeStr()).addVar(var);
	}

	public void addParam(Var param) {
		Scope function = getScope(param.getScopeStr());
		param.setParam(true);
		function.addVar(param);
		addRegistry(VarRegistry.instance(param));
	}

	private void addRegistry(Registry registry) {
		this.registries.add(registry);
	}

	public void addFunction(Function function) {
		validadeSameName(function);
		validateNameOfAGlobalVar(function);
		this.functions.put(function.getId(), function);
		addScope(function);
		addRegistry(FuncitonRegistry.instance(function));
	}

	public void addScope(Scope scope) {
		scopes.put(scope.getId(), scope);
	}

	private void validateNameOfAGlobalVar(Function function) {
		if (getScope("programa").getVars().values().stream().anyMatch(v -> v.toString().equals(function.getId())))
			throw new CompilerException("Há uma variável chamada " + function);
	}

	private void validadeSameName(Function function) {
		if (functions.containsValue(function))
			throw new CompilerException("Já existe uma função chamada " + function);
	}

	public Var getVar(String id, String scope) {
		Scope scopeToSearch = getScope(scope);
		do {
			if (scopeToSearch.getVars().containsKey(id))
				return scopeToSearch.getVar(id);
			scopeToSearch = scopeToSearch.getParent();
		} while (scopeToSearch != Scope.NULL);
		return Var.NULL;
	}

	public Scope getScope(String id) {
		return scopes.getOrDefault(id, Scope.NULL);
	}

	public void initialize(String id, String scope) {
		Var var = getVar(id, scope);
		if (var == Var.NULL)
			throw new CompilerException("Variável \"" + id + "\" não declarada. Impossível inicializar.");
		var.initialize();
	}

	public void setVarUsed(String id, String scope) {
		Var var = getVar(id, scope);
		if (var == Var.NULL)
			throw new CompilerException("Variável \"" + id + "\" não declarada. Impossível usar.");
		var.use();
	}
	
	public void setFunctionUsed(String id, String scope) {
		Function function = (Function) getScope(id);
		if (function == Scope.NULL)
			throw new CompilerException("Função \"" + id + "\" não declarada. Impossível usar.");
		function.use();
	}

	public void validateVarUse(String id, String scope) {
		if (matchVarRegexAndHasntVar(id, scope)) {
			throw new CompilerException("A variável \"" + id + "\" não foi declarada.");
		}
	}

	private boolean matchVarRegexAndHasntVar(String id, String scope) {
		boolean matchIdRegex = id.matches("[a-zA-Z][a-zA-Z0-9_]*");
		return matchIdRegex && getVar(id, scope).equals(Var.NULL);
	}

	public void validateAtribuition(String id, String scope) {
		Var var = getVar(id, scope);
		if (var.isConstant() && var.getValue() != null) {
			throw new CompilerException("Não é permitida atribuição à constante " + var);
		}
	}

	public void setValue(String id, String scope, String value) {
		getVar(id, scope).setValue(value);
	}

	public Registry getRegistryByExample(Registry registry) {
		return registries.get(registries.indexOf(registry));
	}

}
