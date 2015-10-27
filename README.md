![Screenshot of the server management window, controller configuration window, and client browser.](/screenshot.png?raw=true)

# Browser Remote
Browser remote is a remote control for a computer's keyboard. It works with any browser on phones, tablets, laptops, or other devices that support WebSockets. The server runs on any OS with Java installed. Browser Remote is a self-contained HTTP and WebSocket server that can work over the internet or just the local network.

## Building
Run `make` in the project's root directory to build `browser-remote.jar`. All resources (.cfg files, html, images) are packaged into `browser-remote.jar`, so it only requires the `libs` folder in the same directory to run. To replace a resource after building, put the modified file in the same relative location and `browser-remote.jar` will load it first before looking inside of the jar archive.

## License
This software is released by Chad Price under a Modified BSD license.

Included Binary Libraries:
* [NanoHTTPD](https://github.com/NanoHttpd/nanohttpd), released under a Modified BSD license
* [Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket), released under an MIT license
