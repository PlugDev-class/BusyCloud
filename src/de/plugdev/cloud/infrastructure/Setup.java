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
import de.plugdev.cloud.console.ConsoleInput;

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
		builder.append("#  Please use /groupadd, /groupdel, /groupedit  #\n");
		builder.append("#         (Instead of editing this file)        #\n");
		builder.append("#################################################\n");
		builder.append("Lobby | 20000 | 512 | 33000 | 3 | 50 | spigot-1.8.8 | yes\n");
		writeFile(groupsSettingsFile, builder.toString());
		
		download(bungeeCordType.getDownloadURL(), "backend/downloads/" + bungeeCordType.getVersion() + ".jar");
		download(spigotServerVersion.getDownloadURL(),
				"backend/downloads/" + spigotServerVersion.getVersion() + ".jar");
		if (useViaversion) {
			download("https://github.com/ViaVersion/ViaVersion/releases/download/3.2.1/ViaVersion-3.2.1.jar",
					"backend/downloads/ViaVersion-3.2.1.jar");
		}
		
		ApplicationInterface.getAPI().getCloud().getInfrastructure().checkVersions();
		
		new ConsoleInput();
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

		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("local/settings.json"));
			ApplicationInterface.getAPI().getCloud().getInfrastructure().useViaVersion = (Boolean) jsonObject.get("useViaVersion");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		new ConsoleInput();
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
