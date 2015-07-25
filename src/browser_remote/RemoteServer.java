package browser_remote;

import java.net.InetSocketAddress;

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
		if (!controlPanel.isRunning()) {
			return;
		}
		String[] tokens = message.split(" ");
		if (tokens.length != 2)
			return;
		if (tokens[1].equals("down") || tokens[1].equals("up")) {
			synchronized (controlPanel) {
				controlPanel.remoteButton(conn, tokens[0], tokens[1].equals("down"));
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

}
