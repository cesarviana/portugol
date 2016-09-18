package ide.impl.compiler;

import java.util.HashSet;
import java.util.Set;

public class SimbolTable {

	private static SimbolTable instance;
	
	private Set<Var> vars;
	
	public static SimbolTable instance() {
		if(instance == null)
			instance = new SimbolTable();
		return instance;
	}
	
	private SimbolTable(){
		vars = new HashSet<>();
	}

	public void addVar(Var var) {
		if(vars.contains(var))
			throw new CompilerException("A variável já foi declarada");
		this.vars.add( var );
	}

}
