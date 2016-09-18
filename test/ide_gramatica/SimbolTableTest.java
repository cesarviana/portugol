package ide_gramatica;

import org.junit.Test;

import gals.SemanticError;
import ide.impl.compiler.CompilerException;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;

public class SimbolTableTest {

	SimbolTable table = SimbolTable.instance();

	@Test(expected = CompilerException.class)
	public void testAddVar() {
		table.addVar(new Var("global", "inteiro", "a"));
		table.addVar(new Var("global", "inteiro", "a"));
	}

}
