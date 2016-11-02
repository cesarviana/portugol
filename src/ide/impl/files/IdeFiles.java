package ide.impl.files;

import ide.impl.compiler.assembly.impl.Assembly;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class IdeFiles {

	private static IdeFiles instance;
	private final List<PortugolFilesListener> listeners;
	private final List<PortugolFile> files;
	private Assembly assembly;
	private PortugolFile selectedFile = PortugolFile.newInstance();

	public static IdeFiles instance() {
		if (instance == null)
			instance = new IdeFiles();
		return instance;
	}

	private IdeFiles() {
		listeners = new ArrayList<>();
		files = new ArrayList<>();
		assembly = new Assembly();
	}

	public void add(File file) {
		PortugolFile portugolFile = PortugolFile.newInstance(file);
		files.add(portugolFile);
		listeners.forEach(list -> list.added(portugolFile));
		selectedFile = portugolFile;
	}

	public void addListener(PortugolFilesListener listener) {
		this.listeners.add(listener);
	}

	public PortugolFile getSelectedFile() {
		return selectedFile;
	}
	
}
