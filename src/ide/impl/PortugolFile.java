package ide.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class PortugolFile {

	private final File file;
	private String text = "";

	private PortugolFile(File file) {
		this.file = file;
		this.text = extractFileText();
	}

	public static PortugolFile newInstance(File file) {
		if(file==null)
			throw new NullPointerException("Informe o arquivo.");
		return new PortugolFile(file);
	}
	
	private String extractFileText() {
		StringBuilder text = new StringBuilder();
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			lines.forEach(line->{
				text.append(line).append("\n");
			});
		} catch (IOException e) {
			throw new RuntimeException(
					"Falha ao ler o arquivo " + file.getName(), e);
		}
		return text.toString();
	}
	
	public String getText() {
		return text;
	}

	public void setText(String code) {
		this.text = code;
	}

}
