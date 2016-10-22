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
        add("HLT 0");
        generateAssemblyAndAssert( code );
    }

    @Test
    public void testOneVector() {
        String code = " programa { inteiro a[5] } ";
        add(".data");
        add("programa_a : 0,0,0,0,0");
        add(".text");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testVarInsideScopes(){
        String code =
                "programa{" +
                "   inteiro a" +
                "   funcao inicio(){" +
                "       inteiro a" +
                "       inteiro x[6]" +
                "       para(inteiro i=0; i<10;i++){" +
                "           inteiro c" +
                "       }" +
                "   }" +
                "}";

        add(".data");
        add("programa_a : 0");
        add("programa_inicio_a : 0");
        add("programa_inicio_x : 0,0,0,0,0,0");
        add("programa_inicio_inicio->para0_c : 0");
        add("programa_inicio_inicio->para0_i : 0");
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
        add("LD a");
        add("STO $out_port");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testEscrevaImediate(){
        String code =
                " programa {  funcao inicio() { escreva(7) } }";
        add(".data");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 7");
        add("STO $out_port");
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
        compiler.compile(TestUtil.createPortugolFile(
                code));
        assembler.setSimbolTable(simbolTable);
        assembler.setCode(code);
        return assembler.assembly();
    }

    private void assemblyOk(Assembly assembly) {
        assertEquals(sb.toString(), assembly.toString());
        System.out.println(sb.toString());
    }

}