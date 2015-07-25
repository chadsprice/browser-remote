package browser_remote;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.*;

public class KeyPresser {

	private Map<Integer, Integer> keyPresses;
	private Robot robot;
	
	public KeyPresser() throws AWTException {
		keyPresses = new HashMap<Integer, Integer>();
		robot = new Robot();
	}
	
	public void addPress(int key) {
		if (!keyPresses.containsKey(key)) {
			keyPresses.put(key, 0);
		}
		if (keyPresses.get(key) == 0) {
			robot.keyPress(key);
		}
		keyPresses.put(key, keyPresses.get(key) + 1);
	}
	
	public void removePress(int key) {
		if (!keyPresses.containsKey(key)) {
			throw new IllegalStateException();
		}
		if (keyPresses.get(key) == 1) {
			robot.keyRelease(key);
		}
		keyPresses.put(key, keyPresses.get(key) - 1);
	}
	
}
