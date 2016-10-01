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

	@Test(expected=CompilerException.class)
	public void testAtribuicaoAConstante() {
		String code = "programa {"+ 
						"const inteiro a = 10"+
						"funcao inicio(){"+
							"a=20"+
					   "}"	+
					  "}";
		compile(code);
	}
	
	@Test(expected=CompilerException.class)
	public void testDeclaracaoDuasFuncoesComMesmoNome() {
		String code = "programa {"+
						"funcao inicio(){}"	+
					    "funcao inicio(){}"	+
					  "}";
		compile(code);
	}
	
	@Test(expected=CompilerException.class)
	public void testVariavelComMesmoIdPassadoPorParametro() throws Exception {
		String code = "programa {"+
						"funcao inicio(inteiro a) {"+
						"   inteiro a = 1 "+
						"}"+
					  "}";
		compile(code);
	}
	
	@Test(expected=CompilerException.class)
	public void testDoisParametrosComMesmoIdNome() throws Exception {
		String code = "programa {"+
						"funcao inicio(inteiro a, cadeia a) {"+
						"}"+
					  "}";
		compile(code);
	}

	private void compile(String code) {
		PortugolFile pf = TestUtil.createPortugolFile(code);
		try{
			compiler.compile(pf);
		} catch (Exception e){
			e.printStackTrace();;
			throw e;
		}
	}
	
}
