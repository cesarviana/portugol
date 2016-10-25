package ide.impl;

import ide.impl.compiler.assembly.Assembly;
import ide.impl.files.PortugolFile;

public class PortugolAssemblyFile extends PortugolFile {

	private Assembly assembly;
	public PortugolAssemblyFile(Assembly assembly) {
		this.assembly = assembly;
	}
	
	@Override
	public String getText() {
		return assembly.toString();
	}
	
	
}
