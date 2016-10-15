package ide.impl.actions;

import ide.Ide;
import view.View;

import java.awt.event.ActionEvent;

public class LoadFileAction extends IdeAction {

	public LoadFileAction(View view, Ide ideImpl) {
		super(view,ideImpl);
		putValue(NAME, "Abrir");
	}

	@Override
	public void execute(ActionEvent arg0) {
		getIde().loadFile();
	}

}
