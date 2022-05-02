package de.plugdev.cloud.internal.infrastructure;

import java.net.HttpURLConnection;
import java.net.URL;

import de.plugdev.cloud.CloudInstance;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.utils.FileUtils;

public class AutoUpdater {

	/*
	 * This method checks if a new version is available.
	 * @since 1.02
	 * @throws IOException
	 */
	public void doCloud() {
		try {
			int subversion = 0;
			boolean continueState = true;
			while (continueState) {
				URL url = new URL("https://github.com/PlugDev-class/BusyCloud/releases/download/1.0" + subversion++ + "/Cloud.jar");
				HttpURLConnection huc = (HttpURLConnection) url.openConnection();
				huc.setRequestMethod("HEAD");
				huc.connect();
				int responseCode = huc.getResponseCode();
				huc.disconnect();
				if (responseCode != HttpURLConnection.HTTP_OK) {
					if(CloudInstance.currentSubversion < subversion) {
						ConsoleOutput.write(ConsoleOutput.YELLOW, "[UPDATER] A new update is available! Update it to be on the safe side!");
						ConsoleOutput.write(ConsoleOutput.YELLOW, "[UPDATER] New Version: 1.0" + subversion-- + " | Current version: 1.0" + CloudInstance.currentSubversion);
						ConsoleOutput.write(ConsoleOutput.YELLOW, "[UPDATER] Download: https://github.com/PlugDev-class/BusyCloud/releases/download/1.0" + subversion +"/Cloud.jar");
//						ApplicationInterface.getAPI().getGuiInterface().pushStatusMessage("[CORE] New Cloudupdate found.");
					} else {
						ConsoleOutput.write(ConsoleOutput.GREEN, "[UPDATER] You're up-to-date!"  + (CloudInstance.currentSubversion > subversion ? " (Maybe higher. - Unstablewarning)" :""));
//						ApplicationInterface.getAPI().getGuiInterface().pushStatusMessage("[CORE] No Cloudupdate found. You're up-to-date!");
					}
					continueState = false;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/*
	 * This method autodownloads the 
	 * spigotcloudbridge and the bungeecloudbridge.
	 * @since 1.02
	 * @throws IOException
	 */
	public void doBridges() {
//		ApplicationInterface.getAPI().getGuiInterface().pushStatusMessage("[CORE] Starting Bridgeupdater");
		@SuppressWarnings("unused")
		URL spigotBridge = null;
		URL bungeeBridge = null;
		try {
			int spigotSubversion = 0;
			spigotBridge = new URL("https://github.com/PlugDev-class/BusyCloud_SpigotCloudBridge/releases/download/1.0"
					+ spigotSubversion + "/SpigotCloudBridge.jar");

			boolean continueState = true;
			while (continueState) {
				URL url = new URL("https://github.com/PlugDev-class/BusyCloud_SpigotCloudBridge/releases/download/1.0"
						+ spigotSubversion++ + "/SpigotCloudBridge.jar");
				HttpURLConnection huc = (HttpURLConnection) url.openConnection();
				huc.setRequestMethod("HEAD");
				huc.connect();
				int responseCode = huc.getResponseCode();
				huc.disconnect();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					spigotBridge = url;
				} else {
					continueState = false;
					break;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		try {
			int bungeeSubversion = 0;
			bungeeBridge = new URL("https://github.com/PlugDev-class/BusyCloud_BungeeCloudBridge/releases/download/1.0"
					+ bungeeSubversion + "/BungeeCloudBridge.jar");
			boolean continueState = true;
			while (continueState) {
				URL url = new URL("https://github.com/PlugDev-class/BusyCloud_BungeeCloudBridge/releases/download/1.0"
						+ bungeeSubversion++ + "/BungeeCloudBridge.jar");
				HttpURLConnection huc = (HttpURLConnection) url.openConnection();
				huc.setRequestMethod("HEAD");
				huc.connect();
				int responseCode = huc.getResponseCode();
				huc.disconnect();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					bungeeBridge = url;
				} else {
					continueState = false;
					break;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		FileUtils.deleteFile("backend/downloads/SpigotCloudBridge.jar");
		FileUtils.deleteFile("backend/downloads/BungeeCloudBridge.jar");
		FileUtils.download("https://github.com/BusyCloud-Service/BusyCloud_SpigotCloudBridge/releases/download/fork-1.0/BusyCloud-Spigot.jar", "backend/downloads/SpigotCloudBridge.jar");
//		ApplicationInterface.getAPI().getGuiInterface().pushStatusMessage("[CORE] Downloading SpigotCloudBridge.jar");
		FileUtils.download(bungeeBridge.getProtocol() + "://" + bungeeBridge.getHost() + bungeeBridge.getPath(), "backend/downloads/BungeeCloudBridge.jar");
//		ApplicationInterface.getAPI().getGuiInterface().pushStatusMessage("[CORE] Downloading BungeeCloudBridge.jar");
	}

}