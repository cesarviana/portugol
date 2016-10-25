package view;

import ide.impl.compiler.assembly.Assembly;
import ide.impl.files.PortugolFile;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import view.impl.ViewImpl;

public interface View {

	static View instance() {
		return ViewImpl.getInstance();
	}

	public void showView();

	public void setLoadFileAction(Action loadFileAction);

	public void setSaveFileAction(Action saveFileAction);
	
	public void setCompileAction(Action compileAction);
	
	public void error(Throwable e);

	public void showFile(PortugolFile pf);

	public JComponent getComponent();

	public void addListener(ViewListener viewListener);

	public void message(String string);

	public void setSimbolTable( TableModel tableModel );

	public void setAssembly(Assembly assembly);

}
