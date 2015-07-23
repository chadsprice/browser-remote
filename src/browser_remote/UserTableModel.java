package browser_remote;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class UserTableModel extends AbstractTableModel {

	private static final String[] columnNames = {"IP", "Controller #", "Lock #", "", ""};

	private Object[] data = {"192.168.2.24", 1, false, new JButton("Kick"), new JButton("Ban")};

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return 4;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

}
