package ide.impl.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PortugolFiles {

	private static PortugolFiles instance;
	private final List<PortugolFilesListener> listeners;
	private final List<PortugolFile> files;
	private PortugolFile selectedFile = PortugolFile.newInstance();

	public static PortugolFiles instance() {
		if (instance == null)
			instance = new PortugolFiles();
		return instance;
	}

	private PortugolFiles() {
		listeners = new ArrayList<>();
		files = new ArrayList<>();
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
