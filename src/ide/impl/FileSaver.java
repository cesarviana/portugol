package ide.impl;

import ide.impl.files.PortugolFile;
import view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class FileSaver extends FileIO {

	public static FileSaver instance(View view) {
		return new FileSaver(view);
	}

	private FileSaver(View view) {
		super(view.getComponent());
	}

	public void save(PortugolFile selectedFile) {
		File file = saveFile();
		boolean append = false;
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, append))){
			writer.write(selectedFile.getText());
			writer.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Falha ao salvar arquivo", e);
		}
	}

}
