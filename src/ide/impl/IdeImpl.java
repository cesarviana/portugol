package ide.impl;

import ide.Ide;
import ide.impl.actions.CompileAction;
import ide.impl.actions.LoadFileAction;
import ide.impl.actions.SaveAsmAction;
import ide.impl.actions.SaveFileAction;
import ide.impl.compiler.Compiler;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.assembly.Assembler;
import ide.impl.compiler.assembly.Assembly;
import ide.impl.files.IdeFiles;
import ide.impl.files.PortugolFile;
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
	
	private final class ChangeCodeListenerUpdateSimbolTableAndShowError extends ViewListenerAdapter {
		@Override
		public void codeChanged(String code) {
			try{
				compile();
			} catch (Exception e) {
				view.error(e);
			}
		}
	}
	
	private final class ChangeCodeListenerAssembly extends ViewListenerAdapter {
		@Override
		public void codeChanged(String code) {
			try{
				assembly();
			} catch (Exception e) {

			}
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
	private final IdeFiles portugolFiles;
	private final FileLoader fileLoader;
	private final FileSaver fileSaver;
	private final Compiler compiler;
	private final SimbolTable simbolTable;

	public IdeImpl() {
		view = View.instance();
		view.setLoadFileAction(new LoadFileAction(view, this));
		view.setSaveFileAction(new SaveFileAction(view, this));
		view.setSaveAsmAction(new SaveAsmAction(view, this));
		CompileAction compileAction = new CompileAction(view, this);
		view.setCompileAction(compileAction);
		view.addListener(new ChangeCodeListener());
		view.addListener(new ChangeCodeListenerUpdateSimbolTableAndShowError());
		view.addListener(new ChangeCodeListenerAssembly());
		portugolFiles = IdeFiles.instance();
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
	public void saveAsm() {
		fileSaver.save( new PortugolAssemblyFile(portugolFiles.getAssembly()));
	}
	
	@Override
	public void compile() {
		compiler.compile(portugolFiles.getSelectedFile());
		view.message("Compilado com sucesso!");
		view.setSimbolTable(new SimbolTableModel(simbolTable));
	}
	
	@Override
	public void assembly() {
		Assembler assembler = Assembler.instance();
		assembler.setSimbolTable(simbolTable);
		assembler.setCode(portugolFiles.getSelectedFile().getText());
		Assembly assembly = assembler.assembly();
		view.setAssembly( assembly );
		portugolFiles.setAssembly(assembly);
	}

}
