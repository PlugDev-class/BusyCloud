package de.plugdev.cloud.internal.infrastructure;

import de.plugdev.cloud.internal.models.IVersion;
import de.plugdev.cloud.internal.utils.FileUtils;
import lombok.Data;

@Data
public class MinecraftVersion implements IVersion {

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

	@Override
	public void download() {
		FileUtils.download(getDownloadURL(), "backend/downloads/" + getVersion() + ".jar");
	}

	@Override
	public String getURL() {
		return downloadURL;
	}

}
