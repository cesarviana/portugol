package util;

import ide.impl.files.PortugolFile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtil {

	public static PortugolFile createPortugolFile(String code) {
		PortugolFile pf = mock(PortugolFile.class);
		when(pf.getText()).thenReturn(code);
		return pf;
	}
	
}
