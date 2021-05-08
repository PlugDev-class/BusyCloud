package de.plugdev.cloud.internal.webinterface.install;

public class WebInstaller {
	
	@SuppressWarnings("unused")
	private boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().equals("nix");
	}
	
	public void install(String path) {
		
	}
	
}