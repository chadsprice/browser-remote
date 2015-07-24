package browser_remote;

import java.awt.event.KeyEvent;
import java.util.*;

public class ZsnesControllerLayout implements ControllerLayout {

	private Set<String> buttons;
	private List<Map<String, Integer>> buttonMaps;
	
	public ZsnesControllerLayout() {
		String[] buttonArray = {"up", "down", "left", "right", "start", "select", "a", "b", "x", "y", "l", "r"};
		buttons = new HashSet<String>(Arrays.asList(buttonArray));
		Map<String, Integer> player1Map = new HashMap<String, Integer>();
		player1Map.put("up", KeyEvent.VK_UP);
		player1Map.put("down", KeyEvent.VK_DOWN);
		player1Map.put("left", KeyEvent.VK_LEFT);
		player1Map.put("right", KeyEvent.VK_RIGHT);
		player1Map.put("start", KeyEvent.VK_ENTER);
		player1Map.put("select", KeyEvent.VK_SHIFT);
		player1Map.put("a", KeyEvent.VK_X);
		player1Map.put("b", KeyEvent.VK_Z);
		player1Map.put("x", KeyEvent.VK_S);
		player1Map.put("y", KeyEvent.VK_A);
		player1Map.put("l", KeyEvent.VK_D);
		player1Map.put("r", KeyEvent.VK_C);
		Map<String, Integer> player2Map = new HashMap<String, Integer>();
		player2Map.put("up", KeyEvent.VK_J);
		player2Map.put("down", KeyEvent.VK_M);
		player2Map.put("left", KeyEvent.VK_N);
		player2Map.put("right", KeyEvent.VK_COMMA);
		player2Map.put("start", KeyEvent.VK_CONTROL);
		player2Map.put("select", KeyEvent.VK_ALT);
		player2Map.put("a", KeyEvent.VK_HOME);
		player2Map.put("b", KeyEvent.VK_END);
		player2Map.put("x", KeyEvent.VK_INSERT);
		player2Map.put("y", KeyEvent.VK_DELETE);
		player2Map.put("l", KeyEvent.VK_PAGE_UP);
		player2Map.put("r", KeyEvent.VK_PAGE_DOWN);
		buttonMaps = new ArrayList<Map<String, Integer>>();
		buttonMaps.add(player1Map);
		buttonMaps.add(player2Map);
	}
	
	@Override
	public Set<String> getButtons() {
		return buttons;
	}

	@Override
	public int getNumberOfControllers() {
		return 2;
	}

	@Override
	public int getKey(String button, int controllerNumber) {
		Integer key = buttonMaps.get(controllerNumber - 1).get(button);
		if (key == null) {
			return -1;
		} else {
			return key;
		}
	}
	
	@Override
	public String toString() {
		return "ZSNES";
	}

}
