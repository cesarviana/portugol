package ide_gramatica;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ide.impl.compiler.Compiler;
import ide.impl.compiler.CompilerException;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
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
							" inteiro a = 0 "+
							" inteiro b = a " +
							" funcao inicio(){"+
								" inteiro a = 0"+
						   "}"	+
					  "}";
		compile(code);
		Var varAPrograma = simbolTable.getVar("a", "programa");
		Var varAIncio = simbolTable.getVar("a", "inicio");
		assertTrue(varAPrograma.isUsed());
		assertFalse(varAIncio.isUsed());
	}
	
	@Test
	public void testUsoVariaveisIguaisEmTresFors() {
		String code = " programa" +
				" {" +
				" 	funcao inicio()" +
				" 	{" +
				" 		para(inteiro i = 0; i < 10; i++){" +
				" 		}" +
				" 		para(inteiro i = 0; i < 10; i++){" +
				" 		}" +
				" 		para(inteiro i = 0; i < 10; i++){" +
				" 		}" +
				" 	}" +
				" }";
		
		compile(code);
		Var varIPara1 = simbolTable.getVar("i", "inicio->para0");
		Var varIPara2 = simbolTable.getVar("i", "inicio->para1");
		Var varIPara3 = simbolTable.getVar("i", "inicio->para2");
		assertTrue(varIPara1.isUsed());
		assertTrue(varIPara2.isUsed());
		assertTrue(varIPara3.isUsed());
	}
	
	@Test
	public void testUsoVariaveisIguaisNoEscopoDeDoisFors() {
		String code = " programa" +
				" {" +
				" 	funcao inicio()" +
				" 	{" +
				" 		para(inteiro i = 0; i < 10; i++){" +
				" 			inteiro a = 0 " +
				" 			inteiro b = a " +
				" 		}" +
				" 		para(inteiro i = 0; i < 10; i++){" +
				" 			inteiro a " +
				" 		}" +
				" 	}" +
				" }";
		
		compile(code);
		Var varIPara1 = simbolTable.getVar("a", "inicio->para0");
		Var varIPara2 = simbolTable.getVar("a", "inicio->para1");
		assertTrue(varIPara1.isUsed());
		assertFalse(varIPara2.isUsed());
	}

	private void compile(String code) {
		PortugolFile pf = TestUtil.createPortugolFile(code);
		try{
			compiler.compile(pf);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
