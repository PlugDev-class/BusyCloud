package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.api.plugins.Application;
import eu.busycloud.service.api.plugins.Event;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;
import eu.busycloud.service.utils.ServerGroupContainer;
import eu.busycloud.service.utils.SingleServerInstance;

@SuppressWarnings("unused")
public class Infrastructure {

	private List<ProxyServer> runningProxies = new LinkedList<>();
	private List<SingleServerInstance> runningServers = new LinkedList<>();
	private List<ServerGroup> runningGroups = new LinkedList<>();
	
	private List<Application> applications = new ArrayList<Application>();

	public ServerSoftware[] serverSoftwares = {

			new WebServerSoftware(ServerSoftwareType.BUSYCLOUD, "Web-Interface", "0.1", "PlugDev"),
			new WebServerSoftware(ServerSoftwareType.BUSYCLOUD, "Web-Introduction", "0.1", "PlugDev"),
			
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
			
			new WaterdogSoftware(ServerSoftwareType.PROXY, "WaterdogPE", "latest", "TobiasDev"),

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

			new NukkitSoftware(ServerSoftwareType.BEDROCK, "PowerNukkit", "1.5.1.0", "PowerNukkit"),

			new ServerSoftware(ServerSoftwareType.FORGE, "Mohist", "1.16.5", "MohistMC"),
			new ServerSoftware(ServerSoftwareType.FORGE, "Mohist", "1.12.2", "MohistMC"),
			

	};

	public boolean useViaVersion = false;
	public boolean nibbleCompression = true;
	public String serverName = "Unknown";
	public int maxRam = 1024;
	private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	public Infrastructure() {
		checkVersions();
	}

	/**
	 * 
	 * This method iterates over files to detect
	 * softwares.
	 * 
	 * @since 1.00a
	 * 
	 */
	public void checkVersions() {
		File backendDownloads = new File("saves/environments/");
		if (backendDownloads == null)
			return;
		if (backendDownloads.listFiles() == null)
			return;
		if (backendDownloads.listFiles().length == 0)
			return;
		
		Arrays.asList(backendDownloads.listFiles()).forEach((file) -> {
			Arrays.asList(serverSoftwares).forEach((software) -> {
				if(software.getVersionName()
						.equalsIgnoreCase(file.getName().replaceAll(".jar", "")))
					software.setAvailable(true);
			});
		});
	}
	
	/**
	 * 
	 * This method calls an event in every application.
	 * 
	 * @param event
	 * @since 2.0
	 */
	public void callEvents(Event event) {
		applications.forEach((plugin) -> {
			plugin.getEventList().forEach((pluginEvent) -> {
				if(pluginEvent instanceof Event)
					pluginEvent.onEvent();
			});
		});
	}

	/**
	 * 
	 * This method starts a proxyserver.
	 * 
	 * @param serverName
	 * @param serverSoftware
	 * @return
	 * @since 1.02
	 */
	
	public int startProxyServer(String serverName, ServerSoftware serverSoftware) {
		CloudInstance.LOGGER.info("Starting Proxy(\"" + serverName + "\",\"" + serverSoftware.getVersionName() + "\")");
		ProxyServer proxy = new ProxyServer();
		proxy.startProxy(serverSoftware);
		runningProxies.add(proxy);
		return proxy.getProxyid();
	}

	
	/**
	 * 
	 * This method starts a spigotserver
	 * 
	 * @param serverGroup
	 * @param serverSoftware
	 * @param proxyId
	 * @param maxRam
	 * @param acceptEula
	 * @param port
	 * @param isMain
	 * @return
	 * @since 1.02
	 */
	public UUID startSpigotServer(String serverGroup, ServerSoftware serverSoftware, int proxyId, int maxRam, boolean acceptEula, int port, boolean isMain) {
		CloudInstance.LOGGER.info("Starting SpigotServer(\"" + serverGroup + " - localhost:" + port + "\")");
		SpigotServer spigotServer = new SpigotServer(serverGroup, serverSoftware, true, maxRam, isMain);
		spigotServer.setPort(port);
		spigotServer.startServer();
		runningServers.add(spigotServer);
		return spigotServer.getId();
	}
	
	/**
	 * 
	 * This method starts a nukkitserver
	 * 
	 * @param serverGroup
	 * @param serverSoftware
	 * @param proxyId
	 * @param maxRam
	 * @param acceptEula
	 * @param port
	 * @param isMain
	 * @return
	 * @since 2.0
	 */
	
	public UUID startNukkitServer(String serverGroup, ServerSoftware serverSoftware, int proxyId, int maxRam, boolean acceptEula, int port, boolean isMain) {
		CloudInstance.LOGGER.info("Starting NukkitServer(\"" + serverGroup + " - localhost:" + port + "\")");
		NukkitServer spigotServer = new NukkitServer(serverGroup, serverSoftware, true, maxRam, isMain);
		spigotServer.setPort(port);
		spigotServer.startServer();
		runningServers.add(spigotServer);
		return spigotServer.getId();
	}
	
	/**
	 * 
	 * This method starts a static spigotserver
	 * 
	 * @param serverName
	 * @param serverSoftware
	 * @param proxyId
	 * @param maxRam
	 * @param acceptEula
	 * @param port
	 * @return
	 * @since 2.0
	 */
	
	public UUID startStaticSpigotServer(String serverName, ServerSoftware serverSoftware, int proxyId, int maxRam, boolean acceptEula, int port) {
		CloudInstance.LOGGER.info("Starting SpigotServer*(\"" + serverName + " - localhost:" + port + "\")");
		SpigotServer spigotServer = new SpigotServer(serverName, serverSoftware, acceptEula, maxRam);
		spigotServer.setPort(port);
		spigotServer.startStaticServer();
		runningServers.add(spigotServer);
		return spigotServer.getId();
	}
	
	/**
	 * 
	 * This method starts a static nukkitserver
	 * 
	 * @param serverName
	 * @param serverSoftware
	 * @param proxyId
	 * @param maxRam
	 * @param acceptEula
	 * @param port
	 * @return
	 * @since 2.0
	 */
	
	public UUID startStaticNukkitServer(String serverName, ServerSoftware serverSoftware, int proxyId, int maxRam, boolean acceptEula, int port) {
		CloudInstance.LOGGER.info("Starting NukkitServer*(\"" + serverName + " - localhost:" + port + "\")");
		NukkitServer spigotServer = new NukkitServer(serverName, serverSoftware, acceptEula, maxRam);
		spigotServer.setPort(port);
		spigotServer.startStaticServer();
		runningServers.add(spigotServer);
		return spigotServer.getId();
	}

	
	/**
	 * 
	 * This method starts a servergroup
	 * 
	 * @param serverGroupName
	 * @return
	 * @since 2.0
	 */
	
	public int startServerGroup(String serverGroupName) {
		return new ServerGroup(new ServerGroupContainer(serverGroupName)).getServerGroupContainer().getGroupId();
	}
	
	/**
	 * 
	 * This method stops a proxyserver
	 * 
	 * @param disconnectSpigots
	 * @param proxyId
	 * @since 1.02
	 */

	public void stopProxyServer(boolean disconnectSpigots, int proxyId) {
		ProxyServer proxy = getProxyById(proxyId);
		if (disconnectSpigots)
			proxy.getRegisteredServer().forEach((runningServer) -> { runningServer.stopServer(); });
		proxy.stopProxy();
		runningProxies.remove(proxy);
	}

	/**
	 * 
	 * This method stops a nukkit/spigot-server
	 * 
	 * @param serverId
	 * @since 2.0
	 */
	
	public void stopServer(UUID serverId) {
		getServerById(serverId).stopServer();
		runningServers.remove(getServerById(serverId));
	}

	
	/**
	 * 
	 * This method sends a command to a specific server
	 * 
	 * @param servername
	 * @param command
	 * @since 2.0
	 */
	
	public void rconServer(String servername, String command) {
		getServerByName(servername).rconServer(command);
	}
	
	/**
	 * 
	 * This method sends a command to a specific server
	 * 
	 * @param servername
	 * @param command
	 * @since 2.0
	 */
	public void rconServer(UUID id, String command) {
		getServerById(id).rconServer(command);
	}

	/**
	 * 
	 * This method sends a command to a specific proxy
	 * 
	 * @param servername
	 * @param command
	 * @since 1.00
	 */
	public void rconProxy(int serverId, String command) {
		getProxyById(serverId).sendRCON(command);
	}

	
	
	/**
	 * 
	 * This method returns a server by uuid
	 * 
	 * @param id
	 * @return
	 * @since 2.0
	 */
	
	public SingleServerInstance getServerById(UUID id) {
		for(SingleServerInstance serverInstance : runningServers)
			if(serverInstance.getId() == id)
				return serverInstance;
		return null;
	}

	/**
	 * 
	 * This method returns a server by name
	 * 
	 * @param name
	 * @return
	 * @since 2.0
	 */
	public SingleServerInstance getServerByName(String name) {
		for(SingleServerInstance serverInstance : runningServers)
			if(serverInstance.getPartServerName().equalsIgnoreCase(name))
				return serverInstance;
		return null;
	}

	
	/**
	 * 
	 * This method returns a server by a networkkey.
	 * 
	 * @param key
	 * @return
	 * @since 2.0
	 */
	public SingleServerInstance getServerByKey(String key) {
		for (SingleServerInstance server : runningServers)
			if (server.getRegisterKey().equals(key))
				return server;
		return null;
	}
	
	/**
	 * 
	 * This method returns a proxy by id
	 * 
	 * @param id
	 * @since 1.02
	 */
	
	public ProxyServer getProxyById(int id) {
		for (ProxyServer proxy : runningProxies)
			if (proxy.getProxyid() == id)
				return proxy;
		return null;
	}
	
	/**
	 * 
	 * This method returns a proxy by name
	 * 
	 * @param proxyName
	 * @return
	 * @since 2.0
	 */
	public ProxyServer getProxyByName(String proxyName) {
		for (ProxyServer proxy : runningProxies)
			if (proxy.getProxyName().equalsIgnoreCase(proxyName))
				return proxy;
		return null;
	}

	/**
	 * 
	 * This method returns a proxy by a networkkey.
	 * 
	 * @param id
	 * @return
	 * @since 1.02
	 */
	
	public ProxyServer getProxyByKey(String id) {
		for (ProxyServer proxy : runningProxies)
			if (proxy.getKey().toLowerCase().equalsIgnoreCase(id.toLowerCase()))
				return proxy;
		return null;
	}

	/**
	 * 
	 * This method returns a software by string-input
	 * 
	 * @param input
	 * @return
	 * @since 1.02
	 */
	
	public ServerSoftware getSoftwareById(String input) {
		for (ServerSoftware serverSoftware : serverSoftwares)
			if (serverSoftware.getVersionName().toLowerCase().equals(input.toLowerCase()))
				return serverSoftware;
		return null;
	}

	/**
	 * 
	 * This method returns a group by string-input
	 * 
	 * @param key
	 * @return
	 * @since 2.0
	 */
	
	public ServerGroup getGroupByName(String key) {
		for (ServerGroup group : runningGroups)
			if (group.getServerGroupContainer().getGroupName().equals(key))
				return group;
		return null;
	}

	/**
	 * 
	 * @since 1.00a
	 * 
	 */
	public void shutdownTask() {
		CloudInstance.LOGGER.info("Received Shutdown-Task");
		CloudInstance.LOGGER.info("Shutting down every proxy with their spigot-instances.");
		List<Object> objectList = new ArrayList<>();
		runningProxies.forEach((runningServer) -> { 
			objectList.add(runningServer.getProxyid());
		});
		objectList.forEach((runningServerId) -> {
			stopProxyServer(true, (int) runningServerId);
		});
		objectList.clear();
		
		runningServers.forEach((runningServer) -> {
			objectList.add(runningServer.getId());
		});
		objectList.forEach((runningServerId) -> {
			stopServer((UUID) runningServerId);
		});
		CloudInstance.LOGGER.info("Cleaning up...");
		CloudInstance.LOGGER.info("Thank you for using this program! See you later! <3");

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	/**
	 * 
	 * @return
	 * @since 1.00
	 */
	
	public List<ProxyServer> getRunningProxies() {
		return runningProxies;
	}
	
	/**
	 * 
	 * @return
	 * @since 1.00
	 */

	public List<SingleServerInstance> getRunningServers() {
		return runningServers;
	}

	/**
	 * 
	 * @return
	 * @since 1.00
	 */
	public List<ServerGroup> getRunningGroups() {
		return runningGroups;
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @since 1.00
	 */
	public boolean isValidSoftware(String input) {
		return getSoftwareById(input) != null;
	}

}