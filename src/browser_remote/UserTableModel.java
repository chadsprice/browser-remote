package browser_remote;

import java.util.*;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class UserTableModel extends AbstractTableModel {

	private static final String[] columnNames = {"IP", "Controller #", "Lock #", "", ""};

	private List<Object[]> data;
	//private Object[] data = {"192.168.2.24", 1, false, new JButton("Kick"), new JButton("Ban")};

	public UserTableModel() {
		super();
		data = new ArrayList<Object[]>();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int row, int column) {
		return data.get(row)[column];
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		data.get(row)[col] = value;
		fireTableCellUpdated(row, col);
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 0) {
			return false;
		}
		return true;
	}

	public void addRow(Object[] row) {
		data.add(row);
		fireTableDataChanged();
	}

}
