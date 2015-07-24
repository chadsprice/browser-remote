package browser_remote;

import java.util.*;

public interface ControllerLayout {

	public Set<String> getButtons();
	public int getNumberOfControllers();
	public int getKey(String button, int controllerNumber);
	
}
