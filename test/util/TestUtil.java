package util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ide.impl.files.PortugolFile;

public class TestUtil {

	public static PortugolFile createPortugolFile(String code) {
		PortugolFile pf = mock(PortugolFile.class);
		when(pf.getText()).thenReturn(code);
		return pf;
	}
	
}
