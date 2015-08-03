package browser_remote;

import java.awt.event.KeyEvent;
import java.util.*;

public class ConfigurableControllerLayout implements ControllerLayout {

	private String name;
	private List<String> buttons;
	private List<Map<String, Integer>> keyMaps;

	public ConfigurableControllerLayout(String name) {
		this.name = name;
		buttons = new ArrayList<String>();
		keyMaps = new ArrayList<Map<String, Integer>>();
		
		// TODO defaults to ZSNES layout
		String[] buttonArray = {"up", "down", "left", "right", "start", "select", "a", "b", "x", "y", "l", "r"};
		buttons.addAll(Arrays.asList(buttonArray));
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
		keyMaps.add(p1Keys);
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
		keyMaps.add(p2Keys);
	}
	
	@Override
	public List<String> getButtons() {
		return buttons;
	}

	@Override
	public int getNumberOfControllers() {
		return keyMaps.size();
	}

	@Override
	public int getKey(String button, int controllerNumber) {
		Integer key = keyMaps.get(controllerNumber - 1).get(button);
		if (key == null) {
			return -1;
		} else {
			return key;
		}
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
		for (Map<String, Integer> keyMap : keyMaps) {
			keyMap.remove(button);
		}
	}

	public void setKey(int controllerNumber, String button, int key) {
		keyMaps.get(controllerNumber - 1).put(button, key);
	}

	@Override
	public String toString() {
		return name;
	}
	
}
