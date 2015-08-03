package browser_remote;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class ConfigureControllerPanel extends JPanel {

	private ControlPanel controlPanel;
	private ConfigurableControllerLayout controllerLayout;
	private int controllerNumber;

	private JComboBox<Integer> controllerNumberComboBox;
	private JButton addControllerButton;
	private JButton removeControllerButton;
	private JTable keyTable;
	private KeyTableModel keyTableModel;
	private JScrollPane keyScrollPane;
	private JButton doneButton;
	private JButton saveButton;
	private JButton loadButton;

	public ConfigureControllerPanel(ControlPanel controlPanel, ConfigurableControllerLayout controllerLayout) {
		this.controlPanel = controlPanel;
		this.controllerLayout = controllerLayout;
		controllerNumber = 1;

		controllerNumberComboBox = new JComboBox<Integer>();
		int numberOfControllers = controllerLayout.getNumberOfControllers();
		Integer[] controllerNumbers = new Integer[numberOfControllers];
		for (int i = 0; i < numberOfControllers; i++) {
			controllerNumbers[i] = i + 1;
		}
		controllerNumberComboBox = new JComboBox<Integer>(controllerNumbers);
		controllerNumberComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setControllerNumber((Integer) controllerNumberComboBox.getSelectedItem());
			}
		});

		addControllerButton = new JButton("Add");
		addControllerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addControllerButtonPressed();
			}
		});

		removeControllerButton = new JButton("Delete");
		removeControllerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeControllerButtonPressed();
			}
		});
		
		keyTable = new JTable();
		keyTableModel = new KeyTableModel(this, controllerLayout);
		keyTable.setModel(keyTableModel);
		keyTable.setRowHeight(22);
		AwtComponentCellEditor cellEditor = new AwtComponentCellEditor();
		TableColumn keyLabelColumn = keyTable.getColumnModel().getColumn(1);
		keyLabelColumn.setCellRenderer(cellEditor);
		keyLabelColumn.setCellEditor(cellEditor);
		TableColumn noneButtonColumn = keyTable.getColumnModel().getColumn(2);
		noneButtonColumn.setCellRenderer(cellEditor);
		noneButtonColumn.setCellEditor(cellEditor);
		TableColumn deleteButtonColumn = keyTable.getColumnModel().getColumn(3);
		deleteButtonColumn.setCellRenderer(cellEditor);
		deleteButtonColumn.setCellEditor(cellEditor);
		
		keyScrollPane = new JScrollPane(keyTable);
		int keyScrollPaneWidth = keyScrollPane.getPreferredSize().width;
		keyScrollPane.setPreferredSize(new Dimension(keyScrollPaneWidth, keyTable.getRowHeight() * 10));
		
		setFocusable(true);
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				keyTableModel.keyPressed(e);
			}
		});
		
		doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doneButtonPressed();
			}
		});
		
		saveButton = new JButton("Save");
		loadButton = new JButton("Load");

		// add ui elements
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(new JLabel("Controller #:"));
		topPanel.add(controllerNumberComboBox);
		topPanel.add(addControllerButton);
		topPanel.add(removeControllerButton);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(doneButton);
		bottomPanel.add(saveButton);
		bottomPanel.add(loadButton);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(topPanel);
		add(keyScrollPane);
		add(bottomPanel);
	}

	private void setControllerNumber(int controllerNumber) {
		this.controllerNumber = controllerNumber;
		keyTableModel.setControllerNumber(controllerNumber);
	}

	private void addControllerButtonPressed() {
		if (controllerLayout.getNumberOfControllers() == 1) {
			removeControllerButton.setEnabled(true);
		}
		controllerLayout.addController();
		controllerNumberComboBox.addItem(controllerLayout.getNumberOfControllers());
		controllerNumberComboBox.setSelectedIndex(controllerNumberComboBox.getItemCount() - 1);
		setControllerNumber(controllerNumberComboBox.getItemAt(controllerNumberComboBox.getItemCount() - 1));
	}

	private void removeControllerButtonPressed() {
		if (controllerLayout.getNumberOfControllers() == 2) {
			removeControllerButton.setEnabled(false);
		}
		controllerLayout.removeController(controllerNumber);
		controllerNumberComboBox.removeItemAt(controllerNumberComboBox.getItemCount() - 1);
		controllerNumberComboBox.setSelectedIndex(controllerNumberComboBox.getItemCount() - 1);
		setControllerNumber((Integer) controllerNumberComboBox.getSelectedItem());
	}
	
	private void doneButtonPressed() {
		controlPanel.controllerConfigureDone();
	}

	public void setControllerLayout(ConfigurableControllerLayout controllerLayout) {
		this.controllerLayout = controllerLayout;
	}

}
