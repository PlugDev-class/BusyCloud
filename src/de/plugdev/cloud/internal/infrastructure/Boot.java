package de.plugdev.cloud.internal.infrastructure;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.ServerGroup;
import de.plugdev.cloud.internal.console.ConsoleInstance;
import de.plugdev.cloud.internal.models.IVersion;
import de.plugdev.cloud.internal.utils.FileUtils;

public class Boot {

	/*
	 * The start and initiating of some elements. (Firstboot)
	 * @since 1.02
	 * @author PlugDev
	 * @throws IOException
	 * @param agreeLicensement Agree BusyCloud-Licensement.
	 * @param agreeStatistics Agree some unused statistics.
	 * @param serverName Used servername for purpose.
	 * @param optimizationType Maybe later used optimizationType. Reserved for furtureuse.
	 * @param bungeeCordType BungeeCord version to easily download the version.
	 * @param spigotFork Used Spigotfork for futureuse.
	 * @param spigotServerVersion Used Spigotversion by MinecraftVersion-Instance.
	 * @param useViaVersion If the server want to use ViaVersion.
	 * @param ressourceScanner Used Scanner to close quitly without cancelling the task.
	 */
	
	@SuppressWarnings("unchecked")
	public Boot(boolean agreeLicensement, boolean agreeStatistics, String servername,
			String optimizationType, IVersion bungeeCordType, String spigotfork,
			IVersion spigotServerVersion, boolean useViaversion, Scanner ressourceScanner) throws IOException {

		File backendFolder = new File("backend");
		File backendBackupsFolder = new File("backend/backups");
		File backendDownloadsFolder = new File("backend/downloads");
		File backendTemplatesFolder = new File("backend/templates");

		File localFolder = new File("local");
		File localPermissionsFolder = new File("local/permissions");

		File serverFolder = new File("server");
		File serverStaticFolder = new File("server/static");
		File serverTempFolder = new File("server/temp");
		
		FileUtils.checkFolder(backendFolder);
		FileUtils.checkFolder(backendBackupsFolder);
		FileUtils.checkFolder(backendDownloadsFolder);
		FileUtils.checkFolder(backendTemplatesFolder);
		FileUtils.checkFolder(localFolder);
		FileUtils.checkFolder(localPermissionsFolder);
		FileUtils.checkFolder(serverFolder);
		FileUtils.checkFolder(serverStaticFolder);
		FileUtils.checkFolder(serverTempFolder);

		File localSettingsFile = new File("local/settings.json");
		if (!localSettingsFile.exists()) {
			localSettingsFile.createNewFile();
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("servername", servername);
		jsonObject.put("optimizationType", optimizationType);
		jsonObject.put("bungeeCoreType", bungeeCordType.getVersion());
		jsonObject.put("spigotFork", spigotfork);
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
		builder.append("Lobby | " + UUID.randomUUID() + " | 512 | 25579 | 3 | 50 | " + spigotServerVersion.getVersion() + " | yes\n");
		FileUtils.writeFile(groupsSettingsFile, builder.toString());

		File localPermissionsGroupsFile = new File("local/permissions/groups.pdv");
		File localPermissionsUsersFile = new File("local/permissions/users.pdv");
		if(!localPermissionsGroupsFile.exists()) {
			localPermissionsGroupsFile.createNewFile();
		}
		if(!localPermissionsUsersFile.exists()) {
			localPermissionsUsersFile.createNewFile();
		}
		
		builder = new StringBuilder();
		builder.append("#################################################\n");
		builder.append("# T h a n k  y o u  f o r  u s i n g  BusyCloud #\n");
		builder.append("#################################################\n");
		builder.append("#        Please use the command \"/perms\"        #\n");
		builder.append("#         (Instead of editing this file)        #\n");
		builder.append("#################################################\n");
		builder.append("01 | admin | §cAdmin - | §cTeam | *,debug.* | rffu | rffu | rffu | rffu | rffu\n");
		builder.append("02 | default | §7Player - | §7Player | lobby.use,lobby.interact | rffu | rffu | rffu | rffu | rffu\n");
		FileUtils.writeFile(localPermissionsGroupsFile, builder.toString());
		
		builder = new StringBuilder();
		builder.append("#################################################\n");
		builder.append("# T h a n k  y o u  f o r  u s i n g  BusyCloud #\n");
		builder.append("#################################################\n");
		builder.append("#        Please use the command \"/perms\"        #\n");
		builder.append("#         (Instead of editing this file)        #\n");
		builder.append("#################################################\n");
		builder.append("0f30fb39bdef403e991440e25aa4a8b0 | 01\n");
		FileUtils.writeFile(localPermissionsUsersFile, builder.toString());
		
		
		bungeeCordType.download();
		spigotServerVersion.download();
		
		ApplicationInterface.getAPI().getInfrastructure().loadVersions();
		
		FileUtils.download("https://github.com/PlugDev-class/BusyCloud_BungeeCloudBridge/releases/download/1.01/BungeeCloudBridge.jar", "backend/downloads/BungeeCloudBridge.jar");
		FileUtils.download("https://github.com/PlugDev-class/BusyCloud_SpigotCloudBridge/releases/download/1.01/SpigotCloudBridge.jar", "backend/downloads/SpigotCloudBridge.jar");
		FileUtils.download("https://github.com/ViaVersion/ViaVersion/releases/download/3.2.1/ViaVersion-3.2.1.jar", "backend/downloads/ViaVersion-3.2.1.jar");
		
		new ConsoleInstance();
		ApplicationInterface.getAPI().getInfrastructure().useViaVersion = useViaversion;
		ressourceScanner.close();
		
		ServerGroup lobbyGroup = new ServerGroup(UUID.randomUUID(), "Lobby", 512, ApplicationInterface.getAPI().getInfrastructure().getVersionById(spigotServerVersion.getVersion()).get(), 20000, true, ApplicationInterface.getAPI().getInfrastructure().getProxy(), 1, 80);
		lobbyGroup.init();
		ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(lobbyGroup);
	}
	
	/*
	 * The start and initiating of some elements.
	 * @since 1.02
	 * @author PlugDev
	 * @throws IOException
	 */
	public Boot() {
		FileUtils.deleteFolderRecursivly("server/temp");
		FileUtils.mkdirs("server/temp");
		
		AutoUpdater updater = new AutoUpdater();
		updater.doCloud();
		updater.doBridges();
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("local/settings.json"));
			ApplicationInterface.getAPI().getInfrastructure().useViaVersion = (Boolean) jsonObject.get("useViaVersion");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		new ConsoleInstance();
	}

}
