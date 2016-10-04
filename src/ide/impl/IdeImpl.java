package ide.impl;

import ide.Ide;
import ide.impl.actions.CompileAction;
import ide.impl.actions.LoadFileAction;
import ide.impl.actions.SaveFileAction;
import ide.impl.compiler.Compiler;
import ide.impl.compiler.SimbolTable;
import ide.impl.files.PortugolFile;
import ide.impl.files.PortugolFiles;
import ide.impl.files.PortugolFilesListenerAdapter;
import view.View;
import view.ViewListenerAdapter;

public class IdeImpl implements Ide {

	private final class ChangeCodeListener extends ViewListenerAdapter {
		@Override
		public void codeChanged(String code) {
			portugolFiles.getSelectedFile().setText(code);
		}
	}
	
	private final class FileDisplayer extends PortugolFilesListenerAdapter {
		@Override
		public void added(PortugolFile pf) {
			listenThatFileWasAddedAndShow(pf);
		}

		private void listenThatFileWasAddedAndShow(PortugolFile pf) {
			view.showFile(pf);
		}
	}

	
	private final View view;
	private final PortugolFiles portugolFiles;
	private final FileLoader fileLoader;
	private final FileSaver fileSaver;
	private final Compiler compiler;
	private final SimbolTable simbolTable;

	public IdeImpl() {
		view = View.instance();
		view.setLoadFileAction(new LoadFileAction(view, this));
		view.setSaveFileAction(new SaveFileAction(view, this));
		view.setCompileAction(new CompileAction(view, this));
		view.addListener(new ChangeCodeListener());
		portugolFiles = PortugolFiles.instance();
		portugolFiles.addListener(new FileDisplayer());
		fileLoader = FileLoader.instance(view);
		fileSaver = FileSaver.instance(view);
		simbolTable = SimbolTable.instance();
		compiler = Compiler.instance(simbolTable);
	}

	@Override
	public void start() {
		view.showView();
	}

	@Override
	public void loadFile() {
		portugolFiles.add(fileLoader.selectFile());
	}

	@Override
	public void saveFile() {
		fileSaver.save(portugolFiles.getSelectedFile());
	}
	
	@Override
	public void compile() {
		compiler.compile(portugolFiles.getSelectedFile());
		view.message("Compilado com sucesso!");
		view.setSimbolTable(new SimbolTableModel(simbolTable));
	}

}
