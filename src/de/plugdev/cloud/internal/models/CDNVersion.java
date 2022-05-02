package de.plugdev.cloud.internal.models;

import de.plugdev.cloud.internal.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CDNVersion implements IVersion {
	
	private final String version;
	private String downloadURL;
	private boolean available;
	
	public CDNVersion(String version, boolean rCDN) {
		this.version = version;
		this.downloadURL = "https://cdn.getbukkit.org/spigot/" + version + (rCDN ? "-R0.1-SNAPSHOT-latest" : "")+ ".jar";
	}
	
	public void download() {
		FileUtils.download(getDownloadURL(), "backend/downloads/" + this.version + ".jar");
	}
	
	@Override
	public boolean isProxy() {
		return false;
	}

	@Override
	public String getURL() {
		return downloadURL;
	}
	
}
