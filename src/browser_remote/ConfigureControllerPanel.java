package browser_remote;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class ConfigureControllerPanel extends JPanel {

	private static int KEY_TABLE_DISPLAY_ROWS = 15;
	
	private static FileNameExtensionFilter FILE_FILTER = new FileNameExtensionFilter("Configuration Files", "cfg");
	private static JFileChooser configFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setFileFilter(FILE_FILTER);
		return fileChooser;
	}

	private ControlPanel controlPanel;
	private ControllerLayout controllerLayout;
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

	public ConfigureControllerPanel(ControlPanel controlPanel, ControllerLayout controllerLayout) {
		this.controlPanel = controlPanel;
		this.controllerLayout = controllerLayout;
		controllerNumber = 1;

		controllerNumberComboBox = new JComboBox<Integer>();
		for (int i = 0; i < controllerLayout.getNumberOfControllers(); i++) {
			controllerNumberComboBox.addItem(i + 1);
		}
		controllerNumberComboBox.setSelectedItem(controllerNumber);
		controllerNumberComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object selection = controllerNumberComboBox.getSelectedItem();
				if (selection != null) {
					setControllerNumber((Integer) selection);
				}
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
		keyTable.setRowHeight(22);
		updateKeyTableModel();

		keyScrollPane = new JScrollPane(keyTable);
		int keyScrollPaneWidth = keyScrollPane.getPreferredSize().width;
		int keyScrollPaneHeight = keyTable.getRowHeight() * KEY_TABLE_DISPLAY_ROWS;
		keyScrollPane.setPreferredSize(new Dimension(keyScrollPaneWidth, keyScrollPaneHeight));

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
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveButtonPressed();
			}
		});

		loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadButtonPressed();
			}
		});

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

	public void setControllerLayout(ControllerLayout controllerLayout) {
		this.controllerLayout = controllerLayout;

		controllerNumber = 1;

		controllerNumberComboBox.removeAllItems();
		for (int i = 0; i < controllerLayout.getNumberOfControllers(); i++) {
			controllerNumberComboBox.addItem(i + 1);
		}
		controllerNumberComboBox.setSelectedItem(controllerNumber);

		updateKeyTableModel();
	}
	
	private void updateKeyTableModel() {
		keyTableModel = new KeyTableModel(this, controllerLayout);
		keyTable.setModel(keyTableModel);
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

	private void loadButtonPressed() {
		JFileChooser fileChooser = configFileChooser();
		int returnValue = fileChooser.showOpenDialog(getParent());
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			addLayoutFromFile(fileChooser.getSelectedFile());
		}
	}

	private void addLayoutFromFile(File file) {
		ControllerLayout layout = ControllerLayout.loadFromFile(file);
		if (file == null) {
			JOptionPane.showMessageDialog((JFrame) getTopLevelAncestor(), "Failed to read file.", "Load Error", JOptionPane.ERROR_MESSAGE);
		}
		controlPanel.loadedNewControllerLayout(layout);
	}
	
	private void saveButtonPressed() {
		JFileChooser fileChooser = configFileChooser();
		int returnValue = fileChooser.showSaveDialog(getParent());
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (!file.getName().endsWith(".cfg")) {
				file = new File(file.getAbsolutePath() + ".cfg");
			}
			saveConfiguration(file);
		}
	}
	
	private void saveConfiguration(File file) {
		try {
			controllerLayout.saveToFile(file);
		} catch (IOException e) {
			JOptionPane.showMessageDialog((JFrame) getTopLevelAncestor(), "Failed to write file.", "Save Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

}
