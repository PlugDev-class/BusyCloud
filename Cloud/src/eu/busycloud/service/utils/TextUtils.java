package eu.busycloud.service.utils;

import eu.busycloud.service.CloudInstance;

public class TextUtils {

	public static void clearScreen() {
		for (int i = 0; i < 300; i++)
			sendSpaceX3();
	}

	public static void sendHeader() {
		CloudInstance.LOGGER.info("==============================================================");
		CloudInstance.LOGGER.info("  ____                   _____ _                 _ ");
		CloudInstance.LOGGER.info(" |  _ \\                 / ____| |               | |");
		CloudInstance.LOGGER.info(" | |_) |_   _ ___ _   _| |    | | ___  _   _  __| |");
		CloudInstance.LOGGER.info(" |  _ <| | | / __| | | | |    | |/ _ \\| | | |/ _` |");
		CloudInstance.LOGGER.info(" | |_) | |_| \\__ \\ |_| | |____| | (_) | |_| | (_| |");
		CloudInstance.LOGGER.info(" |____/ \\__,_|___/\\__, |\\_____|_|\\___/ \\__,_|\\__,_|");
		CloudInstance.LOGGER.info("                   __/ |                           ");
		CloudInstance.LOGGER.info("                  |___/                            ");
		CloudInstance.LOGGER.info("--------------------------------------------------------------");
		CloudInstance.LOGGER.info("Developed by PlugDev | v" + CloudInstance.currentMajorVersion + "."
				+ CloudInstance.currentMinorVersion + " | Build: " + CloudInstance.currentBuildVersion);
		CloudInstance.LOGGER.info("--------------------------------------------------------------");
		CloudInstance.LOGGER.info("Help? -> type '/help' to get more help.");
		CloudInstance.LOGGER.info("==============================================================");
	}

	public static void sendSpaceX3() {
		CloudInstance.LOGGER.info(" ");
		CloudInstance.LOGGER.info(" ");
		CloudInstance.LOGGER.info(" ");
	}

}
