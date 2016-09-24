package ide.impl.compiler;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ide.impl.files.PortugolFile;
import util.TestUtil;

public class CompilerTest {

	private Compiler compiler;
	private SimbolTable simbolTable;
	
	@Before
	public void setup() {
		simbolTable = SimbolTable.instance();
		compiler = Compiler.instance(simbolTable);
	}
	
	@Test
	public void testDeclaracaoComInicializacao() {
		String code = "programa {"+
							"funcao inicio(){"+
								"inteiro a=0, b"+
						   "}"	+
					  "}";
		compile(code);
		assertTrue(simbolTable.getVar("a").isInitialized());
		assertFalse(simbolTable.getVar("b").isInitialized());
	}

	private void compile(String code) {
		PortugolFile pf = TestUtil.createPortugolFile(code);
		compiler.compile(pf);
	}
	
	@Test(expected=CompilerException.class)
	public void testUsaAntesDeDeclarar(){
		String code = "programa {"+
						"funcao inicio(){"+
							"inteiro a = b"+	
						"}"
					+ "}";
		compile(code);
	}
	
	@Test
	public void testPara() {
		String code = "programa {"+
							"funcao inicio(){"+
								"para (inteiro i = 0; i<10; i++){}"+
						   "}"	+
					  "}";
		compile(code);
		assertFalse(simbolTable.getVar("i").equals(Var.NULL));
	}
	
	@Test
	public void variavel() throws Exception {
		assertTrue("b".matches("[a-zA-Z][a-zA-Z0-9_]*"));
	}

}
