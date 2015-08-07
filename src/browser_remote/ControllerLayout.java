package browser_remote;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class ControllerLayout {

	private String name;
	private List<String> buttons;
	private List<Map<String, Integer>> keyMaps;
	private Map<String, Rectangle2D.Double> buttonPositions;
	private Map<String, Integer> defaultBrowserKeyCodes;
	private Map<String, Map<String, Integer>> ipBrowserKeyCodes;

	public ControllerLayout(String name) {
		this.name = name;
		buttons = new ArrayList<String>();
		keyMaps = new ArrayList<Map<String, Integer>>();
		buttonPositions = new HashMap<String, Rectangle2D.Double>();
		defaultBrowserKeyCodes = new HashMap<String, Integer>();
		ipBrowserKeyCodes = new HashMap<String, Map<String, Integer>>();
	}

	public List<String> getButtons() {
		return buttons;
	}

	public int getNumberOfControllers() {
		return keyMaps.size();
	}

	public int getKey(String button, int controllerNumber) {
		Integer key = keyMaps.get(controllerNumber - 1).get(button);
		if (key == null) {
			return -1;
		} else {
			return key;
		}
	}
	
	public int getBrowserKeyCode(String button, String ip) {
		if (ipBrowserKeyCodes.containsKey(ip)) {
			Integer keyCode = ipBrowserKeyCodes.get(ip).get(button);
			return keyCode != null ? keyCode : -1;
		} else {
			Integer keyCode = defaultBrowserKeyCodes.get(button);
			return keyCode != null ? keyCode : -1;
		}
	}
	
	public Rectangle2D.Double getButtonPosition(String button) {
		return buttonPositions.get(button);
	}

	public void addController() {
		Map<String, Integer> keyMap = new HashMap<String, Integer>();
		for (String button : buttons) {
			keyMap.put(button, -1);
		}
		keyMaps.add(keyMap);
	}

	public void removeController(int controllerNumber) {
		keyMaps.remove(controllerNumber - 1);
	}

	public void addButton(String button) {
		buttons.add(button);
		for (Map<String, Integer> keyMap : keyMaps) {
			keyMap.put(button, -1);
		}
	}

	public void removeButton(String button) {
		buttons.remove(button);
		for (Map<String, Integer> keyMap : keyMaps) {
			keyMap.remove(button);
		}
	}

	public void setKey(int controllerNumber, String button, int key) {
		keyMaps.get(controllerNumber - 1).put(button, key);
	}
	
	public void setButtonPosition(String button, Rectangle2D.Double position) {
		buttonPositions.put(button, position);
	}
	
	public int getDefaultBrowserKeyCode(String button) {
		return defaultBrowserKeyCodes.get(button);
	}
	
	public void setDefaultBrowserKeyCode(String button, int keyCode) {
		defaultBrowserKeyCodes.put(button, keyCode);
	}

	@Override
	public String toString() {
		return name;
	}

	public static ControllerLayout zsnesControllerLayout() {
		ControllerLayout layout = new ControllerLayout("ZSNES");
		String[] buttonArray = {"up", "down", "left", "right", "start", "select", "a", "b", "x", "y", "l", "r"};
		layout.buttons.addAll(Arrays.asList(buttonArray));
		layout.defaultBrowserKeyCodes.put("up", 38);
		layout.defaultBrowserKeyCodes.put("down", 40);
		layout.defaultBrowserKeyCodes.put("left", 37);
		layout.defaultBrowserKeyCodes.put("right", 39);
		layout.defaultBrowserKeyCodes.put("start", 13);
		layout.defaultBrowserKeyCodes.put("select", 16);
		layout.defaultBrowserKeyCodes.put("a", 88);
		layout.defaultBrowserKeyCodes.put("b", 90);
		layout.defaultBrowserKeyCodes.put("x", 83);
		layout.defaultBrowserKeyCodes.put("y", 65);
		layout.defaultBrowserKeyCodes.put("l", 68);
		layout.defaultBrowserKeyCodes.put("r", 67);
		layout.buttonPositions.put("up", new Rectangle2D.Double(0.07, 0.07, 0.17, 0.20));
		layout.buttonPositions.put("down", new Rectangle2D.Double(0.07, 0.07, 0.17, 0.34));
		layout.buttonPositions.put("left", new Rectangle2D.Double(0.07, 0.07, 0.10, 0.27));
		layout.buttonPositions.put("right", new Rectangle2D.Double(0.07, 0.07, 0.24, 0.27));
		layout.buttonPositions.put("start", new Rectangle2D.Double(0.095, 0.08, 0.463, 0.293));
		layout.buttonPositions.put("select", new Rectangle2D.Double(0.095, 0.08, 0.36, 0.293));
		layout.buttonPositions.put("a", new Rectangle2D.Double(0.1, 0.1, 0.805, 0.255));
		layout.buttonPositions.put("b", new Rectangle2D.Double(0.1, 0.1, 0.705, 0.325));
		layout.buttonPositions.put("x", new Rectangle2D.Double(0.1, 0.1, 0.70, 0.175));
		layout.buttonPositions.put("y", new Rectangle2D.Double(0.1, 0.1, 0.60, 0.245));
		layout.buttonPositions.put("l", new Rectangle2D.Double(0.24, 0.06, 0.09, 0.06));
		layout.buttonPositions.put("r", new Rectangle2D.Double(0.24, 0.06, 0.62, 0.06));
		Map<String, Integer> p1Keys = new HashMap<String, Integer>();
		p1Keys.put("up", KeyEvent.VK_UP);
		p1Keys.put("down", KeyEvent.VK_DOWN);
		p1Keys.put("left", KeyEvent.VK_LEFT);
		p1Keys.put("right", KeyEvent.VK_RIGHT);
		p1Keys.put("start", KeyEvent.VK_ENTER);
		p1Keys.put("select", KeyEvent.VK_SHIFT);
		p1Keys.put("a", KeyEvent.VK_X);
		p1Keys.put("b", KeyEvent.VK_Z);
		p1Keys.put("x", KeyEvent.VK_S);
		p1Keys.put("y", KeyEvent.VK_A);
		p1Keys.put("l", KeyEvent.VK_D);
		p1Keys.put("r", KeyEvent.VK_C);
		layout.keyMaps.add(p1Keys);
		Map<String, Integer> p2Keys = new HashMap<String, Integer>();
		p2Keys.put("up", KeyEvent.VK_J);
		p2Keys.put("down", KeyEvent.VK_M);
		p2Keys.put("left", KeyEvent.VK_N);
		p2Keys.put("right", KeyEvent.VK_COMMA);
		p2Keys.put("start", KeyEvent.VK_CONTROL);
		p2Keys.put("select", KeyEvent.VK_ALT);
		p2Keys.put("a", KeyEvent.VK_HOME);
		p2Keys.put("b", KeyEvent.VK_END);
		p2Keys.put("x", KeyEvent.VK_INSERT);
		p2Keys.put("y", KeyEvent.VK_DELETE);
		p2Keys.put("l", KeyEvent.VK_PAGE_UP);
		p2Keys.put("r", KeyEvent.VK_PAGE_DOWN);
		layout.keyMaps.add(p2Keys);
		return layout;
	}
	
	public static ControllerLayout mameControllerLayout() {
		ControllerLayout layout = new ControllerLayout("MAME");
		String[] buttonArray = {"up", "down", "left", "right", "start", "select", "a", "b", "x", "y", "l", "r"};
		layout.buttons.addAll(Arrays.asList(buttonArray));
		layout.defaultBrowserKeyCodes.put("up", 38);
		layout.defaultBrowserKeyCodes.put("down", 40);
		layout.defaultBrowserKeyCodes.put("left", 37);
		layout.defaultBrowserKeyCodes.put("right", 39);
		layout.defaultBrowserKeyCodes.put("start", 13);
		layout.defaultBrowserKeyCodes.put("select", 16);
		layout.defaultBrowserKeyCodes.put("a", 88);
		layout.defaultBrowserKeyCodes.put("b", 90);
		layout.defaultBrowserKeyCodes.put("x", 83);
		layout.defaultBrowserKeyCodes.put("y", 65);
		layout.defaultBrowserKeyCodes.put("l", 68);
		layout.defaultBrowserKeyCodes.put("r", 67);
		layout.buttonPositions.put("up", new Rectangle2D.Double(0.07, 0.07, 0.17, 0.20));
		layout.buttonPositions.put("down", new Rectangle2D.Double(0.07, 0.07, 0.17, 0.34));
		layout.buttonPositions.put("left", new Rectangle2D.Double(0.07, 0.07, 0.10, 0.27));
		layout.buttonPositions.put("right", new Rectangle2D.Double(0.07, 0.07, 0.24, 0.27));
		layout.buttonPositions.put("start", new Rectangle2D.Double(0.095, 0.08, 0.463, 0.293));
		layout.buttonPositions.put("select", new Rectangle2D.Double(0.095, 0.08, 0.36, 0.293));
		layout.buttonPositions.put("a", new Rectangle2D.Double(0.1, 0.1, 0.805, 0.255));
		layout.buttonPositions.put("b", new Rectangle2D.Double(0.1, 0.1, 0.705, 0.325));
		layout.buttonPositions.put("x", new Rectangle2D.Double(0.1, 0.1, 0.70, 0.175));
		layout.buttonPositions.put("y", new Rectangle2D.Double(0.1, 0.1, 0.60, 0.245));
		layout.buttonPositions.put("l", new Rectangle2D.Double(0.24, 0.06, 0.09, 0.06));
		layout.buttonPositions.put("r", new Rectangle2D.Double(0.24, 0.06, 0.62, 0.06));
		Map<String, Integer> p1Keys = new HashMap<String, Integer>();
		p1Keys.put("up", KeyEvent.VK_UP);
		p1Keys.put("down", KeyEvent.VK_DOWN);
		p1Keys.put("left", KeyEvent.VK_LEFT);
		p1Keys.put("right", KeyEvent.VK_RIGHT);
		p1Keys.put("start", KeyEvent.VK_1);
		p1Keys.put("select", KeyEvent.VK_5);
		p1Keys.put("a", KeyEvent.VK_CONTROL);
		p1Keys.put("b", KeyEvent.VK_ALT);
		p1Keys.put("x", KeyEvent.VK_SPACE);
		p1Keys.put("y", KeyEvent.VK_SHIFT);
		p1Keys.put("l", KeyEvent.VK_Z);
		p1Keys.put("r", KeyEvent.VK_X);
		layout.keyMaps.add(p1Keys);
		Map<String, Integer> p2Keys = new HashMap<String, Integer>();
		p2Keys.put("up", KeyEvent.VK_R);
		p2Keys.put("down", KeyEvent.VK_F);
		p2Keys.put("left", KeyEvent.VK_D);
		p2Keys.put("right", KeyEvent.VK_G);
		p2Keys.put("start", KeyEvent.VK_2);
		p2Keys.put("select", KeyEvent.VK_6);
		p2Keys.put("a", KeyEvent.VK_A);
		p2Keys.put("b", KeyEvent.VK_S);
		p2Keys.put("x", KeyEvent.VK_Q);
		p2Keys.put("y", KeyEvent.VK_W);
		layout.keyMaps.add(p2Keys);
		return layout;
	}
	
}
