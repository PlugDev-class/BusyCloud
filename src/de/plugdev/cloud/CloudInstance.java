package de.plugdev.cloud;

import java.io.File;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.infrastructure.Boot;
import de.plugdev.cloud.internal.infrastructure.Setup;
import de.plugdev.cloud.lang.ApiStatus.Internal;

public class CloudInstance {

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
	
	public static int currentSubversion = 3;
	
	
	/*
	 * @since 0.1
	 * @param args Some arguments which pushs the JVM.
	 * 
	 */

	@Internal
	public static void main(String[] args) {
		System.out.println("[CORE] Starting BusyCloud... Please wait a second.");
		ApplicationInterface applicationInterface = new ApplicationInterface();
		CloudInstance cloud = new CloudInstance();
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
	
	@Internal
	public void boot() {
		if (new File("local/settings.json").exists()) {
			new Boot();
			return;
		}
		new Setup();
	}

}
