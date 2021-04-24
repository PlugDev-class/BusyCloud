package de.plugdev.cloud;

import java.io.File;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.infrastructure.Boot;
import de.plugdev.cloud.infrastructure.Infrastructure;
import de.plugdev.cloud.infrastructure.Setup;

public class Cloud {

	/*
	 * BusyCloud Documentation.
	 * This commentary is like an explanation to the Code and is
	 * conductible to the original code.
	 * 
	 * @author PlugDev
	 * @version 1.02
	 * @since 1.02
	 * @notdevsave
	 * 
	 */
	
	private Infrastructure infrastructure;
	public static int currentSubversion = 2;
	
	
	/*
	 * @since 0.1
	 * @param args Some arguments which pushs the JVM.
	 * 
	 */

	public static void main(String[] args) {
		System.out.println("[CORE] Starting BusyCloud... Please wait a second.");
		ApplicationInterface applicationInterface = new ApplicationInterface();
		Cloud cloud = new Cloud();
		applicationInterface.initializeInterface(cloud);
		cloud.boot();
	}
	
	/*
	 * 
	 * This method starts the bootstep. If local/settings.json doesn't exists
	 * the setup starts.
	 * 
	 * @since 0.1
	 */
	
	public void boot() {
		infrastructure = new Infrastructure();
		if (new File("local/settings.json").exists()) {
			new Boot();
			return;
		}
		new Setup();
	}

	
	/*
	 * @since 0.1
	 * @return Returns the infrastructure-Instance
	 * 
	 */
	public Infrastructure getInfrastructure() {
		return infrastructure;
	}

}
