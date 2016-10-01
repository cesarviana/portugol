package ide.impl.compiler;

import java.util.HashMap;
import java.util.Map;

import gals.Token;

public class SimbolTable {

	private Map<String, Var> vars;
	private Map<String, Function> functions;

	public static SimbolTable instance() {
		return new SimbolTable();
	}

	private SimbolTable() {
		vars = new HashMap<>();
		functions = new HashMap<>();
	}

	public void addVar(Var var) {
		System.out.println("SimbolTable.addVar() = " + var);
		validadeNameOfAFunction(var);
		addVarToListAndToScope(var);
	}

	private void addVarToListAndToScope(Var var) {
		getFunction(var.getScope()).addVar(var);
		this.vars.put(var.getId(), var);
	}
	
	public void addParam(Var param) {
		Function function = getFunction(param.getScope());
		function.addParam(param);
	}

	
	private void validadeNameOfAFunction(Var var) {
		if (functions.values().stream().anyMatch(f -> f.getId().equals(var.toString())))
			throw new CompilerException("Já há uma função com o nome " + var);
	}

	public void addFunction(Function function) {
		validadeSameName(function);
		validateNameOfAVar(function);
		this.functions.put(function.getId(),function);
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

	public Var getVar(String id) {
		return vars.getOrDefault(id, Var.NULL);
	}
	
	public Function getFunction(String id){
		return functions.getOrDefault(id, Function.NULL);
	}

	public void initialize(String id) {
		Var var = getVar(id);
		if (var == Var.NULL)
			throw new CompilerException("Variável " + id + " não declarada");
		var.initialize();
	}

	public void validateVarUse(Token token) {
		if (matchVarRegexAndHasntVar(token.getLexeme())) {
			throw new CompilerException(
					"A variável " + token.getLexeme() + " não foi declarada.");
		}
	}

	private boolean matchVarRegexAndHasntVar(String lexeme) {
		boolean matchIdRegex = lexeme.matches("[a-zA-Z][a-zA-Z0-9_]*");
		return matchIdRegex && getVar(lexeme).equals(Var.NULL);
	}

	public void validadeAtribuition(String id) {
		Var var = getVar(id);
		if (var.isConstant() && var.getValue() != null) {
			throw new CompilerException(
					"Não é permitida atribuição à constante " + var);
		}
	}

	public void setValue(String id, String value) {
		getVar(id).setValue(value);
	}


}
