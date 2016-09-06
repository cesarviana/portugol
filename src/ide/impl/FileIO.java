package ide.impl;

import java.io.File;
import java.util.concurrent.CancellationException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public abstract class FileIO {
	
	private JFileChooser chooser;
	private JComponent parent;
	
	private final class PortugolFileFilter extends FileFilter {
		@Override
		public String getDescription() {
			return "Arquivos Portugol";
		}
		@Override
		public boolean accept(File f) {
			return f.getName().endsWith(".por");
		}
	}
	
	public FileIO(JComponent jComponent) {
		parent = jComponent;
		chooser = createChooser();
	}
	
	private JFileChooser createChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new PortugolFileFilter());
		return chooser;
	}
	
	public JFileChooser getChooser() {
		return chooser;
	}

	public File selectFile() {
		int r = getChooser().showOpenDialog(parent);
		if(r == JFileChooser.APPROVE_OPTION)
			return getChooser().getSelectedFile();
		throw new CancellationException("Seleção de arquivo cancelada.");
	}

}
