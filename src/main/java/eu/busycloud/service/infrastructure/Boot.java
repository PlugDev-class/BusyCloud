package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.FileHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonParser;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleInstance;
import eu.busycloud.service.utils.FileUtils;
import eu.busycloud.service.utils.TextUtils;

public class Boot {

	/**
	 * The start and initiating of some elements. (Firstboot)
	 * 
	 * @since 1.02
	 * @recode 2.0
	 * @author PlugDev
	 * @Deprecated -> @throws IOException
	 * @param serverName Used servername for purpose.
	 * @param bungeeCordSoftware BungeeCord version to easily download the version.
	 * @param spigotServerSoftware Used Spigotversion by MinecraftVersion-Instance.
	 * @param useViaVersion If the server want to use ViaVersion.
	 * @param nibblecompression If the server should use nibble-compression
	 * @param maxRam The maxRam used by BusyCloud
	 */

	public Boot(String servername, ServerSoftware bungeeCordSoftware, ServerSoftware spigotServerSoftware, boolean useViaversion, boolean nibblecompression, int maxRam) {

		File localSettingsFile = new File("configurations/cloudconfig.json");
		if (!localSettingsFile.exists())
			try {
				localSettingsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("servername", servername);
		jsonObject.put("maxRam", maxRam);

		JSONObject bungeeCordMotdObject = new JSONObject();
		bungeeCordMotdObject.put("motdPlayerInfo", "§5BusyCloud");
		bungeeCordMotdObject.put("motdLine1", "§cBusyCloud-Service §32");
		bungeeCordMotdObject.put("motdLine2", "§edeveloped by PlugDev");
		bungeeCordMotdObject.put("motdProtocol", "§aPublic-Beta v2");
		
		JSONObject bungeeCordObject = new JSONObject();
		bungeeCordObject.put("maxPlayers", 40);
		bungeeCordObject.put("startport", 25575);
		bungeeCordObject.put("maxRam", 512);
		bungeeCordObject.put("motdSettings", bungeeCordMotdObject);
		
		JSONObject featureObject = new JSONObject();
		featureObject.put("enable-motdModification", true);
		featureObject.put("enable-bungeePermissions", true);
		featureObject.put("nibble-compression", nibblecompression);
		featureObject.put("viaversion", useViaversion);

		JSONArray jvmStartparameter = new JSONArray();
		jvmStartparameter.put("XX:+UseG1GC");
		jvmStartparameter.put("XX:MaxGCPauseMillis=50");
		jvmStartparameter.put("XX:MaxPermSize=256M");
		jvmStartparameter.put("XX:-UseAdaptiveSizePolicy");
		jvmStartparameter.put("XX:CompileThreshold=100");
		jvmStartparameter.put("Dcom.mojang.eula.agree=true");
		jvmStartparameter.put("Dio.netty.recycler.maxCapacity=0");
		jvmStartparameter.put("Dio.netty.recycler.maxCapacity.default=0");
		jvmStartparameter.put("-Xmx%MAX%M");
		jvmStartparameter.put("-jar %VERSION-IN-JAR%.jar");
		
		jsonObject.put("bungeeCord", bungeeCordObject);
		jsonObject.put("features", featureObject);
		jsonObject.put("jvmStartparameter", jvmStartparameter);
		
		FileUtils.writeFile(localSettingsFile, TextUtils.GSON.toJson(JsonParser.parseString(jsonObject.toString())));

		File groupsSettingsFile = new File("configurations/servergroups.json");
		if (!groupsSettingsFile.exists())
			try {
				groupsSettingsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		jsonObject = new JSONObject();
		
		Map<String, Object> lobbyServer = new LinkedHashMap<String, Object>();
		lobbyServer.put("serverSoftware", "Spigot-1.8.8");
		lobbyServer.put("startPort", 25580);
		lobbyServer.put("groupId", 1);
		lobbyServer.put("maxRamEachServer", 256);
		lobbyServer.put("startServerByGroupstart", 3);
		lobbyServer.put("startNewServerByPercentage", 75);
		lobbyServer.put("lobbyState", true);
		jsonObject.put("Lobby", lobbyServer);
		FileUtils.writeFile(groupsSettingsFile, TextUtils.GSON.toJson(JsonParser.parseString(jsonObject.toString())));
		
		if(bungeeCordSoftware != null)
			bungeeCordSoftware.download();
		if(spigotServerSoftware != null)
			spigotServerSoftware.download();
		ApplicationInterface.getAPI().getInfrastructure().checkVersions();
		ApplicationInterface.getAPI().getInfrastructure().useViaVersion = useViaversion;
		new ConsoleInstance(false);

		ApplicationInterface.getAPI().getInfrastructure().startServerGroup("Lobby");
	}

	/**
	 * The start and initiating of some elements.
	 * 
	 * @since 1.02
	 * @author PlugDev
	 * @throws IOException
	 */
	

	@SuppressWarnings("deprecation")
	public Boot(boolean startsWithSetup) {
		if(startsWithSetup)
			new ConsoleInstance(startsWithSetup);
				
		FileUtils.deleteFolderRecursivly("server/temp");
		FileUtils.mkdirs("server/temp");

		new AutoUpdater(false, false);
		try {
			JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(new File("configurations/cloudconfig.json").toPath()), "UTF-8"));
			ApplicationInterface.getAPI().getInfrastructure().useViaVersion = jsonObject.getJSONObject("features").getBoolean("viaversion");
			ApplicationInterface.getAPI().getInfrastructure().serverName = jsonObject.getString("servername");
			ApplicationInterface.getAPI().getInfrastructure().maxRam = jsonObject.getInt("maxRam");
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

		Arrays.asList(new File("server/static").listFiles()).forEach((root) -> {
			String version = "";
			for (File subroot : new File(root.getAbsolutePath()).listFiles())
				if (subroot.getName().endsWith(".jar"))
					version = subroot.getName().replaceAll(".jar", "");
			SpigotServer server;
			server = new SpigotServer();
			ApplicationInterface.getAPI().getInfrastructure().getRunningServers().add(server);
			server.startStaticServer(root.getName(),
					ApplicationInterface.getAPI().getInfrastructure().getVersionById(version), true, 512);
		});

		
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			Path path = Paths.get("developer/logs", format.format(new Date(System.currentTimeMillis())) + ".log");
			if (!Files.exists(path.getParent()))
	            Files.createDirectory(path.getParent());
			CloudInstance.LOGGER.addHandler(new FileHandler("developer/logs/" + format.format(new Date(System.currentTimeMillis())) + ".log", true));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		new ConsoleInstance(startsWithSetup);
	}

}
