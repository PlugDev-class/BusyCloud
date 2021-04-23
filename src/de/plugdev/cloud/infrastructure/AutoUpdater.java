package de.plugdev.cloud.infrastructure;

import java.net.HttpURLConnection;
import java.net.URL;

import de.plugdev.cloud.Cloud;
import de.plugdev.cloud.console.ConsoleOutput;
import de.plugdev.cloud.utils.FileUtils;

public class AutoUpdater {

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
					if(Cloud.currentSubversion < subversion) {
						ConsoleOutput.write(ConsoleOutput.YELLOW, "[UPDATER] A new update is available! Update it to be on the safe side!");
						ConsoleOutput.write(ConsoleOutput.YELLOW, "[UPDATER] New Version: 1.0" + subversion-- + " | Current version: 1.0" + Cloud.currentSubversion);
						ConsoleOutput.write(ConsoleOutput.YELLOW, "[UPDATER] Download: https://github.com/PlugDev-class/BusyCloud/releases/download/1.0" + subversion +"/Cloud.jar");
					} else {
						ConsoleOutput.write(ConsoleOutput.GREEN, "[UPDATER] You're up-to-date!");
					}
					continueState = false;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void doBridges() {
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

		FileUtils.download(spigotBridge.getProtocol() + "://" + spigotBridge.getHost() + spigotBridge.getPath(), "backend/downloads/SpigotCloudBridge.jar");
		FileUtils.download(bungeeBridge.getProtocol() + "://" + bungeeBridge.getHost() + bungeeBridge.getPath(), "backend/downloads/BungeeCloudBridge.jar");
	}

}