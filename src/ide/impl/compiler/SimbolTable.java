package ide.impl.compiler;

import java.util.HashMap;
import java.util.Map;

import ide.impl.compiler.registryControl.Registry;
import ide.impl.compiler.registryControl.VarRegistry;

public class SimbolTable {

	private final Map<String,Var> vars;
	private final Map<String, Function> functions;
	private final Map<String, Scope> scopes;
	private final Map<Registry, Registry> registries;

	public static SimbolTable instance() {
		return new SimbolTable();
	}

	private SimbolTable() {
		vars = new HashMap<>();
		functions = new HashMap<>();
		registries = new HashMap<>();
		scopes = new HashMap<>();
	}

	public void addVar(Var var) {
		System.out.println("SimbolTable.addVar() = " + var);
		validadeNameOfAFunction(var);
		addVarToListAndToScope(var);
	}

	private void addVarToListAndToScope(Var var) {
		getScope(var.getScope()).addVar(var);
		this.vars.put(var.getId(), var);
	}
	
	public void addParam(Var param) {
		Scope function = getScope(param.getScope());
		param.setParam(true);
		function.addVar(param);
	}

	
	private void validadeNameOfAFunction(Var var) {
		if (functions.values().stream().anyMatch(f -> f.getId().equals(var.toString())))
			throw new CompilerException("Já há uma função com o nome " + var);
		this.vars.put(var.getId(), var);		
		putRegistry(new VarRegistry(var));
	}

	private void putRegistry(Registry registry) {
		this.registries.put(registry, registry);
	}

	public void addFunction(Function function) {
		validadeSameName(function);
		validateNameOfAVar(function);
		this.functions.put(function.getId(),function);
		this.scopes.put(function.getId(), function);
	}

	private void validateNameOfAVar(Function function) {
		if (vars.values().stream()
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
			throw new CompilerException("Variável \""+ id + "\" não declarada");
		var.initialize();
	}
	
	public void setUsed(String id, String scope) {
		Var var = getVar(id, scope);
		if(var==Var.NULL)
			throw new CompilerException("Variável \""+ id + "\" não declarada");
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


	public Registry getRegistry(Registry similar) {
		return registries.get(similar);
	}
}
