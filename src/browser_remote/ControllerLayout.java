package browser_remote;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
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
	
	public static ControllerLayout loadFromFile(String filename) {
		return loadFromFile(new File(filename));
	}
	
	public static ControllerLayout loadFromFile(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		String name = file.getName();
		if (name.endsWith(".cfg")) {
			name = name.substring(0, name.length() - 4);
		}
		ControllerLayout loadedLayout = new ControllerLayout(name);
		loadedLayout.addController();
		while (scanner.hasNextLine()) {
			String[] tokens = scanner.nextLine().split(" ");
			if (tokens[0].equals("button")) {
				if (tokens.length == 7) {
					String button = tokens[1];
					loadedLayout.addButton(button);
					try {
						double width = Double.parseDouble(tokens[2]);
						double height = Double.parseDouble(tokens[3]);
						double left = Double.parseDouble(tokens[4]);
						double top = Double.parseDouble(tokens[5]);
						loadedLayout.setButtonPosition(button, new Rectangle2D.Double(width, height, left, top));
						int keyCode = Integer.parseInt(tokens[6]);
						loadedLayout.setDefaultBrowserKeyCode(button, keyCode);
					} catch (NumberFormatException e) {
						// TODO
					}
				}
			} else if (tokens[0].equals("map")) {
				if (tokens.length == 3) {
					try {
						int key = Integer.parseInt(tokens[2]);
						loadedLayout.setKey(loadedLayout.getNumberOfControllers(), tokens[1], key);
					} catch (NumberFormatException e) {
						// TODO
					}
				}
			} else if (tokens[0].equals("new_controller")) {
				loadedLayout.addController();
			}
		}
		scanner.close();
		return loadedLayout;
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
	
}
