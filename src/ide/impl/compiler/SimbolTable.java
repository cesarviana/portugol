package ide.impl.compiler;

import java.util.HashMap;
import java.util.Map;

import ide.impl.compiler.registryControl.Registry;
import ide.impl.compiler.registryControl.VarRegistry;

public class SimbolTable {

	private final Map<String, Function> functions;
	private final Map<String, Scope> scopes;
	private final Map<Registry, Registry> registries;

	public static SimbolTable instance() {
		return new SimbolTable();
	}

	private SimbolTable() {
		functions = new HashMap<>();
		registries = new HashMap<>();
		scopes = new HashMap<>();
	}

	public void addVar(Var var) {
		System.out.println("SimbolTable.addVar() = " + var);
		validadeNameOfAFunction(var);
		addVarToScope(var);
	}

	private void validadeNameOfAFunction(Var var) {
		if (functions.values().stream().anyMatch(f -> f.getId().equals(var.toString())))
			throw new CompilerException("Já há uma função com o nome " + var);		
		putRegistry(new VarRegistry(var));
	}
	
	private void addVarToScope(Var var) {
		getScope(var.getScopeStr()).addVar(var);
	}
	
	public void addParam(Var param) {
		Scope function = getScope(param.getScope());
		param.setParam(true);
		function.addVar(param);
	}

	private void putRegistry(Registry registry) {
		this.registries.put(registry, registry);
	}

	public void addFunction(Function function) {
		validadeSameName(function);
		validateNameOfAGlobalVar(function);
		this.functions.put(function.getId(),function);
		addScope(function);
	}
	
	public void addScope(Scope scope) {
		scopes.put(scope.getId(), scope);
	}

	private void validateNameOfAGlobalVar(Function function) {
		if (getScope("programa").getVars().values().stream()
				.anyMatch(v -> v.toString().equals(function.getId())))
			throw new CompilerException("Há uma variável chamada " + function);
	}

	private void validadeSameName(Function function) {
		if (functions.containsValue(function))
			throw new CompilerException(
					"Já existe uma função chamada " + function);
	}

	public Var getVar(String id, String scope) {
		return getScope(scope).getVar(id);
	}
	
	public Scope getScope(String id){
		return scopes.getOrDefault(id, Scope.NULL);
	}

	public void initialize(String id, String scope) {
		Var var = getVar(id, scope);
		if(var==Var.NULL)
			throw new CompilerException("Variável \""+ id + "\" não declarada. Impossível inicializar.");
		var.initialize();
	}
	
	public void setUsed(String id, String scope) {
		Var var = getVar(id, scope);
		if(var==Var.NULL)
			throw new CompilerException("Variável \""+ id + "\" não declarada. Impossível usar.");
		var.use();
	}

	public void validateVarUse(String id, String scope){
		if(matchVarRegexAndHasntVar(id, scope)){
			throw new CompilerException("A variável \""+ id + "\" não foi declarada.");
		}
	}
	
	private boolean matchVarRegexAndHasntVar(String id, String scope) {
		boolean matchIdRegex = id.matches("[a-zA-Z][a-zA-Z0-9_]*");
		return matchIdRegex && getVar(id, scope).equals(Var.NULL);
	}

	public void validadeAtribuition(String id, String scope) {
		Var var = getVar(id, scope);
		if( var.isConstant() && var.getValue() != null){
			throw new CompilerException("Não é permitida atribuição à constante " + var);
		}
	}

	public void setValue(String id, String scope, String value) {
		getVar(id, scope).setValue(value);
	}


	public Registry getRegistry(Registry registry) {
		return registries.get(registry);
	}

}
