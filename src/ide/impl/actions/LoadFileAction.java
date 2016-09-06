package ide.impl.actions;

import java.awt.event.ActionEvent;

import ide.Ide;
import view.View;

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
