package de.plugdev.cloud.internal.models;

import de.plugdev.cloud.internal.utils.FileUtils;
import lombok.Data;

@Data
public class PaperVersion implements IVersion {

	private final String version;
	private String downloadURL;
	private boolean available;
	
	public PaperVersion(String version) {
		this.version = version;
		this.downloadURL = "https://papermc.io/api/v2/projects/paper/versions/1.8.8/builds/443/downloads/" + version + ".jar";
	}
	
	public void download() {
		FileUtils.download(downloadURL, "backend/downloads/" + this.version + ".jar");
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
