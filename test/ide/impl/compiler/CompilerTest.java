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
	public void testDeclaracao() {
		String code = "programa {"+
							"funcao inicio(){"+
								"inteiro a=0, b=0"+
						   "}"	+
					  "}";
		PortugolFile pf = TestUtil.createPortugolFile(code);
		compiler.compile(pf);
		assertTrue(simbolTable.getVar("a").getValue().equals("0"));
		assertTrue(simbolTable.getVar("b").getValue().equals("0"));
	}
	
	@Test
	public void testPara() {
		String code = "programa {"+
							"funcao inicio(){"+
								"para (inteiro i = 0; i<10; i++){}"+
						   "}"	+
					  "}";
		PortugolFile pf = TestUtil.createPortugolFile(code);
		compiler.compile(pf);
	}

}
