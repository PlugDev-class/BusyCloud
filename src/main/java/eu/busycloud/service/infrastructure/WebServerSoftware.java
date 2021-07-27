package eu.busycloud.service.infrastructure;

import java.io.IOException;
import java.net.URL;

public class WebServerSoftware extends ServerSoftware {

	public WebServerSoftware(ServerSoftwareType type, String parent, String version, String author) {
		super(type, parent, version, author);
		try {
			this.downloadURL = new URL("https://github.com/PlugDev-class/DownloadRepository/releases/download/latest/" + parent + ".zip");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@Override
	public void download() {
		try {
			download0();
		} catch (IOException e) {
			System.out.print(" .. The requested file isn't available. Please try again later or contact the support, thx ^^\n");
		}
	}

}
