package ide.impl.compiler.assembly;

import ide.impl.compiler.*;
import ide.impl.compiler.Compiler;
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
        return sb.append(s).append("\n");
    }

    @Before
    public void setUp() {
        assembler = Assembler.instance();
        simbolTable = SimbolTable.instance();
        assembler.setSimbolTable(simbolTable);
        sb = new StringBuilder();
    }

    @Test
    public void testEmptyTable() {
        Assembly assembly = assembler.assembly();
        add(".data");
        add(".text");
        add("HLT 0");
        assemblyOk(assembly);
    }

    @Test
    public void testOneInt() {
        String code = " programa { inteiro a } ";

        add(".data");
        add("programa_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("HLT 0");
        generateAssemblyAndAssert( code );
    }

    @Test
    public void testOneVector() {
        String code = " programa { inteiro a[5] } ";
        add(".data");
        add("programa_a : 0,0,0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testLeia(){
        String code =
                " programa {  funcao inicio() { inteiro a  leia(a) } }";
        add(".data");
        add("programa_inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LD $in_port");
        add("STO a");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testLeiaVet(){
        String code =
                " programa {  funcao inicio() { inteiro vet[5]  leia(vet[3]) } }";
        add(".data");
        add("programa_inicio_vet : 0,0,0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 3");
        add("STO $indr");
        add("LD $in_port");
        add("STOV vet");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testEscreva(){
        String code =
                " programa {  funcao inicio() { inteiro a  escreva(a) } }";
        add(".data");
        add("programa_inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LD programa_inicio_a");
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
    public void testAtribuicaoValor(){
    	String code =
                " programa {  " +
                "	inteiro a = 5"+
                " }";
        add(".data");
        add("programa_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO programa_a");
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
        add("programa_a : 0");
        add("programa_c : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO programa_a");
        add("LD programa_a");
        add("STO programa_c");
        
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
    	add("programa_inicio_a : 0");
        add("programa_inicio_vet2 : 0,0");
        add(".text");
        add("_PRINCIPAL:");
        
        add("LDI 2");
        add("STO $indr");
        add("LDV programa_inicio_vet2");
        add("STO programa_inicio_a");
        
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
    	add("programa_inicio_a : 0");        
        add("programa_inicio_vet2 : 0,0,0");
        add("programa_inicio_vet3 : 0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        
        add("LDI 2");
        add("STO programa_inicio_a");
        
        add("LDI 0");// indice que recebera atribuicao
        add("STO 1000");

        add("LD programa_inicio_a");
        add("STO 1001");
        
        add("LD 1000");// pega o indice aqui
        add("STO $indr");
        
        add("LD 1001");
        add("STOV programa_inicio_vet2");
        
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
    	add("programa_inicio_a : 0");        
        add("programa_inicio_vet2 : 0,0,0");
        add("programa_inicio_vet3 : 0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        
        // carrega a
        add("LDI 2");
        add("STO programa_inicio_a");
        // guarda o indice onde o vet2 deve receber o valor
        add("LDI 0");
        add("STO 1000");
        // guarda o valor da variavel a na pilha
        add("LD programa_inicio_a");
        add("STO 1001");
        // carrega para $indr o valor do indice guardado 
        add("LD 1000");
        add("STO $indr");
        // salva no vetor o valor da variavel a guardado na pilha
        add("LD 1001");
        add("STOV programa_inicio_vet2");
        
        // carrega a
        add("LDI 2");
        add("STO programa_inicio_a");
        // guarda o indice onde o vet3 deve receber o valor
        add("LDI 0");
        add("STO 1000");
        // guarda o valor da variavel a na pilha
        add("LD programa_inicio_a");
        add("STO 1001");
        // carrega para $indr o valor do indice guardado 
        add("LD 1000");
        add("STO $indr");
        // salva no vetor o valor da variavel a guardado na pilha
        add("LD 1001");
        add("STOV programa_inicio_vet3");
        
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
        add("programa_inicio_vet : 0,0,0,0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 3");
        add("STO $indr");
        add("LDV vet");
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
        add("programa_inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("ADDI 5");
        add("SUBI 5");
        add("STO programa_inicio_a");
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
        add("programa_inicio_x : 0");
        add("programa_inicio_n : 0");
        add("programa_inicio_z : 0");
        add("programa_inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO programa_inicio_x");
        add("LDI 3");
        add("STO programa_inicio_n");
        add("LDI 1");
        add("STO programa_inicio_z");
        add("LD programa_inicio_x");
        add("ADD programa_inicio_n");
        add("SUB programa_inicio_z");
        add("STO programa_inicio_a");
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
        add("programa_inicio_x : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO programa_inicio_x");
        add("LD programa_inicio_x");
        add("ADD programa_inicio_x");
        add("STO programa_inicio_x");
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
        add("programa_inicio_x : 0");
        add("programa_inicio_a : 0");
        add("programa_inicio_z : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO programa_inicio_x");
        add("LDI 2");
        add("STO programa_inicio_a");
        add("LD programa_inicio_x");
        add("ADD programa_inicio_a");
        add("SUBI 3");
        add("STO programa_inicio_z");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    private void generateAssemblyAndAssert(String code) {
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