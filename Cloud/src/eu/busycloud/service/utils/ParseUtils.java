package eu.busycloud.service.utils;

import eu.busycloud.service.CloudInstance;

public class ParseUtils {
	
	public static Integer parseInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception exception) {
			CloudInstance.LOGGER.warning("The given input isn't a valid number: '" + string + "'.");
			return 0;
		}
	}
	
	public static Boolean parseBool(String string, boolean preferredBoolean) {
		try {
			switch (string.toLowerCase()) {
				case "yes":
					return true;
				case "ja":
					return true;
				case "true":
					return true;
				case "no":
					return false;
				case "nein":
					return false;
				case "false":
					return false;
				default:
					CloudInstance.LOGGER.warning("The given input isn't a valid boolean: '" + string + "'.");
					return preferredBoolean;
			}
		} catch (Exception exception) {
			CloudInstance.LOGGER.warning("The given input isn't a valid boolean: '" + string + "'.");
			return preferredBoolean;
		}
	}
	
}
