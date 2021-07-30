package eu.busycloud.service.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.busycloud.service.CloudInstance;

public class TextUtils {

	public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().disableInnerClassSerialization().setPrettyPrinting().create();
	
	/**
	 * 
	 * This method sends 300 messages with an empty input.
	 * 
	 * @since 2.0
	 * 
	 */
	
	public static void clearScreen() {
		for (int i = 0; i < 300; i++)
			System.out.println(" ");
	}
	
	
	/**
	 * 
	 * This method sends a 62 character length line of "="
	 * 
	 * @since 2.0
	 * 
	 */
	public static void sendFatLine() {
		CloudInstance.LOGGER.info("==============================================================");
	}
	
	/**
	 * 
	 * This method sends a 62 character length line of "-"
	 * 
	 * @since 2.0
	 * 
	 */
	public static void sendLine() {
		CloudInstance.LOGGER.info("--------------------------------------------------------------");
	}

	/**
	 * 
	 * This method sends a BusyCloud-PlainHeader
	 * 
	 * @since 2.0
	 * 
	 */
	public static void sendPlainHeader() {
		sendFatLine();
		CloudInstance.LOGGER.info("  ____                   _____ _                 _ ");
		CloudInstance.LOGGER.info(" |  _ \\                 / ____| |               | |");
		CloudInstance.LOGGER.info(" | |_) |_   _ ___ _   _| |    | | ___  _   _  __| |");
		CloudInstance.LOGGER.info(" |  _ <| | | / __| | | | |    | |/ _ \\| | | |/ _` |");
		CloudInstance.LOGGER.info(" | |_) | |_| \\__ \\ |_| | |____| | (_) | |_| | (_| |");
		CloudInstance.LOGGER.info(" |____/ \\__,_|___/\\__, |\\_____|_|\\___/ \\__,_|\\__,_|");
		CloudInstance.LOGGER.info("                   __/ |                           ");
		CloudInstance.LOGGER.info("                  |___/                            ");
		sendLine();
	}
	
	/**
	 * 
	 * This method sends a BusyCloud-PlainHeader with some information about it.
	 * 
	 * @since 2.0
	 * 
	 */
	public static void sendHeader() {
		sendPlainHeader();
		CloudInstance.LOGGER.info("Developed by PlugDev | v" + CloudInstance.currentMajorVersion + "."
				+ CloudInstance.currentMinorVersion + " | Build: " + CloudInstance.currentBuildVersion);
		sendLine();
		CloudInstance.LOGGER.info("Help? -> type '/help' to get more help.");
		CloudInstance.LOGGER.info("Introduction? -> type '/introduction' to get more help with setup.");
		sendFatLine();
	}

}
