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
import eu.busycloud.service.utils.BootContainer;
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
	 * @param bootContainer
	 */

	public Boot(BootContainer bootContainer) {
		File localSettingsFile = new File("configurations/cloudconfig.json");
		if (!localSettingsFile.exists())
			try {
				localSettingsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("servername", bootContainer.getNetworkName());
		jsonObject.put("maxRam", bootContainer.getMaxRam());
		jsonObject.put("minecraftJava", bootContainer.isMinecraftJavaUse());

		JSONObject bungeeCordMotdObject = new JSONObject();
		bungeeCordMotdObject.put("motdPlayerInfo", "§5BusyCloud");
		bungeeCordMotdObject.put("motdLine1", "§cBusyCloud-Service §32");
		bungeeCordMotdObject.put("motdLine2", "§edeveloped by PlugDev");
		bungeeCordMotdObject.put("motdProtocol", "§aPublic-Beta v2");
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.put("§5>====================<");
		jsonArray.put("§c-!! BusyCloud | v2 !!-");
		jsonArray.put("§c-!!    Public Beta !!-");
		jsonArray.put("§5>--------------------<");
		jsonArray.put("§e-!! New Feature:   !!-");
		jsonArray.put("§e-!! NUKKIT_SUPPORT !!-");
		jsonArray.put("§5>====================<");
		bungeeCordMotdObject.put("motdPlayerInfo", jsonArray);
		
		JSONObject bungeeCordObject = new JSONObject();
		bungeeCordObject.put("maxPlayers", 40);
		bungeeCordObject.put("startport", bootContainer.isMinecraftJavaUse() ? 25577 : 19132);
		bungeeCordObject.put("maxRam", 512);
		bungeeCordObject.put("motdSettings", bungeeCordMotdObject);
		
		JSONObject featureObject = new JSONObject();
		featureObject.put("enable-motdModification", true);
		featureObject.put("enable-bungeePermissions", true);
		featureObject.put("nibble-compression", bootContainer.isNibbleCompression());
		featureObject.put("viaversion", bootContainer.isUseViaVersion());

		JSONArray jvmStartparameter = new JSONArray();
		jvmStartparameter.put("XX:+UseG1GC");
		jvmStartparameter.put("XX:MaxGCPauseMillis=50");
		jvmStartparameter.put("XX:MaxPermSize=256M");
		jvmStartparameter.put("XX:-UseAdaptiveSizePolicy");
		jvmStartparameter.put("XX:CompileThreshold=100");
		jvmStartparameter.put("Dcom.mojang.eula.agree=true");
		jvmStartparameter.put("Dio.netty.recycler.maxCapacity=0");
		jvmStartparameter.put("Dio.netty.recycler.maxCapacity.default=0");
		jvmStartparameter.put("Xmx%MAX%M");
		jvmStartparameter.put("jar %VERSION-IN-JAR%.jar");
		
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
		
		if(bootContainer.isPreinstall()) {
			CloudInstance.LOGGER.info("Preinstalling software...");
			if(bootContainer.isMinecraftJavaUse()) {
				CloudInstance.LOGGER.info("Starting download of: Waterfall-1.17, Spigot-1.8.8");
				ApplicationInterface.getAPI().getInfrastructure().getSoftwareById("Waterfall-1.17").download();
				ApplicationInterface.getAPI().getInfrastructure().getSoftwareById("Spigot-1.8.8").download();
			} else {
				CloudInstance.LOGGER.info("Starting download of: Waterdog-latest, Powernukkit-1.5.1.0");
				ApplicationInterface.getAPI().getInfrastructure().getSoftwareById("Waterdog-latest").download();
				ApplicationInterface.getAPI().getInfrastructure().getSoftwareById("Powernukkit-1.5.1.0").download();
			}
			CloudInstance.LOGGER.info("Preinstalling finished! Please restart the cloud to load Proxy and Lobby");
		}

		CloudInstance.LOGGER.info("Preinstalling CloudAPI...");
		ApplicationInterface.getAPI().getInfrastructure().getSoftwareById("API-latest").download();
		CloudInstance.LOGGER.info("Preinstalling CloudAPI finished! BusyCloud is ready to use!");
		
		Map<String, Object> lobbyServer = new LinkedHashMap<String, Object>();
		lobbyServer.put("serverSoftware", bootContainer.isMinecraftJavaUse() ? "Spigot-1.8.8" : "PowerNukkit-1.5.1.0");
		lobbyServer.put("startPort", bootContainer.isMinecraftJavaUse() ? 25580 : 19140);
		lobbyServer.put("maxRamEachServer", bootContainer.isMinecraftJavaUse() ? 256 : 512);
		lobbyServer.put("groupId", 1);
		lobbyServer.put("startServerByGroupstart", 2);
		lobbyServer.put("startNewServerByPercentage", 80);
		lobbyServer.put("lobbyState", true);
		jsonObject.put("Lobby", lobbyServer);
		FileUtils.writeFile(groupsSettingsFile, TextUtils.GSON.toJson(JsonParser.parseString(jsonObject.toString())));
		ApplicationInterface.getAPI().getInfrastructure().checkVersions();
		ApplicationInterface.getAPI().getInfrastructure().useViaVersion = bootContainer.isUseViaVersion();
		new ConsoleInstance(false);
	}

	/**
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
			for(String key : jsonObject.keySet())
				ApplicationInterface.getAPI().getInfrastructure().startServerGroup(key);
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
			SpigotServer server = new SpigotServer(root.getName(), ApplicationInterface.getAPI().getInfrastructure().getSoftwareById(version), true, 512);
			ApplicationInterface.getAPI().getInfrastructure().getRunningServers().add(server);
			server.startStaticServer();
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
