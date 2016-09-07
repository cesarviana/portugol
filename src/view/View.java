package view;

import javax.swing.Action;
import javax.swing.JComponent;

import ide.impl.files.PortugolFile;
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

}
