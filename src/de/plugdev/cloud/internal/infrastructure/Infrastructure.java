package de.plugdev.cloud.internal.infrastructure;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.ServerGroup;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.models.CDNVersion;
import de.plugdev.cloud.internal.models.IVersion;
import de.plugdev.cloud.internal.models.PaperVersion;
import de.plugdev.cloud.internal.models.TacoVersion;
import lombok.Data;

@Data
public class Infrastructure {

	private List<IVersion> versions = new ArrayList<>();
	private List<IService> services = new ArrayList<>();
	
	private List<MinecraftVersion> loadedMinecraftservices = new LinkedList<>();
	private List<ServerGroup> runningGroups = new LinkedList<>();

	public boolean useViaVersion = false;

	public Infrastructure() {
		versions.add(new CDNVersion("spigot-1.7.10", false));
		versions.add(new CDNVersion("spigot-1.8", true));
		versions.add(new CDNVersion("spigot-1.8.3", true));
		versions.add(new CDNVersion("spigot-1.8.4", true));
		versions.add(new CDNVersion("spigot-1.8.5", true));
		versions.add(new CDNVersion("spigot-1.8.6.jar", true));
		versions.add(new CDNVersion("spigot-1.8.7.jar", true));
		versions.add(new CDNVersion("spigot-1.8.7.jar", true));
		versions.add(new CDNVersion("spigot-1.8.8.jar", true));
		versions.add(new CDNVersion("spigot-1.9.jar", true));
		versions.add(new CDNVersion("spigot-1.9.2.jar", true));
		versions.add(new CDNVersion("spigot-1.9.4.jar", true));
		versions.add(new CDNVersion("spigot-1.10.jar", true));
		versions.add(new CDNVersion("spigot-1.10.2.jar", true));
		versions.add(new CDNVersion("spigot-1.11", false));
		versions.add(new CDNVersion("spigot-1.11.1", false));
		versions.add(new CDNVersion("spigot-1.11.2", false));
		versions.add(new CDNVersion("spigot-1.12", false));
		versions.add(new CDNVersion("spigot-1.12.1", false));
		versions.add(new CDNVersion("spigot-1.12.2", false));
		versions.add(new CDNVersion("spigot-1.13", false));
		versions.add(new CDNVersion("spigot-1.13.1", false));
		versions.add(new CDNVersion("spigot-1.13.2", false));
		versions.add(new CDNVersion("spigot-1.14", false));
		versions.add(new CDNVersion("spigot-1.14.1", false));
		versions.add(new CDNVersion("spigot-1.14.2", false));
		versions.add(new CDNVersion("spigot-1.14.3", false));
		versions.add(new CDNVersion("spigot-1.14.4", false));
		versions.add(new CDNVersion("spigot-1.15", false));
		versions.add(new CDNVersion("spigot-1.15.1", false));
		versions.add(new CDNVersion("spigot-1.15.2", false));
		versions.add(new CDNVersion("spigot-1.16.1", false));
		versions.add(new CDNVersion("spigot-1.16.2", false));
		versions.add(new CDNVersion("spigot-1.16.3", false));
		versions.add(new CDNVersion("spigot-1.16.4", false));
		versions.add(new CDNVersion("spigot-1.16.5", false));

		versions.add(new TacoVersion("taco-1.8.8"));
		versions.add(new TacoVersion("taco-1.9.2"));
		versions.add(new TacoVersion("taco-1.9.4"));

		versions.add(new PaperVersion("paper-1.8.8-443"));
		versions.add(new PaperVersion("paper-1.9.4-773"));
		versions.add(new PaperVersion("paper-1.10.2-916"));
		versions.add(new PaperVersion("paper-1.11.2-1104"));
		versions.add(new PaperVersion("paper-1.12.2-1618"));
		versions.add(new PaperVersion("paper-1.13.2-665"));
		versions.add(new PaperVersion("paper-1.14.4-243"));
		versions.add(new PaperVersion("paper-1.15.2-391"));
		versions.add(new PaperVersion("paper-1.16.5-621"));

		versions.add(new MinecraftVersion("BungeeCord", "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar", true));
		versions.add(new MinecraftVersion("Waterfall", "https://papermc.io/api/v2/projects/waterfall/versions/1.16/builds/405/downloads/waterfall-1.16-405.jar", true));

		loadVersions();
	}
	
	public void loadVersions() {
		File file = new File("backend/downloads");
		if(!file.exists())
			return;
		if(file.list().length == 0)
			return;
		Arrays.stream(file.listFiles()).forEach((fileElement) -> {
			versions.forEach((version) -> {
				if(version.getVersion().equalsIgnoreCase(file.getName().replaceFirst(".jar", "")))
					version.setAvailable(true);
			});
		});
	}
	

	public UUID startProxyServer(String serverName, IVersion minecraftVersion) {
		ConsoleOutput.write(ConsoleOutput.GREEN_BOLD, "Starting Proxy(\"" + serverName + "\",\"" + minecraftVersion.getVersion() + "\")");
		IService service = new Proxy(serverName, minecraftVersion);
		service.start();
		services.add(service);
		return service.getUniqueId();
	}

	public UUID startSpigotServer(String serverGroup, IVersion minecraftVersion, boolean isStatic, int maxRam, int port, boolean isLobby) {
		
		String serverName = getGroupByName(serverGroup).get().getName() + getGroupByName(serverGroup).get().getGroupList().size();
		ConsoleOutput.write(ConsoleOutput.GREEN_BOLD, "Starting SpigotServer(\"" + serverGroup + " - localhost:" + port + "\")");
		IService service = new SpigotServer(getProxy().getUniqueId(), serverName, serverGroup, maxRam, minecraftVersion, isStatic, isLobby);
		service.start();
		services.add(service);
		return service.getUniqueId();
	}
	
	public Optional<IService> getServiceById(UUID uniqueId) {
		return services.stream().filter((service) -> service.getUniqueId() == uniqueId).findAny();
	}

	public Optional<IService> getServiceByName(String name) {
		return services.stream().filter((predicate) -> predicate.getName().toLowerCase().equals(name.toLowerCase())).findAny();
	}
	
	public Proxy getProxy() {
		return services.stream().filter((predicate) -> predicate.getName().startsWith("proxy")).findAny().isPresent() ? (Proxy) services.stream().filter((predicate) -> predicate.getName().startsWith("proxy")).findAny().get() : loadProxy();
	}

	private Proxy loadProxy() {
		IVersion version = null;
		if (isValidVersion("BungeeCord")) {
			version = getVersionById("BungeeCord").get();
		} else if (isValidVersion("Waterfall")) {
			version = getVersionById("Waterfall").get();
		} else {
			ConsoleOutput.write(ConsoleOutput.BLUE, "No Proxy available. Maybe custom? Please provide to rename the custom jar to \"BungeeCord.jar\".");
			return null;
		}
		UUID proxyId = ApplicationInterface.getAPI().getInfrastructure().startProxyServer("Proxy-" + 1, version);
		return (Proxy) ApplicationInterface.getAPI().getInfrastructure().getServiceById(proxyId).get();
	}

	public Optional<IService> getServiceByKey(String key) {
		return services.stream().filter((predicate) -> predicate.getKey().toLowerCase().equals(key.toLowerCase())).findAny();
	}

	public Optional<IVersion> getVersionById(String input) {
		return versions.stream().filter((predicate) -> predicate.getVersion().toLowerCase().equals(input.toLowerCase())).findAny();
	}

	public Optional<ServerGroup> getGroupByName(String key) {
		return runningGroups.stream().filter((predicate) -> predicate.getName().toLowerCase().equals(key.toLowerCase())).findAny();
	}

	public void shutdownTask() {
		System.out.println("Received Shutdown-Task inquiry.");
		System.out.println("Shutting down active service-instances...");
		services.stream().forEach((service) -> service.stop());
		System.out.println("Cleaning up...");
		System.out.println("Thank you for using this program! See you later! <3");
		System.exit(0);
	}

	public boolean isValidVersion(String input) {
		return getVersionById(input).isPresent();
	}

}