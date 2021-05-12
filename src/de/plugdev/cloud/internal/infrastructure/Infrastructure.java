package de.plugdev.cloud.internal.infrastructure;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.plugdev.cloud.external.ServerGroup;
import de.plugdev.cloud.internal.console.ConsoleOutput;

@SuppressWarnings("unused")
public class Infrastructure {

	private List<Proxy> runningProxies = new LinkedList<>();
	private List<SpigotServer> runningServers = new LinkedList<>();
	private List<MinecraftVersion> allowedMinecraftservices = new LinkedList<>();
	private List<MinecraftVersion> loadedMinecraftservices = new LinkedList<>();
	private List<ServerGroup> runningGroups = new LinkedList<>();
	private List<MinecraftVersion> downloads = new LinkedList<>();

	public boolean useViaVersion = false;
	private List<ExecutorService> services = new LinkedList<>();

	public Infrastructure() {
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.7.10", "https://cdn.getbukkit.org/spigot/spigot-1.7.10-SNAPSHOT-b1657.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.8", "https://cdn.getbukkit.org/spigot/spigot-1.8-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.8.3",
				"https://cdn.getbukkit.org/spigot/spigot-1.8.3-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.8.4",
				"https://cdn.getbukkit.org/spigot/spigot-1.8.4-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.8.5",
				"https://cdn.getbukkit.org/spigot/spigot-1.8.5-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.8.6",
				"https://cdn.getbukkit.org/spigot/spigot-1.8.6-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.8.7",
				"https://cdn.getbukkit.org/spigot/spigot-1.8.7-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.8.7",
				"https://cdn.getbukkit.org/spigot/spigot-1.8.7-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.8.8",
				"https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.9",
				"https://cdn.getbukkit.org/spigot/spigot-1.9-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.9.2",
				"https://cdn.getbukkit.org/spigot/spigot-1.9.2-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.9.4",
				"https://cdn.getbukkit.org/spigot/spigot-1.9.4-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.10",
				"https://cdn.getbukkit.org/spigot/spigot-1.10-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("spigot-1.10.2",
				"https://cdn.getbukkit.org/spigot/spigot-1.10.2-R0.1-SNAPSHOT-latest.jar", false));
		allowedMinecraftservices
				.add(new MinecraftVersion("spigot-1.11", "https://cdn.getbukkit.org/spigot/spigot-1.11.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.11.1", "https://cdn.getbukkit.org/spigot/spigot-1.11.1.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.11.2", "https://cdn.getbukkit.org/spigot/spigot-1.11.2.jar", false));
		allowedMinecraftservices
				.add(new MinecraftVersion("spigot-1.12", "https://cdn.getbukkit.org/spigot/spigot-1.12.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.12.1", "https://cdn.getbukkit.org/spigot/spigot-1.12.1.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.12.2", "https://cdn.getbukkit.org/spigot/spigot-1.12.2.jar", false));
		allowedMinecraftservices
				.add(new MinecraftVersion("spigot-1.13", "https://cdn.getbukkit.org/spigot/spigot-1.13.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.13.1", "https://cdn.getbukkit.org/spigot/spigot-1.13.1.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.13.2", "https://cdn.getbukkit.org/spigot/spigot-1.13.2.jar", false));
		allowedMinecraftservices
				.add(new MinecraftVersion("spigot-1.14", "https://cdn.getbukkit.org/spigot/spigot-1.14.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.14.1", "https://cdn.getbukkit.org/spigot/spigot-1.14.1.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.14.2", "https://cdn.getbukkit.org/spigot/spigot-1.14.2.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.14.3", "https://cdn.getbukkit.org/spigot/spigot-1.14.3.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.14.4", "https://cdn.getbukkit.org/spigot/spigot-1.14.4.jar", false));
		allowedMinecraftservices
				.add(new MinecraftVersion("spigot-1.15", "https://cdn.getbukkit.org/spigot/spigot-1.15.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.15.1", "https://cdn.getbukkit.org/spigot/spigot-1.15.1.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.15.2", "https://cdn.getbukkit.org/spigot/spigot-1.15.2.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.16.1", "https://cdn.getbukkit.org/spigot/spigot-1.16.1.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.16.2", "https://cdn.getbukkit.org/spigot/spigot-1.16.2.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.16.3", "https://cdn.getbukkit.org/spigot/spigot-1.16.3.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.16.4", "https://cdn.getbukkit.org/spigot/spigot-1.16.4.jar", false));
		allowedMinecraftservices.add(
				new MinecraftVersion("spigot-1.16.5", "https://cdn.getbukkit.org/spigot/spigot-1.16.5.jar", false));

		allowedMinecraftservices.add(new MinecraftVersion("taco-1.8.8",
				"https://github.com/TacoSpigot/TacoSpigot/releases/download/v1.8.8-R1.0/TacoSpigot.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("taco-1.9.2",
				"https://github.com/TacoSpigot/TacoSpigot/releases/download/v1.9.2-R0.1/TacoSpigot.jar", false));
		allowedMinecraftservices.add(new MinecraftVersion("taco-1.9.4",
				"https://github.com/TacoSpigot/TacoSpigot/releases/download/v1.9.4-R0.1/TacoSpigot.jar", false));

		allowedMinecraftservices.add(new MinecraftVersion("paper-1.8.8",
				"https://papermc.io/api/v2/projects/paper/versions/1.8.8/builds/443/downloads/paper-1.8.8-443.jar",
				false));
		allowedMinecraftservices.add(new MinecraftVersion("paper-1.9.4",
				"https://papermc.io/api/v2/projects/paper/versions/1.9.4/builds/773/downloads/paper-1.9.4-773.jar",
				false));
		allowedMinecraftservices.add(new MinecraftVersion("paper-1.10.2",
				"https://papermc.io/api/v2/projects/paper/versions/1.10.2/builds/916/downloads/paper-1.10.2-916.jar",
				false));
		allowedMinecraftservices.add(new MinecraftVersion("paper-1.11.2",
				"https://papermc.io/api/v2/projects/paper/versions/1.11.2/builds/1104/downloads/paper-1.11.2-1104.jar",
				false));
		allowedMinecraftservices.add(new MinecraftVersion("paper-1.12.2",
				"https://papermc.io/api/v2/projects/paper/versions/1.12.2/builds/1618/downloads/paper-1.12.2-1618.jar",
				false));
		allowedMinecraftservices.add(new MinecraftVersion("paper-1.13.2",
				"https://papermc.io/api/v2/projects/paper/versions/1.13.2/builds/655/downloads/paper-1.13.2-655.jar",
				false));
		allowedMinecraftservices.add(new MinecraftVersion("paper-1.14.4",
				"https://papermc.io/api/v2/projects/paper/versions/1.14.4/builds/243/downloads/paper-1.14.4-243.jar",
				false));
		allowedMinecraftservices.add(new MinecraftVersion("paper-1.15.2",
				"https://papermc.io/api/v2/projects/paper/versions/1.15.2/builds/391/downloads/paper-1.15.2-391.jar",
				false));
		allowedMinecraftservices.add(new MinecraftVersion("paper-1.16.5",
				"https://papermc.io/api/v2/projects/paper/versions/1.16.5/builds/621/downloads/paper-1.16.5-621.jar",
				false));

		allowedMinecraftservices.add(new MinecraftVersion("BungeeCord",
				"https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar",
				true));
		allowedMinecraftservices.add(new MinecraftVersion("Waterfall",
				"https://papermc.io/api/v2/projects/waterfall/versions/1.16/builds/405/downloads/waterfall-1.16-405.jar",
				true));

		checkVersions();

		services.add(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));
	}

	public void checkVersions() {
		File backendDownloads = new File("backend/downloads/");
		if (backendDownloads == null) {
			return;
		}
		if (backendDownloads.listFiles() == null) {
			return;
		}
		if (backendDownloads.listFiles().length != 0) {
			for (File file : backendDownloads.listFiles()) {
				final String name = file.getName().replaceAll(".jar", "");
				for (MinecraftVersion version : allowedMinecraftservices) {
					if (version.getVersion().equalsIgnoreCase(name)) {
						version.setAvailable(true);
					}
				}
			}
		}
	}

	public int startProxyServer(String serverName, MinecraftVersion minecraftVersion) {
		ConsoleOutput.write(ConsoleOutput.GREEN_BOLD,
				"[CORE] Starting Proxy(\"" + serverName + "\",\"" + minecraftVersion.getVersion() + "\")");
		Proxy proxy = new Proxy();
		proxy.startProxy(minecraftVersion);
		runningProxies.add(proxy);
		return proxy.getProxyid();
	}

	public int startSpigotServer(String serverGroup, MinecraftVersion minecraftVersion, int proxyId, boolean isStatic,
			int maxRam, boolean acceptEula, int port, boolean isMain) {
		ConsoleOutput.write(ConsoleOutput.GREEN_BOLD,
				"[CORE] Starting SpigotServer(\"" + serverGroup + " - localhost:" + port + "\")");
		SpigotServer spigotServer = new SpigotServer();
		spigotServer.setMaxRam(maxRam);
		spigotServer.setPort(port);
		spigotServer.setStatic(isStatic);
		spigotServer.startServer(serverGroup, minecraftVersion, acceptEula, maxRam, isMain);
		runningServers.add(spigotServer);
		return spigotServer.getId();
	}

	public void stopProxyServer(boolean disconnectSpigots, int proxyId) {
		Proxy proxy = getProxyById(proxyId);
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

	public Proxy getProxyById(int id) {
		for (Proxy proxy : runningProxies) {
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

	public Proxy getProxyByKey(String id) {
		for (Proxy proxy : runningProxies) {
			if (proxy.getKey().toLowerCase().equalsIgnoreCase(id.toLowerCase())) {
				return proxy;
			}
		}
		return null;
	}

	public MinecraftVersion getVersionById(String input) {
		for (MinecraftVersion minecraftVersion : allowedMinecraftservices) {
			if (minecraftVersion.getVersion().toLowerCase().equals(input.toLowerCase())) {
				return minecraftVersion;
			}
		}
		return null;
	}

	public SpigotServer getSpigotServerByKey(String key) {
		for (SpigotServer server : runningServers) {
			if (server.getRegisterKey().equals(key)) {
				return server;
			}
		}
		return null;
	}

	public ServerGroup getGroupbyName(String key) {
		for (ServerGroup group : runningGroups) {
			if (group.getGroupName().equals(key)) {
				return group;
			}
		}
		return null;
	}

	public void addTask(Runnable runnable) {
		services.get(1).execute(runnable);
	}

	public void shutdownTask() {
		System.out.println("[CORE] Received Shutdown-Task inquiry.");
		System.out.println("[CORE] Shutting down every proxy with their spigot-instances.");
		for (Proxy runningProxies : runningProxies) {
			stopProxyServer(true, runningProxies.getProxyid());
			System.out.println("[CORE] Stopped proxy " + runningProxies.getProxyName());
		}

		System.out.println("[CORE] Try to stop any other servers attached to this program.");
		for (SpigotServer runningServers : runningServers) {
			stopSpigotServer(runningServers.getId());
			System.out.println("[CORE] Stop spigotinstance " + runningServers.getServerName());
		}

		System.out.println("[CORE] Cleaning up...");

		System.out.println("[CORE] Thank you for using this program! See you later! <3");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	public List<Proxy> getRunningProxies() {
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

	public List<ExecutorService> getServices() {
		return services;
	}

}