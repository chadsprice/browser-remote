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
			return HttpServer.class.getResourceAsStream(filename);
		}
	}
	
	public HttpServer(int port) throws IOException {
		super(port);
		start();
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		if (uri.equals("/")) {
			InputStream stream = getResource("http_resources/snes_controller.html");
			if (stream != null) {
				return new NanoHTTPD.Response(Status.OK, NanoHTTPD.MIME_HTML, stream);
			} else {
				return new NanoHTTPD.Response(Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Error 500, internal server error.");
			}
			/*FileInputStream fis;
			try {
				fis = new FileInputStream(new File("http_resources/snes_controller.html"));
				return new NanoHTTPD.Response(Status.OK, NanoHTTPD.MIME_HTML, fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return new NanoHTTPD.Response(Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Error 500, internal server error.");
			}*/
		} else if(uri.contains("svg_snes_controller.svg")) {
			InputStream stream = getResource("http_resources/snes_controller.html");
			if (stream != null) {
				return new NanoHTTPD.Response(Status.OK, "image/svg+xml", stream);
			} else {
				return new NanoHTTPD.Response(Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Error 500, internal server error.");
			}
			/*FileInputStream fis;
			try {
				fis = new FileInputStream(new File("http_resources/snes_controller.svg"));
				return new NanoHTTPD.Response(Status.OK, "image/svg+xml", fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return new NanoHTTPD.Response(Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Error 500, internal server error.");
			}*/
		} else {
			return new NanoHTTPD.Response(Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");
		}
	}
	
}
