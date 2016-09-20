package ide.impl.compiler;

import java.util.HashSet;
import java.util.Set;

public class SimbolTable {

	private static SimbolTable instance;

	private Set<Var> vars;
	private Set<Function> functions;

	public static SimbolTable instance() {
//		if (instance == null)
//			instance = new SimbolTable();
//		return instance;
		return new SimbolTable();
	}

	private SimbolTable() {
		vars = new HashSet<>();
		functions = new HashSet<>();
	}

	public void addVar(Var var) {
		if (vars.contains(var))
			throw new CompilerException("A variável " + var
					+ " já foi declarada no escopo " + var.getScope());
		if(functions.stream().anyMatch(f->f.getId().equals(var.toString())))
			throw new CompilerException("Já há uma função com o nome " + var);
		this.vars.add(var);
	}

	public void addFunction(Function function) {
		if (functions.contains(function))
			throw new CompilerException(
					"Já existe uma função chamada " + function);
		if (vars.stream().anyMatch(v -> v.toString().equals(function.getId())))
			throw new CompilerException("Há uma variável chamada " + function);
		this.functions.add(function);
	}

}
