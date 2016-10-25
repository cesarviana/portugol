package ide.impl.actions;

import ide.Ide;
import view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class IdeAction extends AbstractAction {

	private final View view;
	private final Ide ide;
	
	public IdeAction(View view, Ide ide) {
		this.view = view;
		this.ide = ide;
	}

	public View getView() {
		return view;
	}
	
	public Ide getIde() {
		return ide;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			execute(e);
		} catch (Exception e2) {
			e2.printStackTrace();
			view.error( e2 );
		}
	}
	
	public abstract void execute(ActionEvent e);
	
}
