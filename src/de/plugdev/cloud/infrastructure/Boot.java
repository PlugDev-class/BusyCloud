package de.plugdev.cloud.infrastructure;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.api.ServerGroup;
import de.plugdev.cloud.console.ConsoleInstance;
import de.plugdev.cloud.utils.FileUtils;

public class Boot {

	@SuppressWarnings("unchecked")
	public Boot(boolean agreeLicensement, boolean agreeStatistics, String servername, int ramUsageInMbyte,
			String optimizationType, MinecraftVersion bungeeCordType, String spigotType,
			MinecraftVersion spigotServerVersion, boolean useViaversion, Scanner ressourceScanner) throws IOException {

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
		
		FileUtils.checkFolder(backendFolder);
		FileUtils.checkFolder(backendBackupsFolder);
		FileUtils.checkFolder(backendDownloadsFolder);
		FileUtils.checkFolder(backendTemplatesFolder);
		FileUtils.checkFolder(localFolder);
		FileUtils.checkFolder(localPermissionsFolder);
		FileUtils.checkFolder(serverFolder);
		FileUtils.checkFolder(serverLobbyFolder);
		FileUtils.checkFolder(serverStaticFolder);
		FileUtils.checkFolder(serverTempFolder);

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

		FileUtils.writeFile(localSettingsFile, jsonObject.toJSONString());
		
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
		builder.append("Lobby | 20000 | 512 | 25579 | 3 | 50 | " + spigotServerVersion.getVersion() + " | yes\n");
		FileUtils.writeFile(groupsSettingsFile, builder.toString());

		bungeeCordType.install();
		spigotServerVersion.install();
		
		ApplicationInterface.getAPI().getCloud().getInfrastructure().checkVersions();
		
		FileUtils.download("https://github.com/PlugDev-class/BusyCloud_BungeeCloudBridge/releases/download/1.01/BungeeCloudBridge.jar", "backend/downloads/BungeeCloudBridge.jar");
		FileUtils.download("https://github.com/PlugDev-class/BusyCloud_SpigotCloudBridge/releases/download/1.01/SpigotCloudBridge.jar", "backend/downloads/SpigotCloudBridge.jar");
		FileUtils.download("https://github.com/ViaVersion/ViaVersion/releases/download/3.2.1/ViaVersion-3.2.1.jar", "backend/downloads/ViaVersion-3.2.1.jar");
		
		new ConsoleInstance();
		ApplicationInterface.getAPI().getCloud().getInfrastructure().useViaVersion = useViaversion;
		ressourceScanner.close();
		
		ServerGroup lobbyGroup = new ServerGroup(ApplicationInterface.getAPI().getInfrastructure().getVersionById(spigotServerVersion.getVersion()), 33000, "Lobby", 20000, null, 512, 3, 50, true);
		ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(lobbyGroup);
		
	}

	public Boot() {
		FileUtils.deleteFolderRecursivly("server/temp");
		FileUtils.mkdirs("serve/temp");
		
		AutoUpdater updater = new AutoUpdater();
		updater.doCloud();
		updater.doBridges();
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("local/settings.json"));
			ApplicationInterface.getAPI().getCloud().getInfrastructure().useViaVersion = (Boolean) jsonObject.get("useViaVersion");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		new ConsoleInstance();
	}

}
