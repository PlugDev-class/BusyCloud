package eu.busycloud.service.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.busycloud.service.CloudInstance;

public class TextUtils {

	public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().disableInnerClassSerialization().setPrettyPrinting().create();
	
	public static void clearScreen() {
		for (int i = 0; i < 300; i++)
			System.out.println(" ");
	}
	
	public static void sendFatLine() {
		CloudInstance.LOGGER.info("==============================================================");
	}
	public static void sendLine() {
		CloudInstance.LOGGER.info("--------------------------------------------------------------");
	}

	public static void sendPainHeader() {
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
	
	public static void sendHeader() {
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
		CloudInstance.LOGGER.info("Developed by PlugDev | v" + CloudInstance.currentMajorVersion + "."
				+ CloudInstance.currentMinorVersion + " | Build: " + CloudInstance.currentBuildVersion);
		sendLine();
		CloudInstance.LOGGER.info("Help? -> type '/help' to get more help.");
		sendFatLine();
	}

	public static void sendSpaceX3() {
		for (int i = 0; i < 3; i++)
			System.out.println(" ");
	}

}
