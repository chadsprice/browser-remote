package browser_remote;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import javax.swing.*;
import javax.swing.table.TableColumn;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener, WindowFocusListener {

	private static final int DEFAULT_HTTP_PORT = 8080;
	private static final int DEFAULT_WEBSOCKET_PORT = 8081;

	private String urlAddress;
	private int httpPort = DEFAULT_HTTP_PORT;
	private int websocketPort = DEFAULT_WEBSOCKET_PORT;

	private KeyPresser keyPresser;

	private RemoteServer remoteServer;
	private Set<String> banned;

	private ControllerLayout controllerLayout;
	private boolean serverRunning;
	private boolean windowFocused;

	private Set<User> users;
	private boolean userTableShown;

	private JComboBox<ControllerLayout> controllerComboBox;
	private JButton controllerConfigureButton;
	private JButton startButton;
	private JLabel serverStateLabel;
	private JLabel urlLabel;
	private JLabel usersConnectedLabel;
	private JTable userTable;
	private UserTableModel userTableModel;
	private JScrollPane userScrollPane;

	public ControlPanel() {
		try {
			keyPresser = new KeyPresser();
		} catch (AWTException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		banned = new HashSet<String>();
		controllerLayout = new ZsnesControllerLayout();
		users = new HashSet<User>();

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
		controllerComboBox = new JComboBox<ControllerLayout>(new ControllerLayout[] {controllerLayout, new MameControllerLayout()});
		controllerComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (User user : users) {
					releaseAllButtons(user);
				}
				controllerLayout = (ControllerLayout) controllerComboBox.getSelectedItem();
			}
		});

		controllerConfigureButton = new JButton("Configure");
		controllerConfigureButton.addActionListener(this);

		startButton = new JButton("Start");
		startButton.addActionListener(this);

		serverStateLabel = new JLabel();

		urlLabel = new JLabel();

		usersConnectedLabel = new JLabel();

		userTable = new JTable();
		userTableModel = new UserTableModel();
		userTable.setModel(userTableModel);
		userTable.setRowHeight(22);
		userTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		userTable.getColumnModel().getColumn(2).setPreferredWidth(45);
		userTable.getColumnModel().getColumn(3).setPreferredWidth(50);
		userTable.getColumnModel().getColumn(4).setPreferredWidth(50);
		AwtComponentCellEditor cellEditor = new AwtComponentCellEditor();
		TableColumn controllerNumberColumn = userTable.getColumnModel().getColumn(1);
		controllerNumberColumn.setCellRenderer(cellEditor);
		controllerNumberColumn.setCellEditor(cellEditor);
		TableColumn kickColumn = userTable.getColumnModel().getColumn(3);
		kickColumn.setCellRenderer(cellEditor);
		kickColumn.setCellEditor(cellEditor);
		TableColumn banColumn = userTable.getColumnModel().getColumn(4);
		banColumn.setCellRenderer(cellEditor);
		banColumn.setCellEditor(cellEditor);

		userScrollPane = new JScrollPane(userTable);
		int userScrollPaneWidth = userScrollPane.getPreferredSize().width;
		userScrollPane.setPreferredSize(new Dimension(userScrollPaneWidth, userTable.getRowHeight() * 10));

		updateServerStateLabel();
		updateUrlLabel();
		updateUsersConnectedLabel();

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
		add(usersConnectedLabel, constraints);
	}

	public boolean isRunning() {
		return serverRunning && !windowFocused;
	}

	public void startRemoteServer() {
		remoteServer = new RemoteServer(websocketPort, this);
		remoteServer.start();
	}

	public void startHttpServer() {
		try {
			new HttpServer(httpPort);
		} catch (IOException e) {
			JOptionPane.showMessageDialog((JFrame) getTopLevelAncestor(), "Failed to start HTTP server.", "HTTP Server Error", JOptionPane.ERROR_MESSAGE);
		}
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
		for (User user : users) {
			releaseAllButtons(user);
		}
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

	private void updateUsersConnectedLabel() {
		int usersConnected = userTableModel.getRowCount();
		if (usersConnected == 0) {
			usersConnectedLabel.setText("0 users connected");
		} else if (usersConnected == 1) {
			usersConnectedLabel.setText("1 user connected:");
		} else {
			usersConnectedLabel.setText(String.format("%d users connected:", usersConnected));
		}
	}

	private void showUserTable() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.gridy = 5;
		constraints.gridwidth = 3;
		add(userScrollPane, constraints);

		((JFrame) getTopLevelAncestor()).pack();

		userTableShown = true;
	}

	public void remoteOpened(WebSocket conn) {
		String ip = conn.getRemoteSocketAddress().getAddress().getHostAddress();
		// check if it is a user that has been banned
		if (banned.contains(ip)) {
			conn.close(CloseFrame.NORMAL);
			return;
		}
		// check if it is a user that is already connected
		for (User user : users) {
			if (user.ip.equals(ip)) {
				user.conn.close(CloseFrame.NORMAL);
				user.conn = conn;
				return;
			}
		}
		final User user = new User(conn, 1, controllerLayout);
		users.add(user);
		// add user to table
		if (!userTableShown) {
			showUserTable();
		}
		int numberOfControllers = controllerLayout.getNumberOfControllers();
		Integer[] controllerNumbers = new Integer[numberOfControllers];
		for (int i = 0; i < numberOfControllers; i++) {
			controllerNumbers[i] = i + 1;
		}
		final JComboBox<Integer> controllerNumberBox = new JComboBox<Integer>(controllerNumbers);
		controllerNumberBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				releaseAllButtons(user);
				user.controllerNumber = (Integer) controllerNumberBox.getSelectedItem();
			}
		});
		final JButton kick = new JButton("Kick");
		kick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeUser(user);
			}
		});
		final JButton ban = new JButton("Ban");
		ban.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeUser(user);
				banned.add(user.ip);
			}
		});
		userTableModel.addRow(new Object[] {ip, controllerNumberBox, false, kick, ban});
		updateUsersConnectedLabel();
	}

	public void remoteClosed(WebSocket conn) {
		for (User user : users) {
			if (user.conn == conn) {
				removeUser(user);
				break;
			}
		}
	}

	public void remoteButton(WebSocket conn, String button, boolean pressed) {
		for (User user : users) {
			if (user.conn == conn) {
				int key = controllerLayout.getKey(button, user.controllerNumber);
				if (key == -1) {
					break;
				}
				if (pressed) {
					if (!user.isPressed.get(button)) {
						keyPresser.addPress(key);
						user.isPressed.put(button, true);
					}
				} else {
					if (user.isPressed.get(button)) {
						keyPresser.removePress(key);
						user.isPressed.put(button, false);
					}
				}
				break;
			}
		}
	}

	private void removeUser(User user) {
		releaseAllButtons(user);
		users.remove(user);
		for (int i = 0; i < userTableModel.getRowCount(); i++) {
			if (user.ip.equals((String) userTableModel.getValueAt(0, i))) {
				userTableModel.removeRow(i);
				break;
			}
		}
	}

	private void releaseAllButtons(User user) {
		for (Map.Entry<String, Boolean> entry : user.isPressed.entrySet()) {
			if (entry.getValue()) {
				int key = controllerLayout.getKey(entry.getKey(), user.controllerNumber);
				keyPresser.removePress(key);
				entry.setValue(false);
			}
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

		controlPanel.startRemoteServer();
		controlPanel.startHttpServer();
	}

}
