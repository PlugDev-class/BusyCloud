package eu.busycloud.service.utils;

import eu.busycloud.service.CloudInstance;

public class ParseUtils {
	
	/**
	 * 
	 * This method converts a string like a numeric input
	 * to a Integer-Object via {@link Integer#parseInt(String)}
	 * 
	 * @param string The given input
	 * @return int The expected/converted output
	 * @since 2.0
	 */
	
	public static Integer parseInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception exception) {
			CloudInstance.LOGGER.warning("The given input isn't a valid number: '" + string + "'.");
			return -Integer.MAX_VALUE;
		}
	}
	
	/**
	 * 
	 * This method converts a string like a boolic input
	 * to a Boolean-Object via {@link Boolean#parseBoolean(String)}
	 * 
	 * @param string The given input
	 * @param preferredBoolean The preferred output if exception throws
	 * @return bool The expected/converted output
	 * @since 2.0
	 */
	
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
