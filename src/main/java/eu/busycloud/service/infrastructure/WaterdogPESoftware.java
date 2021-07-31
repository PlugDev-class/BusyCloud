package eu.busycloud.service.infrastructure;

import java.io.IOException;
import java.net.URL;

@Deprecated
public class WaterdogPESoftware extends ServerSoftware {
	
	public WaterdogPESoftware(ServerSoftwareType type, String parent, String version, String author) {
		super(type, parent, version, author);
		try {
			this.downloadURL = new URL("https://github.com/WaterdogPE/WaterdogPE/releases/download/v1.1.2/Waterdog.jar");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@Override
	public void download() {
		try {
			download0();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
