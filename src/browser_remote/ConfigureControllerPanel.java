package browser_remote;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class ConfigureControllerPanel extends JPanel {

	private ConfigurableControllerLayout controllerLayout;
	private int controllerNumber;

	private JComboBox<Integer> controllerNumberComboBox;
	private JButton addControllerButton;
	private JButton removeControllerButton;
	private JScrollPane buttonScrollPane;
	private JButton doneButton;
	private JButton saveButton;
	private JButton loadButton;

	public ConfigureControllerPanel(ConfigurableControllerLayout controllerLayout) {
		this.controllerLayout = controllerLayout;

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
				addController();
			}
		});

		removeControllerButton = new JButton("Delete");
		removeControllerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeController();
			}
		});
		
		doneButton = new JButton("Done");
		saveButton = new JButton("Save");
		loadButton = new JButton("Load");

		// set layout to grid bag
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		// add ui elements
		constraints.anchor = GridBagConstraints.WEST;

		constraints.insets = new Insets(5, 5, 5, 0);
		add(new JLabel("Controller #:"), constraints);

		constraints.insets = new Insets(5, 5, 5, 0);
		add(controllerNumberComboBox, constraints);

		constraints.insets = new Insets(5, 5, 5, 0);
		add(addControllerButton, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		add(removeControllerButton, constraints);

		constraints.gridy = 1;
		constraints.insets = new Insets(5, 5, 5, 0);
		add(doneButton, constraints);

		constraints.insets = new Insets(5, 5, 5, 0);
		add(saveButton, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		add(loadButton, constraints);
	}

	private void setControllerNumber(int controllerNumber) {
		this.controllerNumber = controllerNumber;
		// TODO
	}

	private void addController() {
		controllerLayout.addController();
		controllerNumberComboBox.addItem(controllerLayout.getNumberOfControllers());
		controllerNumberComboBox.setSelectedIndex(controllerNumberComboBox.getItemCount() - 1);
		setControllerNumber(controllerNumberComboBox.getItemAt(controllerNumberComboBox.getItemCount() - 1));
	}

	private void removeController() {
		controllerLayout.removeController(controllerNumber);
		controllerNumberComboBox.removeItemAt(controllerNumberComboBox.getItemCount() - 1);
		controllerNumberComboBox.setSelectedIndex(0);
		setControllerNumber((Integer) controllerNumberComboBox.getSelectedItem());
	}

	public void setControllerLayout(ConfigurableControllerLayout controllerLayout) {
		this.controllerLayout = controllerLayout;
	}

}
