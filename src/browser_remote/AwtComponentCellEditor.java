package browser_remote;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class AwtComponentCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

	private Component editing;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof Component) {
			return (Component) value;
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (value instanceof Component) {
			editing = (Component) value;
			return editing;
		} else {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public Object getCellEditorValue() {
		return editing;
	}

}
