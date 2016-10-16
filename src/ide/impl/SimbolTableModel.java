package ide.impl;

import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.registryControl.Registry;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SimbolTableModel extends AbstractTableModel {

	private String[] columns = {"Nome","Tipo","Inicializada","Usada","Escopo","Parâmetro","Posição","Vetor","Função"};
	private final SimbolTable simbolTable;
	
	public SimbolTableModel(SimbolTable simbolTable) {
		this.simbolTable = simbolTable;
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columns[columnIndex];
	}

	@Override
	public int getRowCount() {
		return getRegistries().size();
	}

	private List<Registry> getRegistries() {
		return simbolTable.getRegistries();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Registry registry = getRegistries().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return registry.getName();
		case 1:
			return registry.getType();
		case 2:
			return boolToSimOuNao(registry.isInitialized());
		case 3:
			return boolToSimOuNao(registry.isUsed());
		case 4:
			return registry.getScope();
		case 5:
			return boolToSimOuNao(registry.isParameter());
		case 6:
			return registry.getParameterPosition();
		case 7:
			return boolToSimOuNao(registry.isVector());
		case 8:
			return boolToSimOuNao(registry.isFunction());
		default:
			break;
		}
		return null;
	}

	private Object boolToSimOuNao(boolean property) {
		return property ? "Sim" : "Não";
	}

}
