package ide_gramatica;

import static org.junit.Assert.*;

import java.util.List;

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
	
	@Test(expected = CompilerException.class)
	public void testAddVarWithSameNameOfAParameter() throws Exception {
		Function f = Function.instance("inicio");
		table.addFunction(f);
		Var varA = Var.instance("inicio", "inteiro", "a");
		table.addParam(varA);
		table.addVar(varA);
	}
	
	@Test(expected = CompilerException.class)
	public void testAddTwoParametersWithTheSameName() throws Exception {
		Function f = Function.instance("inicio");
		table.addFunction(f);
		Var varA = Var.instance("inicio", "inteiro", "a");
		table.addParam(varA);
		table.addParam(varA);
	}
	
	@Test
	public void testAddVarIsParameter() throws Exception {
		Function f = Function.instance("inicio");
		table.addFunction(f);
		Var param = Var.instance("inicio", "inteiro", "a");
		table.addParam(param);
		assertTrue(param.isParam());
	}
	
	@Test
	public void testGetParameterPositions() throws Exception {
		Function f = Function.instance("inicio");
		table.addFunction(f);
		Var param1 = Var.instance("inicio", "inteiro", "primeiro");
		table.addParam(param1);
		Var param2 = Var.instance("inicio", "inteiro", "segundo");
		table.addParam(param2);
		Var commonVar = Var.instance("inicio", "inteiro", "x");
		table.addVar(commonVar);
		assertEquals(1,param1.getParamPosition());
		assertEquals(2,param2.getParamPosition());
		assertEquals(0,commonVar.getParamPosition());
	}
}
