package ide_gramatica;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ide.impl.compiler.Compiler;
import ide.impl.compiler.CompilerException;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import ide.impl.compiler.VarVector;
import ide.impl.compiler.registryControl.FuncitonRegistry;
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
	
	@Test(expected=CompilerException.class)
	public void testParaDeveDizerQueJaFoiDeclaradoI() {
		String code = "programa {"+
						"funcao inicio(){"+
							"inteiro i" +
							"para (inteiro i = 0; i<10; i++){}"+
					   "}"+
					  "}";
		compile(code);
		assertFalse(simbolTable.getVar("i", "inicio->para0").equals(Var.NULL));
	}
	@Test
	public void testParaComContatadorExterno() {
		String code = "programa {"+
						"funcao inicio(){ "+ 
							" inteiro i"+
							" para (i = 0; i<10; i++){}"+
						"}"	+
					 "}";
		compile(code);
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
	
	@Test
	public void testDeclaracaoParametro() throws Exception {
		String code = "programa {"+
						"funcao inicio(inteiro a) {"+
						"}"+
					  "}";
		compile(code);
		assertTrue(simbolTable.getVar("a", "inicio").isParam());
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
							" inteiro b = a" +
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
	
	@Test(expected=CompilerException.class)
	public void testUsoVariaveisIguaisEmForComSubEscopo() {
		String code = " programa" +
				" {" +
				" 	funcao inicio()" +
				" 	{" +
				" 		para(inteiro i = 0; i < 10; i++){" +
				"	 		para(inteiro i = 0; i < 10; i++){" +
				" 			}" +
				" 		}" +
				" 	}" +
				" }";
		compile(code);
	}
	
	@Test(expected=CompilerException.class)
	public void testUsoVariaveisIguaisFuncaoComSe() {
		String code = " programa" +
				" {" +
				" 	funcao inicio()" +
				" 	{" +
				" 		inteiro a = 0 " +
				" 		se(a > 0){" +
				"	 		inteiro b = 0" +
				" 			para(inteiro i = 0; i < 10; i++){" +
				" 				inteiro b " +
				" 			}" +
				" 		}" +
				" 	}" +
				" }";
		
		compile(code);
	}
	
	@Test
	public void testPermiteVariaveisMesmoNOmeEmEscoposSeESenao() {
		String code = ""+
				"programa"+
				"{"+
				"	funcao inicio()"+
				"	{"+
				"		"+
				"se (1==1) {"+
				"			inteiro a = 0"+
				"		} senao {"+
				"			inteiro a = 0"+
				"		}"+
				"	}"+
				"}";
		compile(code);
		assertTrue(simbolTable.getVar("a", "inicio->se0").isInitialized());
		assertTrue(simbolTable.getVar("a", "inicio->senao0").isInitialized());
	}
	
	@Test
	public void testUsoEscolhaCaso() {
		String code = ""+
				"programa"+
				"{"+
				"	funcao inicio()"+
				"	{"+
				"		inteiro a = 0"+
				"		escolha(a){"+
				"			caso 0:"+
				"				inteiro b = 0"+
				"			pare"+
				"			caso 1:"+
				"				inteiro b = 0"+
				"			pare"+
				"		}"+
				"	}"+
				"}";
		compile(code);
		assertTrue(simbolTable.getVar("b", "inicio->escolha0->caso0").isInitialized());
		assertTrue(simbolTable.getVar("b", "inicio->escolha0->caso1").isInitialized());
	}
	
	@Test
	public void testUsoEscolhaCasoMaisEnquanto() {
		String code = ""+
				"programa"+
				"{"+
				"	funcao inicio()"+
				"	{"+
				"		inteiro a = 0"+
				"		escolha(a){"+
				"			caso 0:"+
				"				inteiro b = 0"+
				"				enquanto (1 == 1){"+
				"					inteiro c = 0"+
				"				}"+
				"			pare"+
				"		}"+
				"	}"+
				"}";
		compile(code);
		assertEquals("0", simbolTable.getVar("c", "inicio->escolha0->caso0->enquanto0").getValue());
	}
	
	@Test(expected=CompilerException.class)
	public void testFacaEnquanto() {
		String code = ""+
				"programa"+
				"{"+
				"	funcao inicio()"+
				"	{"+
				"		inteiro a = 0"+
				"		faca{"+
				"			inteiro a = 0"+
				"		}enquanto(0 == 1)"+
				"	}"+
				"}";
		compile(code);
	}
	
	@Test
	public void testEstadoRegistroVariavelGlobal() {
		String code = ""+
				"programa"+
				"{"+
				"	cadeia a"+
				"}";
		compile(code);
		String expectedA = "name=a |type=cadeia |initialized=false |used=false |scope=programa |param=false";
		String result = simbolTable.getRegistryByExample(VarRegistry.instance("a", "programa")).toString();
		assertEquals(expectedA, result);
	}
	
	@Test
	public void testEstadoRegistroVariavelLocal() {
		String code = ""+
				"programa"+
				"{"+
				"	funcao inicio(){"+
				"		se(1 == 1){"+
				"   		cadeia a"+
				"		}"+
				"	}"+
				"}";
		compile(code);
		String expectedA = "name=a |type=cadeia |initialized=false |used=false |scope=inicio->se0 |param=false";
		String result = simbolTable.getRegistryByExample(VarRegistry.instance("a", "inicio->se0")).toString();
		assertEquals(expectedA, result);
	}
	
	@Test
	public void testEstadoRegistroVariavelParametro() {
		String code = ""+
				"programa"+
				"{"+
				"	funcao inicio(cadeia a){"+
				"	}"+
				"}";
		compile(code);
		String expectedA = "name=a |type=cadeia |initialized=false |used=false |scope=inicio |param=true";
		assertEquals(expectedA, simbolTable.getRegistryByExample(VarRegistry.instance("a", "inicio")).toString());
	}
	
	@Test
	public void testEstadoRegistroFuncao() {
		String code = ""+
				"programa"+
				"{"+
				"	funcao cadeia inicio(cadeia a){"+
				"	}"+
				"}";
		compile(code);
		FuncitonRegistry registroScopeInicio = FuncitonRegistry.instance("inicio");
		String expectedInicio = "name=inicio |type=cadeia |initialized=false |used=false |scope=programa |param=false";
		assertEquals(expectedInicio, simbolTable.getRegistryByExample(registroScopeInicio).toString());
	}
	
	@Test
	public void testEstadoRegistroFuncaoUsada() {
		String code = ""+
				"programa"+
				"{"+
				"	funcao cadeia inicio(cadeia a){"+
				"		inicio(\"a\")"+
				"	}"+
				"}";
		compile(code);
		FuncitonRegistry registroScopeInicio = FuncitonRegistry.instance("inicio");
		String expectedInicio = "name=inicio |type=cadeia |initialized=false |used=true |scope=programa |param=false";
		String result = simbolTable.getRegistryByExample(registroScopeInicio).toString();
		assertEquals(expectedInicio, result);
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
	
	@Test
	public void testDeclaraVetor(){
		String code = ""+
				"programa{"+
				"	funcao inicio(){"+
				"		cadeia a[]"+
				"	}"+
				"}";
		compile(code);
		Var var = simbolTable.getVar("a", "inicio");
		assertTrue( var instanceof VarVector );
	};
	
	@Test
	public void testDeclaraVetorComAtribuicao(){
		String code = ""+
				"programa{"+
				"	funcao inicio(){"+
				"		cadeia a[2] = {\"1\",\"2\"}"+
				"	}"+
				"}";
		compile(code);
		Var var = simbolTable.getVar("a", "inicio");
		assertTrue( var instanceof VarVector );
		assertTrue( var.isInitialized() );
	};
	
	// TODO Not required by the teacher
	@Test(expected=CompilerException.class)
	public void testDeclaraVetorAtribuindoMenosElementosQueONecessario(){
		String code = ""+
				"programa{"+
				"	funcao inicio(){"+
				"		cadeia a[2] = {\"1\"}"+
				"	}"+
				"}";
		compile(code);
	};
	
	@Test
	public void testVerificaVariavelNaoUtilizada(){
		String code = ""+
				"programa{"+
				"	funcao inicio(){"+
				"		inteiro a = 10"+
				"	}"+
				"}";
		compile(code);
		assertFalse(simbolTable.getVar("a", "inicio").isUsed());
	}
}