package browser_remote;

import java.util.*;

import org.java_websocket.WebSocket;

public class User {
	
	public final String ip;
	public WebSocket conn;
	public Map<String, Boolean> isPressed;
	public int controllerNumber;
	
	public User(WebSocket conn, int controllerNumber, ControllerLayout controllerLayout) {
		ip = conn.getRemoteSocketAddress().getAddress().getHostAddress();
		this.conn = conn;
		this.isPressed = new HashMap<String, Boolean>();
		for (String button : controllerLayout.getButtons()) {
			isPressed.put(button, false);
		}
		this.controllerNumber = controllerNumber;
	}
	
}
