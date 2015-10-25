
browser-remote.jar:
	mkdir -p bin/browser_remote
	javac src/browser_remote/*.java -cp lib/java_websocket.jar:lib/NanoHttpd.jar -d bin
	jar cfm browser-remote.jar Manifest.mf -C bin browser_remote *.cfg http_resources

clean:
	-rm browser-remote.jar
	-rm bin/browser_remote/*.class
