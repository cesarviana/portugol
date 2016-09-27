package ide_gramatica;

import org.junit.Before;
import org.junit.Test;

import ide.impl.compiler.Compiler;
import ide.impl.files.PortugolFile;
import util.TestUtil;

public class CompilerTest {

	private Compiler compiler;
	
	@Before
	public void setup() {
		compiler = Compiler.instance();
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
