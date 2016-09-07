package ide.impl.actions;

import java.awt.event.ActionEvent;

import ide.Ide;
import view.View;

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
