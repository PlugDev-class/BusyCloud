package de.plugdev.cloud.internal.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.ServerGroup;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.generate.ServerGenerator;
import de.plugdev.cloud.internal.utils.FileUtils;
import de.terrarier.netlistening.Connection;

public class SpigotServer {

	private Process instance;

	private int id;
	private String serverName;
	private String serverGroup;
	private int port;
	private int proxyId;
	private int maxRam;
	private MinecraftVersion minecraftVersion;
	private boolean eula;
	private boolean isStatic;

	private Connection connection;
	private String registerKey;

	private Proxy prefferedProxy = null;
	private boolean isMain;

	public void startServer(String serverGroup, MinecraftVersion version, boolean eula, int maxRam, boolean isMain) {
		registerKey = "KEY_" + new Random().nextInt(Integer.MAX_VALUE);
		setMinecraftVersion(version);
		setServerGroup(serverGroup != null ? serverGroup : "Ungrouped");
		this.id = new Random().nextInt(20000);
		this.eula = eula;
		this.isMain = isMain;
		setServerName(serverGroup + "#" + getId());

		new ServerGenerator(version, this);

		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("java ");
		commandBuilder.append(
				"-XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxPermSize=256M -XX:-UseAdaptiveSizePolicy -XX:CompileThreshold=100 "
						+ "-Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 "
						+ "-Dio.netty.recycler.maxCapacity.default=0 "
						+ "-Djline.terminal=jline.UnsupportedTerminal -Xmx" + maxRam + "M -jar " + version.getVersion()
						+ ".jar");
		try {
			this.instance = Runtime.getRuntime().exec(commandBuilder.toString().split(" "), null,
					new File("server/" + ("temp") + "/" + this.getServerName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Proxy prefferedProxy = null;
		for (Proxy proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
			if (prefferedProxy == null
					|| prefferedProxy.getRegisteredServer().size() > proxy.getRegisteredServer().size()) {
				prefferedProxy = proxy;
			}
		}

		if (prefferedProxy == null) {

			MinecraftVersion minecraftVersion = ApplicationInterface.getAPI().getInfrastructure()
					.getVersionById("BungeeCord").isAvailable()
							? ApplicationInterface.getAPI().getInfrastructure().getVersionById("BungeeCord")
							: ApplicationInterface.getAPI().getInfrastructure().getVersionById("Waterfall");

			int id = ApplicationInterface.getAPI().getInfrastructure().startProxyServer("Proxy-1", minecraftVersion);
			prefferedProxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(id);
		}

		prefferedProxy.addSpigotServer(this, isMain);
	}
	
	public File getLatestLog() {
		return new File("server/" + ("temp") + "/" + this.getServerName() + "/logs/latest.log");
	}

	public void startStaticServer(String serverName, MinecraftVersion version, boolean eula, int maxRam) {
		registerKey = "KEY_" + new Random().nextInt(Integer.MAX_VALUE);
		setMinecraftVersion(version);
		setServerGroup("Static");
		this.id = new Random().nextInt(20000);
		this.eula = eula;
		setServerName(serverName);
		setStatic(true);
		
		if (!new File("server/static/" + getServerName()).exists()) {
			new ServerGenerator(version, this);
		} else {
			FileUtils.writeFile(new File("server/static/" + getServerName() + "/" + getRegisterKey()), getRegisterKey());
			FileUtils.copyFile(new File("backend/downloads/SpigotCloudBridge.jar").toPath(), new File("server/static/" + getServerName() + "/plugins/SpigotCloudBridge.jar").toPath());
		}
		
		try {
			
			List<String> strings = Files.readAllLines(new File("server/static/" + getServerName() + "/server.properties").toPath());
			for(String string : strings) {
				if(string.startsWith("server-port=")) {
					port = Integer.parseInt(string.replaceAll("server-port=", ""));
					strings.clear();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		ConsoleOutput.write(ConsoleOutput.GREEN_BOLD, "[CORE] Starting SpigotServer(\"" + getServerName() + " - localhost:" + getPort() + "\")");

		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("java ");
		commandBuilder.append(
				"-XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxPermSize=256M -XX:-UseAdaptiveSizePolicy -XX:CompileThreshold=100 "
						+ "-Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 "
						+ "-Dio.netty.recycler.maxCapacity.default=0 "
						+ "-Djline.terminal=jline.UnsupportedTerminal -jar " + version.getVersion() + ".jar");
		try {
			this.instance = Runtime.getRuntime().exec(commandBuilder.toString().split(" "), null,
					new File("server/" + ("static") + "/" + this.getServerName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		Proxy prefferedProxy = null;
		for (Proxy proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
			if (prefferedProxy == null
					|| prefferedProxy.getRegisteredServer().size() > proxy.getRegisteredServer().size()) {
				prefferedProxy = proxy;
			}
		}

		if (prefferedProxy == null) {

			MinecraftVersion minecraftVersion = ApplicationInterface.getAPI().getInfrastructure()
					.getVersionById("BungeeCord").isAvailable()
							? ApplicationInterface.getAPI().getInfrastructure().getVersionById("BungeeCord")
							: ApplicationInterface.getAPI().getInfrastructure().getVersionById("Waterfall");

			int id = ApplicationInterface.getAPI().getInfrastructure().startProxyServer("Proxy-1", minecraftVersion);
			prefferedProxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(id);
		}

		prefferedProxy.addSpigotServer(this, false);
	}

	public void stopServer() {

		ServerGroup prefferedGroup = null;
		for (ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
			if (group.getGroupList().contains(this)) {
				prefferedGroup = group;
			}
		}

		if (prefferedGroup != null) {
			prefferedGroup.getGroupList().remove(this);
		}

		ConsoleOutput.write(ConsoleOutput.GREEN_BOLD,
				"[CORE] Stopping SpigotServer(\"" + serverGroup + " - localhost:" + port + "\")");

		ApplicationInterface.getAPI().getInfrastructure().getRunningServers().remove(this);

		if (instance.isAlive()) {
			instance.destroyForcibly();
		}

		if (prefferedProxy != null) {
			prefferedProxy.removeSpigotServer(this);
		}

		if (isStatic) {
			return;
		}

		FileUtils.deleteFolderRecursivly(new File("server/" + ("temp") + "/" + this.getServerName()).getPath());
		FileUtils.deleteFile("server/" + ("temp") + "/" + this.getServerName());

	}

	public void sendRCON(String command) {
		if (getConnection() != null) {
			if (getConnection().isConnected()) {
				connection.sendData("rcon", command);
				return;
			}
		}
		ConsoleOutput.write(ConsoleOutput.RED, "[CORE] The server isn't linked to any proxy.");
	}

	public boolean doesAcceptEula() {
		return eula;
	}

	public Process getInstance() {
		return instance;
	}

	public void setMinecraftVersion(MinecraftVersion minecraftVersion) {
		this.minecraftVersion = minecraftVersion;
	}

	public MinecraftVersion getMinecraftVersion() {
		return minecraftVersion;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setServerGroup(String serverGroup) {
		this.serverGroup = serverGroup;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setMaxRam(int maxRam) {
		this.maxRam = maxRam;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getRegisterKey() {
		return registerKey;
	}

	public boolean isMain() {
		return isMain;
	}

	public int getMaxRam() {
		return maxRam;
	}

	public int getPort() {
		return port;
	}

	public int getProxyId() {
		return proxyId;
	}

	public int getId() {
		return id;
	}

	public String getServerName() {
		return serverName;
	}

	public String getServerGroup() {
		return serverGroup;
	}

	public void doTemplate(String servergroup) {

		if (isStatic) {
			return;
		}

		File serverFolder = new File("server/temp/" + getServerName());
		File backendTemplates = new File("backend/templates/" + servergroup);
		if (!backendTemplates.exists()) {
			backendTemplates.mkdirs();
		} else if (backendTemplates.listFiles().length != 0) {
			FileUtils.deleteFolderRecursivly(backendTemplates.getPath());
		}
		try {
			FileUtils.copyFolder(serverFolder.toPath(), backendTemplates.toPath());
			ConsoleOutput.write(ConsoleOutput.GREEN, "[PLUGIN] Template created.");
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

}