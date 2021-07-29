package eu.busycloud.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Handler;
import java.util.logging.Logger;

import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.Boot;

/**
 * 
 * <h1>BusyCloud</h1>
 * This cloud isn't complete or stable and it contains bugs, I've to fix.
 * But for the first, what is BusyCloud?
 * BusyCloud is an program, that can dynamically deploy Spigotserver oriented software.
 * The main goal is to simplify the work with a Minecraft-Server-Network.
 * 
 * @author PlugDev
 * @version 2
 * 
 */


public class CloudInstance {

	/**
	 * BusyCloud Documentation. This commentary is like an explanation to the Code
	 * and is conductible to the original code.
	 * 
	 * @author PlugDev
	 * @version 1.02
	 * @since 1.02
	 * 
	 */

	public final static Logger LOGGER = Logger.getLogger(CloudInstance.class.getName());

	public static int currentMajorVersion = 2;
	public static int currentMinorVersion = 0;
	public static int currentBuildVersion = 91;
	
	/**
	 *
	 * @since 0.1
	 * @param args Some arguments which the JVM pushs
	 * 
	 */

	public static void main(String[] args) {
		System.setProperty("java.util.logging.SimpleFormatter.format", "BusyCloud-v2@Output: [%1$tT] %5$s%6$s%n");
		for(Handler handler : LOGGER.getHandlers())
			try {
				handler.flush();
				handler.setEncoding("UTF-8");
			} catch (SecurityException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		CloudInstance cloud = new CloudInstance();
		new ApplicationInterface(cloud);
		cloud.boot();
	}

	/**
	 * 
	 * This method starts the Bootsector of BusyCloud. If local/settings.json doesn't exists the
	 * setup-console should starts.
	 * 
	 * @since 0.1
	 */

	public void boot() {
		new Boot(!new File("configurations/cloudconfig.json").exists());
	}

}
