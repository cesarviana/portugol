package ide.impl.actions;

import ide.Ide;

import java.awt.event.ActionEvent;

import view.View;

public class SaveAsmAction extends IdeAction {

	public SaveAsmAction(View view, Ide ide) {
		super(view, ide);
		putValue(NAME, "Exportar");
	}

	@Override
	public void execute(ActionEvent e) {
		getIde().saveAsm();
	}

}
