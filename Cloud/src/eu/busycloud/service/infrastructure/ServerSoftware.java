package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ServerSoftware {
	
	public enum ServerSoftwareType {

		PROXY, FORGE, JAVA, BEDROCK, BUSYCLOUD, CUSTOM;

	}

	protected ServerSoftwareType type;
	protected File rootFile;
	protected URL downloadURL;
	protected String versionName;
	protected String author;
	protected boolean available;

	public ServerSoftware(ServerSoftwareType type, String parent, String version, String author) {
		try {
			this.type = type;
			this.rootFile = new File("saves/environments/" + parent + "-" + version + ".jar");
			this.downloadURL = new URL("https://serverjars.com/api/fetchJar/" + parent.toLowerCase() + "/" + version);
			this.versionName = parent + "-" + version;
			this.author = author;
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
	
	public void download0() throws IOException {
		FileOutputStream stream = new FileOutputStream(rootFile);
		ReadableByteChannel rbc = Channels.newChannel(downloadURL.openStream());
		stream.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		setAvailable(true);
		stream.close();
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
