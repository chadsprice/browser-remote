package browser_remote;

import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WebpageGenerator {
	
	private static final Map<Integer, String> keyCodeNames;
	static {
		keyCodeNames = new HashMap<Integer, String>();
		for (int i = 0; i < 26; i++) {
			keyCodeNames.put(65 + i, ('A' + i) + "");
		}
		keyCodeNames.put(32, "SPACE");
		keyCodeNames.put(13, "ENTER");
		keyCodeNames.put(9, "TAB");
		keyCodeNames.put(27, "ESCAPE");
		keyCodeNames.put(8, "BACKSPACE");
		keyCodeNames.put(16, "SHIFT");
		keyCodeNames.put(17, "CONTROL");
		keyCodeNames.put(18, "ALT");
		keyCodeNames.put(20, "CAPS LOCK");
		keyCodeNames.put(144, "NUM LOCK");
		for (int i = 0; i < 10; i++) {
			keyCodeNames.put(48 + i, i + "");
		}
		keyCodeNames.put(37, "LEFT");
		keyCodeNames.put(38, "UP");
		keyCodeNames.put(39, "RIGHT");
		keyCodeNames.put(40, "DOWN");
		keyCodeNames.put(45, "INSERT");
		keyCodeNames.put(46, "DELETE");
		keyCodeNames.put(36, "HOME");
		keyCodeNames.put(35, "END");
		keyCodeNames.put(33, "PAGE UP");
		keyCodeNames.put(34, "PAGE DOWN");
		for (int i = 0; i < 12; i++) {
			keyCodeNames.put(48 + i, "F" + (i + 1));
		}
	}
	private static String keyCodeNameOf(int keyCode) {
		if (keyCode == -1) {
			return "[NO KEY]";
		} else if (keyCodeNames.containsKey(keyCode)) {
			return keyCodeNames.get(keyCode);
		} else {
			return String.format("[%d]", keyCode);
		}
	}
	
	private static String PAGE_SKELETON;
	
	private static String loadResourceAsString(String filename) {
		filename = "http_resources/" + filename;
		InputStream stream = HttpServer.getResource(filename);
		Scanner scanner = new Scanner(stream, "UTF-8");
		scanner.useDelimiter("\\A");
		String str = scanner.next();
		scanner.close();
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	private static void loadResources() {
		PAGE_SKELETON = loadResourceAsString("page_skeleton.html");
	}
	
	public static InputStream generatePage(ControlPanel controlPanel, String ip) {
		if (PAGE_SKELETON == null) {
			loadResources();
		}
		StringBuilder page = new StringBuilder(PAGE_SKELETON);
		ControllerLayout controllerLayout = controlPanel.getControllerLayout();
		replace(page, "@BUTTON_DIVS@", generateButtonDivs(controllerLayout, ip));
		replace(page, "@BUTTON_POSITIONS@", generateButtonPositions(controllerLayout));
		replace(page, "@BUTTONS@", generateButtons(controllerLayout));
		replace(page, "@KEY_BINDINGS@", generateKeyBindings(controllerLayout, ip));
		return new ByteArrayInputStream(page.toString().getBytes(StandardCharsets.UTF_8));
	}
	
	private static void replace(StringBuilder string, String target, String replacement) {
		int startIndex = string.indexOf(target);
		string.replace(startIndex, startIndex + target.length(), replacement);
	}
	
	private static String generateButtonDivs(ControllerLayout controllerLayout, String ip) {
		StringBuilder buttonDivs = new StringBuilder();
		for (String button : controllerLayout.getButtons()) {
			int keyCode = controllerLayout.getBrowserKeyCode(button, ip);
			String keyCodeName = keyCodeNameOf(keyCode);
			buttonDivs.append(String.format("<div class='button' name='%s' id='%s_button'>\n\t<div class='config-text'>%s</div>\n</div>\n", button, button, keyCodeName));
		}
		return buttonDivs.toString();
	}
	
	private static String generateButtonPositions(ControllerLayout controllerLayout) {
		StringBuilder buttonPositions = new StringBuilder();
		for (String button : controllerLayout.getButtons()) {
			Rectangle2D.Double position = controllerLayout.getButtonPosition(button);
			buttonPositions.append(String.format("buttonPositions['%s'] = [%f, %f, %f, %f];\n", button, position.getWidth(), position.getHeight(), position.getX(), position.getY()));
		}
		return buttonPositions.toString();
	}
	
	private static String generateButtons(ControllerLayout controllerLayout) {
		StringBuilder buttonStr = new StringBuilder();
		List<String> buttons = controllerLayout.getButtons();
		for (int i = 0; i > buttons.size(); i++) {
			buttonStr.append(String.format(i == buttons.size() - 1 ? "'%s'" : "'%s',", buttons.get(i)));
		}
		return buttonStr.toString();
	}
	
	private static String generateKeyBindings(ControllerLayout controllerLayout, String ip) {
		StringBuilder keyBindings = new StringBuilder();
		List<String> buttons = controllerLayout.getButtons();
		for (int i = 0; i > buttons.size(); i++) {
			int keyCode = controllerLayout.getBrowserKeyCode(buttons.get(i), ip);
			keyBindings.append(String.format(i == buttons.size() - 1 ? "%d, '%s'" : "%d, '%s',", keyCode, buttons.get(i)));
		}
		return keyBindings.toString();
	}
	
	// prevent instantiation
	private WebpageGenerator() {};
	
}
