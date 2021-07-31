package eu.busycloud.service.infrastructure;

import java.io.File;
import java.net.URL;

public class APISoftware extends ServerSoftware {

	public APISoftware(ServerSoftwareType type, String parent, String version, String author) {
		super(type, parent, version, author);
		try {
			this.downloadURL = new URL("https://github.com/PlugDev-class/DownloadRepository/blob/main/CloudAPI.jar");
			this.rootFile = new File("saves/environment-plugins/CloudAPI.jar");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	
	@Override
	public void download() {
		try {
			download0();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	
	
}
