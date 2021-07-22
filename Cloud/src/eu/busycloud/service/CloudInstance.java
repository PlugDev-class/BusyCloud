package eu.busycloud.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Handler;
import java.util.logging.Logger;

import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.Boot;

public class CloudInstance {

	/*
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
	public static int currentBuildVersion = 72;
	/*
	 * @since 0.1
	 * 
	 * @param args Some arguments which pushs the JVM.
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

	/*
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
