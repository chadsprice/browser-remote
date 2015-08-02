package browser_remote;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MameControllerLayout implements ControllerLayout {

	private List<String> buttons;
	private List<Map<String, Integer>> buttonMaps;
	
	public MameControllerLayout() {
		String[] buttonArray = {"up", "down", "left", "right", "start", "select", "a", "b", "x", "y", "l", "r"};
		buttons = Arrays.asList(buttonArray);
		Map<String, Integer> player1Map = new HashMap<String, Integer>();
		player1Map.put("up", KeyEvent.VK_UP);
		player1Map.put("down", KeyEvent.VK_DOWN);
		player1Map.put("left", KeyEvent.VK_LEFT);
		player1Map.put("right", KeyEvent.VK_RIGHT);
		player1Map.put("start", KeyEvent.VK_1);
		player1Map.put("select", KeyEvent.VK_5);
		player1Map.put("a", KeyEvent.VK_CONTROL);
		player1Map.put("b", KeyEvent.VK_ALT);
		player1Map.put("x", KeyEvent.VK_SPACE);
		player1Map.put("y", KeyEvent.VK_SHIFT);
		player1Map.put("l", KeyEvent.VK_Z);
		player1Map.put("r", KeyEvent.VK_X);
		Map<String, Integer> player2Map = new HashMap<String, Integer>();
		player2Map.put("up", KeyEvent.VK_R);
		player2Map.put("down", KeyEvent.VK_F);
		player2Map.put("left", KeyEvent.VK_D);
		player2Map.put("right", KeyEvent.VK_G);
		player2Map.put("start", KeyEvent.VK_2);
		player2Map.put("select", KeyEvent.VK_6);
		player2Map.put("a", KeyEvent.VK_A);
		player2Map.put("b", KeyEvent.VK_S);
		player2Map.put("x", KeyEvent.VK_Q);
		player2Map.put("y", KeyEvent.VK_W);
		buttonMaps = new ArrayList<Map<String, Integer>>();
		buttonMaps.add(player1Map);
		buttonMaps.add(player2Map);
	}
	
	@Override
	public List<String> getButtons() {
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
		return "MAME";
	}
	
}
