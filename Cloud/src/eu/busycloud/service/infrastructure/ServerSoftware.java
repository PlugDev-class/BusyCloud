package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ServerSoftware {
	
	public enum ServerSoftwareType {

		PROXY, FORGE, JAVA, BEDROCK, CUSTOM;

	}

	private ServerSoftwareType type;
	private File rootFile;
	private URL downloadURL;
	private String versionName;
	private String author;
	private long release;
	private boolean available;

	public ServerSoftware(ServerSoftwareType type, String parent, String version, String author) {
		try {
			this.type = type;
			this.rootFile = new File("saves/environments/" + parent + "-" + version + ".jar");
			this.downloadURL = new URL("https://serverjars.com/api/fetchJar/" + parent.toLowerCase() + "/" + version);
			this.versionName = parent + "-" + version;
			this.author = author;
			this.release = 0;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void download() {
		try (FileOutputStream stream = new FileOutputStream(rootFile)) {
			ReadableByteChannel rbc = Channels.newChannel(downloadURL.openStream());
			stream.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			setAvailable(true);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public File getRootFile() {
		return rootFile;
	}

	public String getVersionName() {
		return versionName;
	}
	
	public String getAuthor() {
		return author;
	}

	public long getRelease() {
		return release;
	}

	public ServerSoftwareType getType() {
		return type;
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
}
