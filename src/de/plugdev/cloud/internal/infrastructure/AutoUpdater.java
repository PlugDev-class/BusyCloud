package de.plugdev.cloud.internal.infrastructure;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
		URL spigotBridge = null;
		URL bungeeBridge = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/PlugDev-class/BusyCloud_Info/master/src/de/plugdev/busycloud/version.txt").openStream()));
			String version1 = reader.readLine();
			String version2 = reader.readLine();
			reader.close();
			spigotBridge = new URL("https://github.com/PlugDev-class/BusyCloud_SpigotCloudBridge/releases/download/"
					+ version1 + ".jar");
			bungeeBridge = new URL("https://github.com/PlugDev-class/BusyCloud_BungeeCloudBridge/releases/download/"
					+ version2 + ".jar");
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		FileUtils.deleteFile("backend/downloads/SpigotCloudBridge.jar");
		FileUtils.deleteFile("backend/downloads/BungeeCloudBridge.jar");
		FileUtils.download(spigotBridge.getProtocol() + "://" + spigotBridge.getHost() + spigotBridge.getPath(), "backend/downloads/SpigotCloudBridge.jar");
		FileUtils.download(bungeeBridge.getProtocol() + "://" + bungeeBridge.getHost() + bungeeBridge.getPath(), "backend/downloads/BungeeCloudBridge.jar");
	}

}