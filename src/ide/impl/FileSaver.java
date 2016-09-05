package ide.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import view.View;

public class FileSaver extends FileIO {

	public static FileSaver instance(View view) {
		return new FileSaver(view);
	}

	private FileSaver(View view) {
		super(view.getComponent());
	}

	public void save(PortugolFile selectedFile) {
		try (PrintWriter writer = new PrintWriter(selectFile())){
			writer.write(selectedFile.getText());
			writer.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Falha ao salvar arquivo", e);
		}
	}

}
