package browser_remote;

import java.awt.event.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class KeyTableModel extends AbstractTableModel {

	private static final String[] columnNames = {"Button", "Key", "", ""};

	private ControllerLayout controllerLayout;
	private int controllerNumber;
	private List<Object[]> data;

	private JLabel keyLabelConfiguring;
	private String buttonConfiguring;

	public KeyTableModel(final ConfigureControllerPanel configureControllerPanel, ControllerLayout controllerLayout) {
		this.controllerLayout = controllerLayout;
		controllerNumber = 1;
		data = new ArrayList<Object[]>();
		for (final String button : controllerLayout.getButtons()) {
			final JLabel keyLabel = new JLabel();
			setKeyLabelText(keyLabel, button);
			keyLabel.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {
					keyLabelPressed(keyLabel, button);
					configureControllerPanel.requestFocus();
				}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {}
			});
			JButton noneButton = new JButton("None");
			noneButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					noneButtonPressed(keyLabel, button);
				}
			});
			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					deleteButtonPressed(button);
				}
			});
			data.add(new Object[] {button, keyLabel, noneButton, deleteButton});
		}
	}

	private void setKeyLabelText(JLabel keyLabel, String button) {
		int key = controllerLayout.getKey(button, controllerNumber);
		String keyLabelText = null;
		if (key == -1) {
			keyLabelText = "";
		} else {
			keyLabelText = KeyEvent.getKeyText(key);
		}
		keyLabel.setText(keyLabelText);
	}

	private void keyLabelPressed(JLabel keyLabel, String button) {
		if (keyLabelConfiguring != null) {
			setKeyLabelText(keyLabelConfiguring, buttonConfiguring);
			if (keyLabel == keyLabelConfiguring) {
				keyLabelConfiguring = null;
				buttonConfiguring = null;
				fireTableDataChanged();
				return;
			}
		}
		keyLabel.setText("[press key]");
		keyLabelConfiguring = keyLabel;
		buttonConfiguring = button;
		fireTableDataChanged();
	}

	public void keyPressed(KeyEvent e) {
		if (keyLabelConfiguring != null) {
			controllerLayout.setKey(controllerNumber, buttonConfiguring, e.getKeyCode());
			setKeyLabelText(keyLabelConfiguring, buttonConfiguring);
			keyLabelConfiguring = null;
			buttonConfiguring = null;
			fireTableDataChanged();
		}
	}

	private void noneButtonPressed(JLabel keyLabel, String button) {
		controllerLayout.setKey(controllerNumber, button, -1);
		keyLabel.setText("");
		if (keyLabel == keyLabelConfiguring) {
			keyLabelConfiguring = null;
			buttonConfiguring = null;
		}
		fireTableDataChanged();
	}

	private void deleteButtonPressed(String button) {
		// TODO
	}

	public void setControllerNumber(int controllerNumber) {
		this.controllerNumber = controllerNumber;
		for (Object[] row : data) {
			if (row[0] instanceof String) {
				setKeyLabelText((JLabel) row[1], (String) row[0]);
			}
		}
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return controllerLayout.getButtons().size();
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
	public boolean isCellEditable(int row, int column) {
		if (column == 0) {
			return false;
		}
		return true;
	}

}
