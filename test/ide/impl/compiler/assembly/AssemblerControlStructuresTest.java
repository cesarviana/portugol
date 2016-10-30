package ide.impl.compiler.assembly;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ide.impl.compiler.Compiler;
import ide.impl.compiler.SimbolTable;
import util.TestUtil;

@SuppressWarnings("ALL")
public class AssemblerControlStructuresTest {
	
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
	public void testSe(){
		
		String code = ""+
		"programa"+
		"{"+
		"	funcao inicio()"+
		"	{"+
		"		inteiro x = 4"+
		"		inteiro y = 4"+
		"		se(x == y){"+
		"			escreva(1)"+
		"		}"+
		"	}"+
		"}";

        add(".data");
        add("programa_inicio_x : 0");
        add("programa_inicio_y : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 4");
        add("STO programa_inicio_x");
        add("LDI 4");
        add("STO programa_inicio_y");
        add("LD programa_inicio_x");
        add("STO 1000");
        add("LD programa_inicio_y");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");
        add("BNE FIM_INICIO_SE0");
        add("LDI 1");
        add("STO $out_port");
        add("FIM_INICIO_SE0:");
        add("HLT 0");

        generateAssemblyAndAssert(code);

	}
	
	private void generateAssemblyAndAssert(String code) {
        Assembly assembly = getAssembly(code);
        assemblyOk(assembly);
    }
	
	private void assemblyOk(Assembly assembly) {
        assertEquals(sb.toString(), assembly.toString());
        System.out.println(sb.toString());
    }

    private Assembly getAssembly(String code) {
        SimbolTable simbolTable = SimbolTable.instance();
        Compiler compiler = Compiler.instance(simbolTable);
        compiler.compile(TestUtil.createPortugolFile(code));
        assembler.setSimbolTable(simbolTable);
        assembler.setCode(code);
        return assembler.assembly();
    }

}
