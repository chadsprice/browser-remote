package browser_remote;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener, WindowFocusListener {

	private static final int DEFAULT_HTTP_PORT = 8080;
	private static final int DEFAULT_WEBSOCKET_PORT = 8081;

	private String urlAddress;
	private int httpPort = DEFAULT_HTTP_PORT;
	private int websocketPort = DEFAULT_WEBSOCKET_PORT;

	private boolean serverRunning;
	private boolean windowFocused;

	private JComboBox controllerComboBox;
	private JButton controllerConfigureButton;
	private JButton startButton;
	private JLabel serverStateLabel;
	private JLabel urlLabel;
	private JTable userTable;
	private JScrollPane userScrollPane;

	public ControlPanel() {
		// find url address
		try {
			urlAddress = null;
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while(interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = (InetAddress) addresses.nextElement();
					String hostAddress = address.getHostAddress();
					if (hostAddress.startsWith("192.168.")) {
						urlAddress = hostAddress;
					} else if (urlAddress == null && hostAddress.startsWith("10.")) {
						urlAddress = hostAddress;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		// create ui elements
		controllerComboBox = new JComboBox(new String[] {"ZSNES", "MAME"});

		controllerConfigureButton = new JButton("Configure");
		controllerConfigureButton.addActionListener(this);

		startButton = new JButton("Start");
		startButton.addActionListener(this);

		serverStateLabel = new JLabel();
		updateServerStateLabel();

		urlLabel = new JLabel();
		updateUrlLabel();

		String[] columnNames = {"IP", "Controller #", "Lock #", "", ""};
		Object[][] data = {
				{"192.168.2.24", 1, false, new JButton("Kick"), new JButton("Ban")},
				{"192.168.2.24", 2, false, new JButton("Kick"), new JButton("Ban")}
		};
		userTable = new JTable(data, columnNames);
		AwtComponentCellEditor cellEditor = new AwtComponentCellEditor();
		TableColumn kickColumn = userTable.getColumnModel().getColumn(3);
		kickColumn.setCellRenderer(cellEditor);
		kickColumn.setCellEditor(cellEditor);
		TableColumn banColumn = userTable.getColumnModel().getColumn(4);
		banColumn.setCellRenderer(cellEditor);
		banColumn.setCellEditor(cellEditor);

		userScrollPane = new JScrollPane(userTable);
		int userScrollPaneWidth = userScrollPane.getPreferredSize().width;
		userScrollPane.setPreferredSize(new Dimension(userScrollPaneWidth, userTable.getRowHeight() * 8));
		System.out.println();

		// set layout to grid bag
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		// add ui elements
		constraints.anchor = GridBagConstraints.WEST;

		constraints.insets = new Insets(5, 5, 5, 0);
		add(new JLabel("Controller:"), constraints);

		constraints.insets = new Insets(5, 5, 5, 0);
		add(controllerComboBox, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		add(controllerConfigureButton, constraints);

		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.gridy = 1;
		add(startButton, constraints);

		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		add(serverStateLabel, constraints);

		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.gridy = 3;
		constraints.gridwidth = 3;
		add(urlLabel, constraints);

		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		add(userScrollPane, constraints);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == controllerConfigureButton) {
			handleControllerConfigureButtonPress();
		} else if (action.getSource() == startButton) {
			handleRunButtonPress();
		}
	}

	@Override
	public void windowLostFocus(WindowEvent arg0) {
		windowFocused = false;
		updateServerStateLabel();
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0) {
		windowFocused = true;
		updateServerStateLabel();
	}

	private void handleControllerConfigureButtonPress() {
		// TODO
	}

	private void handleRunButtonPress() {
		serverRunning = !serverRunning;
		if (serverRunning) {
			startButton.setText("Stop");
		} else {
			startButton.setText("Start");
		}
		updateServerStateLabel();
	}

	private void updateServerStateLabel() {
		if (serverRunning) {
			if (windowFocused) {
				serverStateLabel.setText("<html>The remote is <font color='gray'>PAUSED</font> in this window</html>");
			} else {
				serverStateLabel.setText("<html>The remote is <font color='green'>ON</font></html>");
			}
		} else {
			serverStateLabel.setText("<html>The remote is <font color='gray'>OFF</font></html>");
		}
	}

	private void updateUrlLabel() {
		if (urlAddress == null) {
			urlLabel.setText("Could not determine your URL!");
		} else {
			urlLabel.setText(String.format("URL: %s:%d", urlAddress, httpPort));
		}
	}

	public static void main(String[] args) {
		ControlPanel controlPanel = new ControlPanel();
		JFrame frame = new JFrame("Browser Remote");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(controlPanel);
		frame.pack();
		frame.addWindowFocusListener(controlPanel);
		frame.setVisible(true);
	}

}
