package ide;

import ide.impl.IdeImpl;

public interface Ide {
	static Ide instance() {
		return new IdeImpl();
	}
	void start();
	void loadFile();
	void saveFile();
	void compile();
}
