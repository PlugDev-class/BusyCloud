package de.plugdev.cloud.internal.models;

import de.plugdev.cloud.internal.utils.FileUtils;
import lombok.Data;

@Data
public class BungeeVersion implements IVersion {

	private final String version;
	private String downloadURL;
	private boolean available;
	
	public BungeeVersion(String version) {
		this.version = version;
		this.downloadURL = "https://github.com/TacoSpigot/TacoSpigot/releases/download/" + version + "/TacoSpigot.jar";
	}
	
	public void download() {
		FileUtils.download(downloadURL, "backend/downloads/" + this.version + ".jar");
	}

	@Override
	public boolean isProxy() {
		return true;
	}

	@Override
	public String getURL() {
		return downloadURL;
	}

}
