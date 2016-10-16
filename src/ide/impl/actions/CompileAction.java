package ide.impl.actions;

import ide.Ide;
import view.View;

import java.awt.event.ActionEvent;

public class CompileAction extends IdeAction {

	public CompileAction(View view, Ide ide) {
		super(view, ide);
		putValue(NAME, "Compilar");
	}

	@Override
	public void execute(ActionEvent e) {
		getIde().compile();
	}
}
