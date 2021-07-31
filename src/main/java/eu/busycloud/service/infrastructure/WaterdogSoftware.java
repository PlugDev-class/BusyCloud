package eu.busycloud.service.infrastructure;

import java.net.MalformedURLException;
import java.net.URL;

public class WaterdogSoftware extends ServerSoftware {

	public WaterdogSoftware(ServerSoftwareType type, String parent, String version, String author) {
		super(type, parent, version, author);
	}
	
	public WaterdogSoftware(String version, String author) {
		super(ServerSoftwareType.PROXY, version, "latest", author);
		try {
			this.downloadURL = new URL("https://jenkins.waterdog.dev/job/Waterdog/job/Waterdog/job/master-zlib/lastSuccessfulBuild/artifact/Waterfall-Proxy/bootstrap/target/Waterdog.jar");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
}
