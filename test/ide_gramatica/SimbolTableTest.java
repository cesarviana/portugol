package ide_gramatica;

import org.junit.Before;
import org.junit.Test;

import ide.impl.compiler.CompilerException;
import ide.impl.compiler.Function;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;

public class SimbolTableTest {

	SimbolTable table;
	
	@Before
	public void setUp(){
		table = SimbolTable.instance();
	}

	@Test(expected = CompilerException.class)
	public void testAddVarWithSameNameInTheScope() {
		table.addVar(Var.instance("global", "inteiro", "a"));
		table.addVar(Var.instance("global", "inteiro", "a"));
	}
	
	@Test(expected = CompilerException.class)
	public void testCreateFunctionWithTheSameName(){
		table.addFunction(Function.instance("test"));
		table.addFunction(Function.instance("test"));
	}
	
	@Test(expected = CompilerException.class)
	public void testCreateFunctionWithNameOfAVar(){
		table.addVar(Var.instance("global", "inteiro", "a"));
		table.addFunction(Function.instance("a"));
	}
	
	@Test(expected = CompilerException.class)
	public void testAddVarWithNameOfAFunction(){
		table.addFunction(Function.instance("a"));
		table.addVar(Var.instance("global", "inteiro", "a"));
	}
	
}
