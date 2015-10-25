package browser_remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class HttpServer extends NanoHTTPD {

	public static InputStream getResource(String filename) {
		// never read files outside of the working directory
		if (filename.contains("..")) {
			throw new SecurityException();
		}
		try {
			// try to get resource from directory
			return new FileInputStream(new File(filename));
		} catch (FileNotFoundException e) {
			// try to get resource from .jar file, returns null if not found
			return HttpServer.class.getResourceAsStream("/" + filename);
		}
	}
	
	private ControlPanel controlPanel;
	
	public HttpServer(ControlPanel controlPanel, int port) throws IOException {
		super(port);
		this.controlPanel = controlPanel;
		start();
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		String imageFilename = controlPanel.getControllerLayout().getImageFilename();
		if (uri.equals("/")) {
			String ip = null;
			String[] headers = {"remote-addr", "http-client-ip"};
			for (String header : headers) {
				if (session.getHeaders().containsKey(header)) {
					ip = session.getHeaders().get(header);
					break;
				}
			}
			InputStream stream = WebpageGenerator.generatePage(controlPanel, ip);
			return new NanoHTTPD.Response(Status.OK, NanoHTTPD.MIME_HTML, stream);
		} else if(imageFilename != null && uri.contains(imageFilename)) {
			InputStream stream = getResource(imageFilename);
			if (stream != null) {
				String mimeType = imageFilename.endsWith(".svg") ? "image/svg+xml" : "image";
				return new NanoHTTPD.Response(Status.OK, mimeType, stream);
			} else {
				return new NanoHTTPD.Response(Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Error 500, internal server error.");
			}
		} else {
			return new NanoHTTPD.Response(Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");
		}
	}
	
}
