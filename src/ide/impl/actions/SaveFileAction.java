package ide.impl.actions;

import java.awt.event.ActionEvent;

import ide.Ide;
import view.View;

public class SaveFileAction extends IdeAction {

	public SaveFileAction(View view, Ide ide) {
		super(view, ide);
		putValue(NAME, "Salvar");
	}

	@Override
	public void execute(ActionEvent e) {
		getIde().saveFile();
	}

}
