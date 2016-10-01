package ide_gramatica;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ide.impl.compiler.Compiler;
import ide.impl.compiler.CompilerException;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import ide.impl.compiler.registryControl.Registry;
import ide.impl.compiler.registryControl.VarRegistry;
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
		assertTrue(simbolTable.getVar("a", "inicio").isInitialized());
		assertFalse(simbolTable.getVar("b", "inicio").isInitialized());
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
		assertFalse(simbolTable.getVar("i", "inicio").equals(Var.NULL));
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
	
	@Test
	public void testDeclaracaoDeVariavelComMesmoNomeEmOutroEscopo() throws Exception {
		String code = "programa {"+
						"funcao inicio(inteiro a) {"+
						"}"+
						"funcao dois(inteiro a) {"+
						"}"+
						"funcao tres() {"+
							"inteiro a"+
						"}"+
					  "}";
		compile(code);
	}
	@Test
	public void testUso() {
		String code = "programa {"+
							"funcao inicio(){"+
								" inteiro a=0"+
								" inteiro c=a"+
						   "}"	+
					  "}";
		compile(code);
		Var varA = simbolTable.getVar("a", "inicio");
		Var varC = simbolTable.getVar("c", "inicio");
		assertTrue(varA.isUsed());
		assertFalse(varC.isUsed());
	}
	
	@Test
	public void testUsoVariavelIgualADoEscopoPai() {
		String code = "programa {"+
							" inteiro a = 0"+
							" inteiro b = a " +
							" funcao inicio(){"+
								" inteiro a = 0"+
						   "}"	+
					  "}";
		compile(code);
		Registry registry_a_programa = simbolTable.getRegistry(VarRegistry.instance("programa","a"));
		Registry registry_a_inicio = simbolTable.getRegistry(VarRegistry.instance("inicio","a"));
		assertTrue(registry_a_programa.isUsed());
		assertFalse(registry_a_inicio.isUsed());
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
