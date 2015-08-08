package browser_remote;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class RemoteServer extends WebSocketServer {

	private ControlPanel controlPanel;

	public RemoteServer(int port, ControlPanel controlPanel) {
		super(new InetSocketAddress(port));
		this.controlPanel = controlPanel;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		synchronized (controlPanel) {
			controlPanel.remoteOpened(conn);
		}
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		synchronized (controlPanel) {
			controlPanel.remoteClosed(conn);
		}
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		String[] tokens = message.split(" ");
		if (controlPanel.isRunning() && tokens.length == 2 && 
				(tokens[0].equals("down") || tokens[0].equals("up"))) {
			synchronized (controlPanel) {
				controlPanel.remoteButton(conn, tokens[1], tokens[0].equals("down"));
			}
		} else if (tokens.length % 2 == 1 &&
				tokens[0].equals("set_keycodes")) {
			Map<String, Integer> keyCodes = new HashMap<String, Integer>();
			for (int i = 1; i + 1 < tokens.length; i++) {
				try {
					int keyCode = Integer.parseInt(tokens[i + 1]);
					keyCodes.put(tokens[i], keyCode);
				} catch (NumberFormatException e) {
					// ignore bad input
				}
			}
			synchronized (controlPanel) {
				controlPanel.remoteSetKeyCodes(conn, keyCodes);
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

}
