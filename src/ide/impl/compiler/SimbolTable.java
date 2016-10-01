package ide.impl.compiler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ide.impl.compiler.registryControl.Registry;
import ide.impl.compiler.registryControl.VarRegistry;

public class SimbolTable {

	//private static SimbolTable instance;

	private Map<Var,Var> vars;
	private Set<Function> functions;
	private Map<Registry, Registry> registries;

	public static SimbolTable instance() {
//		if (instance == null)
//			instance = new SimbolTable();
//		return instance;
		return new SimbolTable();
	}

	private SimbolTable() {
		vars = new HashMap<>();
		functions = new HashSet<>();
		registries = new HashMap<>();
	}

	public void addVar(Var var) {
		System.out.println("SimbolTable.addVar() = " + var);
		if (vars.containsValue(var))
			throw new CompilerException("A variável " + var
					+ " já foi declarada no escopo " + var.getScope());
		if(functions.stream().anyMatch(f->f.getId().equals(var.toString())))
			throw new CompilerException("Já há uma função com o nome " + var);
		this.vars.put(var, var);		
		putRegistry(new VarRegistry(var));
	}

	private void putRegistry(Registry registry) {
		this.registries.put(registry, registry);
	}

	public void addFunction(Function function) {
		if (functions.contains(function))
			throw new CompilerException(
					"Já existe uma função chamada " + function);
		if (vars.values().stream().anyMatch(v -> v.toString().equals(function.getId())))
			throw new CompilerException("Há uma variável chamada " + function);
		this.functions.add(function);
	}

	public Var getVar(String id, String scope) {
		Var key = Var.instance(scope, null, id);
		return vars.getOrDefault(key, Var.NULL);
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

	public void validadeNewFunction(String scope) {
		
	}

	public Registry getRegistry(Registry similar) {
		return registries.get(similar);
	}
}
