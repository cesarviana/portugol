package ide_gramatica;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ide.impl.compiler.Compiler;
import ide.impl.compiler.CompilerException;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import ide.impl.compiler.registryControl.SimbolTableRegistry;
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
	public void testInsercaoInteiroNaTabelaDeSimbolos() {
		String code = "programa {"+
							"funcao inicio(){"+
								" inteiro a=0, b "+
								" inteiro c=a, b "+
						   "}"	+
					  "}";
		compile(code);
		SimbolTableRegistry registrya = simbolTable.getRegistry("a");
		String expected = "name=a |type=inteiro |initialized=true |used=true";
		assertEquals(expected, registrya.toString());
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
