package ide.impl.compiler.assembly;

import ide.impl.compiler.Compiler;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.impl.Assembly;
import org.junit.Before;
import org.junit.Test;
import util.TestUtil;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ALL")
public class AssemblerTest {

    private Assembler assembler;
    private SimbolTable simbolTable;
    private StringBuilder sb = new StringBuilder();

    public StringBuilder add(String s) {
        sb.append(s);
        if(!s.contains("HLT"))
            return sb.append("\n");
        return sb;
    }

    @Before
    public void setUp() {
        assembler = Assembler.instance();
        simbolTable = SimbolTable.instance();
        assembler.setSimbolTable(simbolTable);
        sb = new StringBuilder();
    }

    @Test
    public void testOneInt() {
        String code = " programa { inteiro a } ";
        add(".data");
        add("a : 0");
        add(".text");
        add("HLT 0");
        generateAssemblyAndAssert( code );
    }
    
    @Test
	public void testVetorOutsideScope(){
		String code = "programa {"+
						"inteiro vet[5]"+
						"funcao inicio(){"+
							"vet[5] = 1"+
						"}"+
					"}";
		add(".data");
		add("vet : 0,0,0,0,0");
		add(".text");
		add("_PRINCIPAL:");
		add("LDI 5");
		add("STO 1000");
		add("LDI 1");
		add("STO 1001");
		add("LD 1000");
		add("STO $indr");
		add("LD 1001");
		add("STOV vet");
        add("HLT 0");
		generateAssemblyAndAssert(code);
	}
    
    @Test
    public void testOneVector() {
        String code = " programa { inteiro a[5] } ";
        add(".data");
        add("a : 0,0,0,0,0");
        add(".text");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testLeia(){
        String code =
                " programa {  funcao inicio() { inteiro a  leia(a) } }";
        add(".data");
        add("inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LD $in_port");
        add("STO inicio_a");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testLeiaVet(){
        String code =
                " programa {  funcao inicio() { inteiro vet[5]  leia(vet[3]) } }";
        add(".data");
        add("inicio_vet : 0,0,0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 3");
        add("STO $indr");
        add("LD $in_port");
        add("STOV inicio_vet");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testEscreva(){
        String code =
                " programa {  " +
                   "funcao inicio() { " +
                        "inteiro a  " +
                        "escreva(a) " +
                   "}" +
                "}";
        add(".data");
        add("inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LD inicio_a");
        add("STO $out_port");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testEscrevaImediate(){
        String code = " programa { funcao inicio() { escreva(7) } }";
        add(".data");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 7");
        add("STO $out_port");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testEscreverVetor(){
        String code = " programa { " +
                            "funcao inicio() { " +
                                "inteiro vet1[5]" +
                                "escreva(vet1[2])" +
                           "}" +
                        "}";
        add(".data");
        add("inicio_vet1 : 0,0,0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 2");
        add("STO $indr");
        add("LDV inicio_vet1");
        add("STO $out_port");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }
    
    @Test
    public void testAtribuicaoValor(){
    	String code =
                " programa {  " +
                "	inteiro a = 5"+
                " }";
        add(".data");
        add("a : 0");
        add(".text");
        add("LDI 5");
        add("STO a");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }
    
    @Test
    public void testAtribuicaoVariavel(){
    	String code =
                " programa {  " +
                "	inteiro a = 5"+
                "	inteiro c = a"+
                " }";
        add(".data");
        add("a : 0");
        add("c : 0");
        add(".text");
        add("LDI 5");
        add("STO a");
        add("LD a");
        add("STO c");
        
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }
    
    @Test
    public void testAtribuicaoVetorParaVariavel(){
    	String code =
                " programa {  " +
                    	" funcao inicio() {" +
                    	" 	inteiro a " +
                    	" 	inteiro vet2[2] " +
                    	" 	a = vet2[2] " +
                    	" } " +
                " }";

    	add(".data");
    	add("inicio_a : 0");
        add("inicio_vet2 : 0,0");
        add(".text");
        add("_PRINCIPAL:");
        
        add("LDI 2");
        add("STO $indr");
        add("LDV inicio_vet2");
        add("STO inicio_a");
        
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }
    
    @Test
    public void testAtribuicaoVariavelParaVetor(){
    	String code =
                " programa {  " +
                    	" funcao inicio() {" +
                    	" 	inteiro a = 2 " +
                    	" 	inteiro vet2[3] " +
                        " 	inteiro vet3[3] " +
                    	" 	vet2[0] = a " +
                    	" } " +
                " }";
        
    	add(".data");
    	add("inicio_a : 0");
        add("inicio_vet2 : 0,0,0");
        add("inicio_vet3 : 0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        
        add("LDI 2");
        add("STO inicio_a");
        
        add("LDI 0");// indice que recebera atribuicao
        add("STO 1000");

        add("LD inicio_a");
        add("STO 1001");
        
        add("LD 1000");// pega o indice aqui
        add("STO $indr");
        
        add("LD 1001");
        add("STOV inicio_vet2");
        
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }
    
    @Test
    public void testAtribuicaoVariavelParaVetor2(){
    	String code =
                " programa {  " +
                    	" funcao inicio() {" +
                    	" 	inteiro a = 2 " +
                    	" 	inteiro vet2[3] " +
                        " 	inteiro vet3[3] " +
                    	" 	vet2[0] = a " +
                    	" 	vet3[0] = a " +
                    	" } " +
                " }";
        
    	add(".data");
    	add("inicio_a : 0");
        add("inicio_vet2 : 0,0,0");
        add("inicio_vet3 : 0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 2");                       // carrega a
        add("STO inicio_a");       // guarda o indice onde o vet2 deve receber o valor
        add("LDI 0");
        add("STO 1000");                    // guarda o valor da variavel a na pilha
        add("LD inicio_a");
        add("STO 1001");                    // carrega para $indr o valor do indice guardado
        add("LD 1000");
        add("STO $indr");                   // salva no vetor o valor da variavel a guardado na pilha
        add("LD 1001");
        add("STOV inicio_vet2");
        add("LDI 0");
        add("STO 1000");
        add("LD inicio_a");        // guarda o valor da variavel a na pilha
        add("STO 1001");
        add("LD 1000");                     // carrega para $indr o valor do indice guardado
        add("STO $indr");                   // salva no vetor o valor da variavel a guardado na pilha
        add("LD 1001");

        add("STOV inicio_vet3");

        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testOperacaoAritmeticaComVetorComoOperando(){
        String code = "programa {" +
                "   funcao inicio(){" +
                "       inteiro vet[5]" +
                "       vet[3] = 2 " +
                "       inteiro a = vet[3] + 6" +
                "   }" +
                "}";
        add(".data");
        add("inicio_vet : 0,0,0,0,0");
        add("inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 3");                       // Carrega indice
        add("STO 1000");                    // Armazena indice em 1000
        add("LDI 2");                       // Carrega valor
        add("STO 1001");                    // Armazena 2 em 1001
        add("LD 1000");                     // Carrega o indice
        add("STO $indr");                   // Armazena em $indr
        add("LD 1001");                     // Carrega valor 2 de 1001 pro ACC
        add("STOV inicio_vet");    // Armazena valor do ACC em vet
                                            // == Finalizou atribuição
        add("LDI 3");                       // Carrega indice que receberá atribuição
        add("STO $indr");                   // Armazena no $indr;
        add("LDV inicio_vet");     // Carrega vet em $indr=3 (vet[3])
        add("ADDI 6");                      // Adiciona
        add("STO inicio_a");       // Armazena

        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testOperacaoAritmeticaComVetorComoSegundoOperando(){
        String code = "programa {" +
                "   funcao inicio(){" +
                "       inteiro vet[5]" +
                "       vet[3] = 2 " +
                "       inteiro a = 6 + vet[3]" +
                "   }" +
                "}";
        add(".data");
        add("inicio_vet : 0,0,0,0,0");
        add("inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 3");
        add("STO 1000");
        add("LDI 2");
        add("STO 1001");
        add("LD 1000");
        add("STO $indr");
        add("LD 1001");
        add("STOV inicio_vet");

        add("LDI 6");
        add("STO 1000");
        add("LDI 3");
        add("STO $indr");
        add("LDV inicio_vet");
        add("STO 1001");
        add("LD 1000");  // 6
        add("ADD 1001"); // vet[3]
        add("STO inicio_a");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testEscrevaVetor(){
        String code =
                "programa {" +
                " funcao inicio(){ " +
                    "inteiro vet[4] " +
                    "escreva(vet[3]) " +
                    "}" +
                "}";
        add(".data");
        add("inicio_vet : 0,0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 3");
        add("STO $indr");
        add("LDV inicio_vet");
        add("STO $out_port");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testADDI_SUBI(){
        String code = "programa { " +
                "       funcao inicio(){ " +
                "           inteiro a = 5 + 5 - 5" +
                "       } " +
                "     }";
        add(".data");
        add("inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("ADDI 5");
        add("SUBI 5");
        add("STO inicio_a");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testADD_SUB(){
        String code = "programa { " +
                "       funcao inicio(){ " +
                "           inteiro x = 5 " +
                "           inteiro n = 3 " +
                "           inteiro z = 1" +
                "           inteiro a = x + n - z" +
                "       } " +
                "     }";
        add(".data");
        add("inicio_x : 0");
        add("inicio_n : 0");
        add("inicio_z : 0");
        add("inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO inicio_x");
        add("LDI 3");
        add("STO inicio_n");
        add("LDI 1");
        add("STO inicio_z");
        add("LD inicio_x");
        add("ADD inicio_n");
        add("SUB inicio_z");
        add("STO inicio_a");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testADD_SUB_sameVar(){
        String code = "programa { " +
                "       funcao inicio(){ " +
                "           inteiro x = 5 " +
                "           x = x + x " +
                "       } " +
                "     }";
        add(".data");
        add("inicio_x : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO inicio_x");
        add("LD inicio_x");
        add("ADD inicio_x");
        add("STO inicio_x");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testADD_SUBI(){
        String code = "programa { " +
                "       funcao inicio(){ " +
                "           inteiro x = 5 " +
                "           inteiro a = 2 " +
                "           inteiro z = x + a - 3" +
                "       } " +
                "     }";
        add(".data");
        add("inicio_x : 0");
        add("inicio_a : 0");
        add("inicio_z : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO inicio_x");
        add("LDI 2");
        add("STO inicio_a");
        add("LD inicio_x");
        add("ADD inicio_a");
        add("SUBI 3");
        add("STO inicio_z");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testVetorRecebendoOperacaoAritmetica(){
        String code = " programa{" +
                "           funcao inicio(){" +
                "               inteiro vet[5]" +
                "               inteiro a = 4" +
                "               vet[2] = a + 5" +
                "           }" +
                "} ";
        add(".data");
        add("inicio_vet : 0,0,0,0,0");
        add("inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 4");
        add("STO inicio_a");
        add("LDI 2");
        add("STO 1000"); // Armarzena indice 2 do vetor na pilha[0]
        add("LD inicio_a");     // Calcula expressão
        add("ADDI 5");
        add("STO 1001"); // Armazena resultado da expressão  na pilha[1]
        add("LD 1000");  // Carrega indice do vetor (pilha[0])
        add("STO $indr");// Coloca indice do vetor na variável especial $indr
        add("LD 1001");  // Carrega resultado da expressão calculado para ACC
        add("STOV inicio_vet"); // Transfere o resultado do ACC pro vet
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
	public void testAtribuiVariavelPraVetor() throws Exception {
		String code = "programa {" +
						"funcao inicio(){ " +
							" inteiro a " +
							" inteiro vet[5] = a " +
						"}"+
					"}";
		add(".data");
		add(".text");
		add("_PRINCIPAL:");
		add("HLT 0");
		
	}

	@Test
	public void testNegative(){
        String code = " programa{" +
                "           inteiro a = -3" +
                "       }";
        add(".data");
        add("a : 0");
        add(".text");
        add("LDI -3");
        add("STO a");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    protected void generateAssemblyAndAssert(String code) {
        Assembly assembly = getAssembly(code);
        assemblyOk(assembly);
    }

    private Assembly getAssembly(String code) {
        SimbolTable simbolTable = SimbolTable.instance();
        Compiler compiler = Compiler.instance(simbolTable);
        compiler.compile(TestUtil.createPortugolFile(code));
        assembler.setSimbolTable(simbolTable);
        assembler.setCode(code);
        return assembler.assembly();
    }

    private void assemblyOk(Assembly assembly) {
        assertEquals(sb.toString(), assembly.toString());
        System.out.println(sb.toString());
    }

}