package browser_remote;

import java.util.*;

public class ConfigurableControllerLayout implements ControllerLayout {

	private Set<String> buttons;
	private List<Map<String, Integer>> keyMaps;

	public ConfigurableControllerLayout() {
		buttons = new HashSet<String>();
		keyMaps = new ArrayList<Map<String, Integer>>();
		
		// TODO
		addController();
	}
	
	@Override
	public Set<String> getButtons() {
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

	@Override
	public String toString() {
		return "Custom";
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

}
