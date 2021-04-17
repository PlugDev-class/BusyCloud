package de.plugdev.cloud.api.plugins;

import de.plugdev.cloud.api.ApplicationInterface;

public class Application {
	
	public void onEnable(String[] args) {
		ApplicationInterface.getAPI().getPlugins().add(this);
	}
	
	public void onDisable(String[] args) {
		ApplicationInterface.getAPI().getPlugins().remove(this);
	}
	
	public ApplicationInterface getAPI() {
		return ApplicationInterface.getAPI();
	}
	
}
