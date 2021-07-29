package eu.busycloud.service.infrastructure;

import java.io.IOException;
import java.net.URL;

public class NukkitSoftware extends ServerSoftware {

	public NukkitSoftware(ServerSoftwareType type, String parent, String version, String author) {
		super(type, parent, version, author);
		try {
			this.downloadURL = new URL("https://github.com/PowerNukkit/PowerNukkit/releases/download/v1.5.1.0-PN/powernukkit-1.5.1.0-PN-shaded.jar");
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
