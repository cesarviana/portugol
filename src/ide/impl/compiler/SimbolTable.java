package ide.impl.compiler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gals.Token;

public class SimbolTable {

	//private static SimbolTable instance;

	private Map<String,Var> vars;
	private Set<Function> functions;

	public static SimbolTable instance() {
//		if (instance == null)
//			instance = new SimbolTable();
//		return instance;
		return new SimbolTable();
	}

	private SimbolTable() {
		vars = new HashMap<>();
		functions = new HashSet<>();
	}

	public void addVar(Var var) {
		System.out.println("SimbolTable.addVar() = " + var);
		if (vars.containsValue(var))
			throw new CompilerException("A variável " + var
					+ " já foi declarada no escopo " + var.getScope());
		if(functions.stream().anyMatch(f->f.getId().equals(var.toString())))
			throw new CompilerException("Já há uma função com o nome " + var);
		this.vars.put(var.getId(),var);
	}

	public void addFunction(Function function) {
		if (functions.contains(function))
			throw new CompilerException(
					"Já existe uma função chamada " + function);
		if (vars.values().stream().anyMatch(v -> v.toString().equals(function.getId())))
			throw new CompilerException("Há uma variável chamada " + function);
		this.functions.add(function);
	}

	public Var getVar(String id) {
		return vars.getOrDefault(id, Var.NULL);
	}

	public void initialize(String id) {
		Var var = getVar(id);
		if(var==Var.NULL)
			throw new CompilerException("Variável " + id + " não declarada");
		var.initialize();
	}

	public void validateVarUse(Token token){
		if(matchVarRegexAndHasntVar(token.getLexeme())){
			throw new CompilerException("A variável "+ token.getLexeme()+ " não foi declarada.");
		}
	}
	
	private boolean matchVarRegexAndHasntVar(String lexeme) {
		boolean matchIdRegex = lexeme.matches("[a-zA-Z][a-zA-Z0-9_]*");
		return matchIdRegex && getVar(lexeme).equals(Var.NULL);
	}

	public void validadeAtribuition(String id) {
		Var var = getVar(id);
		if( var.isConstant() && var.getValue() != null){
			throw new CompilerException("Não é permitida atribuição à constante " + var);
		}
	}

}
