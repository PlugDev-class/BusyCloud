package de.plugdev.cloud;

import java.io.File;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.infrastructure.Boot;
import de.plugdev.cloud.infrastructure.Infrastructure;
import de.plugdev.cloud.infrastructure.Setup;

public class Cloud {

	private Infrastructure infrastructure;
	
	public static int currentSubversion = 2;

	public static void main(String[] args) {
		System.out.println("[CORE] Starting BusyCloud... Please wait a second.");
		ApplicationInterface applicationInterface = new ApplicationInterface();
		Cloud cloud = new Cloud();
		applicationInterface.initializeInterface(cloud);
		cloud.boot();
	}

	public void boot() {
		infrastructure = new Infrastructure();
		if (new File("local/settings.json").exists()) {
			new Boot();
			return;
		}
		new Setup();
	}

	public Infrastructure getInfrastructure() {
		return infrastructure;
	}

}
