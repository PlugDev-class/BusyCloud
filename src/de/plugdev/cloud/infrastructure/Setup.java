package de.plugdev.cloud.infrastructure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.api.ServerGroup;
import de.plugdev.cloud.console.ConsoleInstance;

public class Setup {

	@SuppressWarnings("unchecked")
	public Setup(boolean agreeLicensement, boolean agreeStatistics, String servername, int ramUsageInMbyte,
			String optimizationType, MinecraftVersion bungeeCordType, String spigotType,
			MinecraftVersion spigotServerVersion, boolean useViaversion) throws IOException {

		File backendFolder = new File("backend");
		File backendBackupsFolder = new File("backend/backups");
		File backendDownloadsFolder = new File("backend/downloads");
		File backendTemplatesFolder = new File("backend/templates");

		File localFolder = new File("local");
		File localPermissionsFolder = new File("local/permissions");

		File serverFolder = new File("server");
		File serverLobbyFolder = new File("server/lobby");
		File serverStaticFolder = new File("server/static");
		File serverTempFolder = new File("server/temp");
		
		checkFolder(backendFolder);
		checkFolder(backendBackupsFolder);
		checkFolder(backendDownloadsFolder);
		checkFolder(backendTemplatesFolder);
		checkFolder(localFolder);
		checkFolder(localPermissionsFolder);
		checkFolder(serverFolder);
		checkFolder(serverLobbyFolder);
		checkFolder(serverStaticFolder);
		checkFolder(serverTempFolder);

		File localSettingsFile = new File("local/settings.json");
		if (!localSettingsFile.exists()) {
			localSettingsFile.createNewFile();
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("servername", servername);
		jsonObject.put("ramUsage", ramUsageInMbyte);
		jsonObject.put("optimizationType", optimizationType);
		jsonObject.put("bungeeCoreType", bungeeCordType.getVersion());
		jsonObject.put("spigotType", spigotType);
		jsonObject.put("spigotVersion", spigotServerVersion.getVersion());
		jsonObject.put("useViaVersion", useViaversion);

		writeFile(localSettingsFile, jsonObject.toJSONString());
		
		File groupsSettingsFile = new File("local/groups.pdv");
		if (!groupsSettingsFile.exists()) {
			groupsSettingsFile.createNewFile();
		}

		StringBuilder builder = new StringBuilder();
		builder.append("#################################################\n");
		builder.append("# T h a n k  y o u  f o r  u s i n g  BusyCloud #\n");
		builder.append("#################################################\n");
		builder.append("#        Please use the command \"/group\"        #\n");
		builder.append("#         (Instead of editing this file)        #\n");
		builder.append("#################################################\n");
		builder.append("Lobby | 20000 | 512 | 33000 | 3 | 50 | " + spigotServerVersion.getVersion() + " | yes\n");
		writeFile(groupsSettingsFile, builder.toString());

		bungeeCordType.install();
		spigotServerVersion.install();
		
		ApplicationInterface.getAPI().getCloud().getInfrastructure().checkVersions();
		
		download("https://github.com/PlugDev-class/BusyCloud_BungeeCloudBridge/releases/download/1.01/BungeeCloudBridge.jar", "backend/downloads/BungeeCloudBridge.jar");
		download("https://github.com/PlugDev-class/BusyCloud_SpigotCloudBridge/releases/download/1.01/SpigotCloudBridge.jar", "backend/downloads/SpigotCloudBridge.jar");
		
		new ConsoleInstance();
		ApplicationInterface.getAPI().getCloud().getInfrastructure().useViaVersion = useViaversion;
		ApplicationInterface.getAPI().getCloud().ressourceScanner.close();
		
		ServerGroup lobbyGroup = new ServerGroup(ApplicationInterface.getAPI().getInfrastructure().getVersionById("spigot-1.8.8"), 33000, "Lobby", 20000, null, 512, 3, 50, true);
		ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(lobbyGroup);
		
	}

	public Setup() throws IOException {
		File localSettingsFile = new File("local/settings.json");
		if (!localSettingsFile.exists()) {
			localSettingsFile.createNewFile();
		}
		
		File groupsSettingsFile = new File("local/groups.pdv");
		if (!groupsSettingsFile.exists()) {
			groupsSettingsFile.createNewFile();
		}
		
		download("https://github.com/PlugDev-class/BusyCloud_BungeeCloudBridge/releases/download/1.01/BungeeCloudBridge.jar", "backend/downloads/BungeeCloudBridge.jar");
		download("https://github.com/PlugDev-class/BusyCloud_SpigotCloudBridge/releases/download/1.01/SpigotCloudBridge.jar", "backend/downloads/SpigotCloudBridge.jar");

		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("local/settings.json"));
			ApplicationInterface.getAPI().getCloud().getInfrastructure().useViaVersion = (Boolean) jsonObject.get("useViaVersion");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		new ConsoleInstance();
		if (ApplicationInterface.getAPI().getCloud().ressourceScanner != null) {
			ApplicationInterface.getAPI().getCloud().ressourceScanner.close();
		}
		
		
	}

	public void writeFile(File file, String string) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(string);
			writer.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void checkFolder(File file) {
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public void download(String url, String path) {
		System.out.print("[SETUP] Download \"" + url + "\" started");
		try {
			Files.copy(new URL(url).openStream(), Paths.get(path));
			System.out.print("   .. | ..   done");
		} catch (IOException e) {
			System.out.print("   .. | ..   failed");
		}
		System.out.println(" ");
	}

}
