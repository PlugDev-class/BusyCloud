package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleInstance;
import eu.busycloud.service.utils.FileUtils;

public class Boot {

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/*
	 * The start and initiating of some elements. (Firstboot)
	 * 
	 * @since 1.02
	 * @author PlugDev
	 * @throws IOException
	 * @param agreeLicensement Agree BusyCloud-Licensement.
	 * @param agreeStatistics Agree some unused statistics.
	 * @param serverName Used servername for purpose.
	 * @param optimizationType Maybe later used optimizationType. Reserved for
	 * furtureuse.
	 * @param bungeeCordType BungeeCord version to easily download the version.
	 * @param spigotFork Used Spigotfork for future use.
	 * @param spigotServerVersion Used Spigotversion by MinecraftVersion-Instance.
	 * @param useViaVersion If the server want to use ViaVersion.
	 * @param ressourceScanner Used Scanner to close quietly without canceling the
	 * task.
	 */

	public Boot(String servername, ServerSoftware bungeeCord, ServerSoftware spigotServer, boolean useViaversion)
			throws IOException {

		File localSettingsFile = new File("configurations/cloudconfig.json");
		if (!localSettingsFile.exists())
			localSettingsFile.createNewFile();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("servername", servername);
		jsonObject.put("useViaVersion", useViaversion);
		jsonObject.put("bungeeCord.maxPlayers", 40);
		jsonObject.put("bungeeCord.motdPlayerInfo", "§5BusyCloud");
		jsonObject.put("bungeeCord.motdLine1", "§cBusyCloud-Service §3²");
		jsonObject.put("bungeeCord.motdLine2", "§edeveloped by PlugDev");
		jsonObject.put("bungeeCord.motdProtocol", "§aPublic-Beta v2");
		FileUtils.writeFile(localSettingsFile, gson.toJson(JsonParser.parseString(jsonObject.toString())));

		File groupsSettingsFile = new File("configurations/servergroups.json");
		if (!groupsSettingsFile.exists())
			groupsSettingsFile.createNewFile();
		jsonObject = new JSONObject();
		
		Map<String, Object> lobbyServer = new HashMap<String, Object>();
		lobbyServer.put("serverSoftware", "Spigot-1.8.8");
		lobbyServer.put("startPort", 25580);
		lobbyServer.put("groupId", 1);
		lobbyServer.put("maxRamEachServer", 256);
		lobbyServer.put("startServerByGroupstart", 3);
		lobbyServer.put("startNewServerByPercentage", 75);
		lobbyServer.put("lobbyState", true);
		jsonObject.put("Lobby", lobbyServer);
		FileUtils.writeFile(groupsSettingsFile, gson.toJson(JsonParser.parseString(jsonObject.toString())));
		
		if(bungeeCord != null)
			bungeeCord.download();
		if(spigotServer != null)
			spigotServer.download();
		ApplicationInterface.getAPI().getInfrastructure().checkVersions();
		ApplicationInterface.getAPI().getInfrastructure().useViaVersion = useViaversion;
		new ConsoleInstance(false);

		ApplicationInterface.getAPI().getInfrastructure().startServerGroup("Lobby");
	}

	/*
	 * The start and initiating of some elements.
	 * 
	 * @since 1.02
	 * @author PlugDev
	 * @throws IOException
	 */
	
	
	public Boot(boolean startsWithSetup) {
		if(startsWithSetup)
			new ConsoleInstance(startsWithSetup);
				
		FileUtils.deleteFolderRecursivly("server/temp");
		FileUtils.mkdirs("server/temp");
		
		new AutoUpdater(false, false);
		try {
			JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(new File("configurations/cloudconfig.json").toPath()), "UTF-8"));
			ApplicationInterface.getAPI().getInfrastructure().useViaVersion = jsonObject.getBoolean("useViaVersion");
			ApplicationInterface.getAPI().getInfrastructure().serverName = jsonObject.getString("servername");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		try {
			JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(new File("configurations/servergroups.json").toPath()), "UTF-8"));
			for(String key : jsonObject.keySet()) {
				ApplicationInterface.getAPI().getInfrastructure().startServerGroup(key);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		if(!new File("server/static").exists()) {
			new ConsoleInstance(startsWithSetup);
			return;
		}
		if (new File("server/static").listFiles().length == 0) {
			new ConsoleInstance(startsWithSetup);
			return;
		}

		for (File root : new File("server/static").listFiles()) {
			String version = "";
			for (File subroot : new File(root.getAbsolutePath()).listFiles())
				if (subroot.getName().endsWith(".jar"))
					version = subroot.getName().replaceAll(".jar", "");
			SpigotServer server;
			server = new SpigotServer();
			ApplicationInterface.getAPI().getInfrastructure().getRunningServers().add(server);
			server.startStaticServer(root.getName(),
					ApplicationInterface.getAPI().getInfrastructure().getVersionById(version), true, 512);
		}

		new ConsoleInstance(startsWithSetup);
	}

}
