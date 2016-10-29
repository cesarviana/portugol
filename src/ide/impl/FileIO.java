package ide.impl;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.concurrent.CancellationException;

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
			return f.isDirectory() || f.getName().endsWith(".por") || f.getName().endsWith(".asm");
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

	public JComponent getParent() {
		return parent;
	}
	
	public File selectFile() {
		int r = getChooser().showOpenDialog(getParent());
		if(r == JFileChooser.APPROVE_OPTION)
			return getChooser().getSelectedFile();
		throw new CancellationException("Salvamento de arquivo cancelado.");
	}
	
	public File saveFile() {
		int r = getChooser().showSaveDialog(getParent());
		if(r == JFileChooser.APPROVE_OPTION)
			return getChooser().getSelectedFile();
		throw new CancellationException("Salvamento de arquivo cancelado.");
	}
}
