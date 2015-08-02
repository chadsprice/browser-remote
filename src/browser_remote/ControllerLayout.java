package browser_remote;

import java.util.*;

public interface ControllerLayout {

	public List<String> getButtons();
	public int getNumberOfControllers();
	public int getKey(String button, int controllerNumber);
	
}
