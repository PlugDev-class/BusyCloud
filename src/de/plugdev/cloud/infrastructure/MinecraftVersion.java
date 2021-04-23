package de.plugdev.cloud.infrastructure;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

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
		System.out.print("[SETUP] Download \"" + getDownloadURL() + "\" started");
		try {
			Files.copy(new URL(getDownloadURL()).openStream(), Paths.get("backend/downloads/" + getVersion() + ".jar"));
			System.out.print("   .. | ..   done");
		} catch (IOException e) {
			System.out.print("   .. | ..   failed");
		}
		System.out.println(" ");
	}

}
