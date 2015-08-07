package browser_remote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WebpageGenerator {
	
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
	
	public static InputStream generatePage(ControllerLayout controllerLayout, String ip) {
		if (PAGE_SKELETON == null) {
			loadResources();
		}
		StringBuilder page = new StringBuilder(PAGE_SKELETON);
		replace(page, "@BUTTON_DIVS@", generateButtonDivs(controllerLayout));
		System.out.print(page);
		return new ByteArrayInputStream(page.toString().getBytes(StandardCharsets.UTF_8));
	}
	
	private static void replace(StringBuilder string, String target, String replacement) {
		int startIndex = string.indexOf(target);
		string.replace(startIndex, startIndex + target.length(), replacement);
	}
	
	private static String generateButtonDivs(ControllerLayout controllerLayout) {
		StringBuilder buttonDivs = new StringBuilder();
		for (String button : controllerLayout.getButtons()) {
			buttonDivs.append(String.format("<div class='button' name='%s' id='%s_button'>\n<div class='config-text'>%s</div>\n</div>", button, button, button.toUpperCase()));
		}
		return buttonDivs.toString();
	}
	
	// prevent instantiation
	private WebpageGenerator() {};
	
}
