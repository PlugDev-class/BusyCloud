package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;
import eu.busycloud.service.utils.ServerGroupContainer;

@SuppressWarnings("unused")
public class Infrastructure {

	private List<ProxyServer> runningProxies = new LinkedList<>();
	private List<SpigotServer> runningServers = new LinkedList<>();
	private List<ServerGroup> runningGroups = new LinkedList<>();

	public ServerSoftware[] serverSoftwares = {

			new WebServerSoftware(ServerSoftwareType.BUSYCLOUD, "Web-Interface", "0.1", "PlugDev"),
			new WebServerSoftware(ServerSoftwareType.BUSYCLOUD, "Web-General", "0.1", "PlugDev"),
			
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.17", "md5"),
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.16", "md5"),
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.15", "md5"),
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.14", "md5"),
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.13", "md5"),
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.12", "md5"),
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.11", "md5"),
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.10", "md5"),
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.9", "md5"),
			new ServerSoftware(ServerSoftwareType.PROXY, "BungeeCord", "1.8", "md5"),

			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.16", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.15", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.14", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.13", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.12", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.11", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.10", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.9", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.8", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Travertine", "1.7", "PaperMC"),

			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.17", "VelocityPowered"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.16", "VelocityPowered"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.15", "VelocityPowered"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.14", "VelocityPowered"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.13", "VelocityPowered"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.12", "VelocityPowered"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.11", "VelocityPowered"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.10", "VelocityPowered"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.9", "VelocityPowered"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Velocity", "1.8", "VelocityPowered"),

			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.17", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.16", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.15", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.14", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.13", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.12", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.11", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.10", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.9", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.PROXY, "Waterfall", "1.8", "PaperMC"),

			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.17", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.16.5", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.16.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.16.3", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.16.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.16.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.16", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.15.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.15.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.15", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.14.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.14.3", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.14.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.14.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.14", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.13.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.13.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.13", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.12.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.12.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.12", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.11.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.11.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.11", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.10.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.10", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.9.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.9.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.9", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.8.8", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Bukkit", "1.8", "Mojang"),

			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.17", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.16.5", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.16.4", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.16.3", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.16.2", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.16.1", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.16", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.15.2", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.15.1", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.15", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.14.4", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.14.3", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.14.2", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.14.1", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.14", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.13.2", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.13.1", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.13", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.12.2", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.12.1", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.12", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.11.2", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.11", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.10.2", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.10", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.9.4", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.9", "PaperMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Paper", "1.8.8", "PaperMC"),

			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.17", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.16.5", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.16.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.16.3", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.16.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.16.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.16", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.15.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.15.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.15", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.14.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.14.3", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.14.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.14.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.14", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.13.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.13.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.13", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.12.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.12.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.12", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.11.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.11.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.11", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.10.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.10.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.10", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.9.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.9.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.9.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.9", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.8.8", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.10", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.9", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.8", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.7", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.6", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.5", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.3", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.7", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.6.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.6.3", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.6.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.6.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.6", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.5.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.5.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.5", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.4.7", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.4.6", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.4.5", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.4.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.4.3", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.4.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.4.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.3.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.3.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.3", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.2.5", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.2.4", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.2.3", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.2.2", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.2.1", "Mojang"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Vanilla", "1.2", "Mojang"),

			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.17", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.16.5", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.16.4", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.16.3", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.16.2", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.16.1", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.16", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.15.2", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.15.1", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.15", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.14.4", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.14.3", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.14.2", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.14.1", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.14", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.13.2", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.13.1", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.13", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.12.2", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.12.1", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.12", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.11.2", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.11.1", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.11", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.10.2", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.10", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.9.4", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.9.2", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.9", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.8.8", "SpigotMC"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Spigot", "1.8", "SpigotMC"),

			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.17", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.16.5", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.16.4", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.16.3", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.16.2", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.16.1", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.15.2", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.15.1", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.15", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.14.4", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.14.3", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.14.2", "pl3xgaming"),
			new ServerSoftware(ServerSoftwareType.JAVA, "Purpur", "1.14.1", "pl3xgaming"),

			new ServerSoftware(ServerSoftwareType.JAVA, "Tuinity", "1.16.5", "Tuinity Ltd."),
			new ServerSoftware(ServerSoftwareType.JAVA, "Tuinity", "1.16.4", "Tuinity Ltd."),
			new ServerSoftware(ServerSoftwareType.JAVA, "Tuinity", "1.16.3", "Tuinity Ltd."),
			new ServerSoftware(ServerSoftwareType.JAVA, "Tuinity", "1.16.2", "Tuinity Ltd."),

			new ServerSoftware(ServerSoftwareType.BEDROCK, "NukkitX", "1.16", "Cloudburst"),
			new ServerSoftware(ServerSoftwareType.BEDROCK, "NukkitX", "1.14", "Cloudburst"),

			new ServerSoftware(ServerSoftwareType.FORGE, "Mohist", "1.16.5", "MohistMC"),
			new ServerSoftware(ServerSoftwareType.FORGE, "Mohist", "1.12.2", "MohistMC"),
			

	};

	public boolean useViaVersion = false;
	public String serverName = "Unknown";
	ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	public Infrastructure() {
		checkVersions();
	}

	public void checkVersions() {
		File backendDownloads = new File("saves/environments/");
		if (backendDownloads == null)
			return;
		if (backendDownloads.listFiles() == null)
			return;
		if (backendDownloads.listFiles().length == 0)
			return;
		for (File file : backendDownloads.listFiles()) {
			final String name = file.getName().replaceAll(".jar", "");
			for (ServerSoftware serverSoftware : serverSoftwares)
				if (serverSoftware.getVersionName().equalsIgnoreCase(name))
					serverSoftware.setAvailable(true);
		}
	}

	public int startProxyServer(String serverName, ServerSoftware serverSoftware) {
		CloudInstance.LOGGER.info("Starting Proxy(\"" + serverName + "\",\"" + serverSoftware.getVersionName() + "\")");
		ProxyServer proxy = new ProxyServer();
		proxy.startProxy(serverSoftware);
		runningProxies.add(proxy);
		return proxy.getProxyid();
	}

	public int startSpigotServer(String serverGroup, ServerSoftware serverSoftware, int proxyId, boolean isStatic,
			int maxRam, boolean acceptEula, int port, boolean isMain) {
		CloudInstance.LOGGER.info("Starting SpigotServer(\"" + serverGroup + " - localhost:" + port + "\")");
		SpigotServer spigotServer = new SpigotServer();
		spigotServer.setMaxRam(maxRam);
		spigotServer.setPort(port);
		spigotServer.setStatic(isStatic);
		spigotServer.startServer(serverGroup, serverSoftware, acceptEula, maxRam, isMain);
		runningServers.add(spigotServer);
		return spigotServer.getId();
	}

	public int startServerGroup(String serverGroupName) {
		return new ServerGroup(new ServerGroupContainer(serverGroupName)).getServerGroupContainer().getGroupId();
	}

	public void stopProxyServer(boolean disconnectSpigots, int proxyId) {
		ProxyServer proxy = getProxyById(proxyId);
		if (disconnectSpigots) {
			for (SpigotServer runningServers : proxy.getRegisteredServer()) {
				runningServers.stopServer();
			}
		}
		proxy.stopProxy();
	}

	public void stopSpigotServer(int serverId) {
		getSpigotServerById(serverId).stopServer();
	}

	public void rconSpigotServer(int serverId, String command) {
		getSpigotServerById(serverId).sendRCON(command);
	}

	public void rconProxy(int serverId, String command) {
		getProxyById(serverId).sendRCON(command);
	}

	public ProxyServer getProxyById(int id) {
		for (ProxyServer proxy : runningProxies) {
			if (proxy.getProxyid() == id) {
				return proxy;
			}
		}
		return null;
	}

	public SpigotServer getSpigotServerById(int id) {
		for (SpigotServer spigotServer : runningServers) {
			if (spigotServer.getId() == id) {
				return spigotServer;
			}
		}
		return null;
	}

	public SpigotServer getSpigotServerByName(String name) {
		for (SpigotServer spigotServer : runningServers) {
			if (spigotServer.getServerName().equalsIgnoreCase(name)) {
				return spigotServer;
			}
		}
		return null;
	}

	public ProxyServer getProxyByKey(String id) {
		for (ProxyServer proxy : runningProxies) {
			if (proxy.getKey().toLowerCase().equalsIgnoreCase(id.toLowerCase())) {
				return proxy;
			}
		}
		return null;
	}

	public ServerSoftware getVersionById(String input) {
		for (ServerSoftware serverSoftware : serverSoftwares)
			if (serverSoftware.getVersionName().toLowerCase().equals(input.toLowerCase()))
				return serverSoftware;
		return null;
	}

	public SpigotServer getSpigotServerByKey(String key) {
		for (SpigotServer server : runningServers)
			if (server.getRegisterKey().equals(key))
				return server;
		return null;
	}

	public ServerGroup getGroupbyName(String key) {
		for (ServerGroup group : runningGroups)
			if (group.getServerGroupContainer().getGroupName().equals(key))
				return group;
		return null;
	}

	public void shutdownTask() {
		CloudInstance.LOGGER.info("Received Shutdown-Task");
		CloudInstance.LOGGER.info("Shutting down every proxy with their spigot-instances.");
		for (ProxyServer runningProxies : runningProxies) {
			CloudInstance.LOGGER.info("Stop proxy: " + runningProxies.getProxyName());
			stopProxyServer(true, runningProxies.getProxyid());
		}

		CloudInstance.LOGGER.info("Try to stop any other servers attached to this program.");
		for (SpigotServer runningServers : runningServers) {
			CloudInstance.LOGGER.info("Stop serverinstance: " + runningServers.getServerName());
			stopSpigotServer(runningServers.getId());
		}
		CloudInstance.LOGGER.info("Cleaning up...");
		CloudInstance.LOGGER.info("Thank you for using this program! See you later! <3");

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	public List<ProxyServer> getRunningProxies() {
		return runningProxies;
	}

	public List<SpigotServer> getRunningServers() {
		return runningServers;
	}

	public List<ServerGroup> getRunningGroups() {
		return runningGroups;
	}

	public boolean isValidVersion(String input) {
		return getVersionById(input) != null;
	}

}