package de.plugdev.cloud.internal.infrastructure;

import de.plugdev.cloud.internal.utils.FileUtils;

public class MinecraftVersion {

	private String version;
	private String downloadURL;
	private boolean available;
	private boolean proxy;

	public MinecraftVersion(String version, String downloadURL, boolean isProxy) {
		this.version = version;
		this.downloadURL = downloadURL;
		this.proxy = isProxy;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getVersion() {
		return version;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public boolean isProxy() {
		return proxy;
	}

	public boolean isAvailable() {
		return available;
	}

	public void install() {
		FileUtils.download(getDownloadURL(), "backend/downloads/" + getVersion() + ".jar");
	}

}