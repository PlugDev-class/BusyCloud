package de.plugdev.cloud.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.stream.Stream;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.api.ServerGroup;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.infrastructure.generate.ServerGenerator;
import de.terrarier.netlistening.Connection;
import de.terrarier.netlistening.api.DataContainer;

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
					new File("server/" + (this.isStatic() ? "static" : "temp") + "/" + this.getServerName()));
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

	public void stopServer() {
//		if (getConnection() != null) {
//			if (getConnection().isConnected()) {
//				getConnection().disconnect();
//			}
//		}

		ServerGroup prefferedGroup = null;
		for (ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
			if (group.getGroupList().contains(this)) {
				prefferedGroup = group;
			}
		}

		if (prefferedGroup != null) {
			prefferedGroup.getGroupList().remove(this);
		}

		ConsoleColors.write(ConsoleColors.GREEN_BOLD,
				"[CORE] Stopping SpigotServer(\"" + serverGroup + " - localhost:" + port + "\")");
		
		ApplicationInterface.getAPI().getInfrastructure().getRunningServers().remove(this);
		
		if (instance.isAlive()) {
			instance.destroyForcibly();
		}
		
		if (prefferedProxy != null) {
			prefferedProxy.removeSpigotServer(this);
		}
		
		if(!isStatic) {
			return;
		}
		
		delete(new File("server/" + ("temp") + "/" + this.getServerName()));
		new File("server/" + ("temp") + "/" + this.getServerName()).delete();

	}

	public void delete(File root) {
		if (root.listFiles() != null) {
			for (File file : root.listFiles()) {
				if (file.isDirectory()) {
					delete(file);
				}
				file.delete();
			}
		}
	}

	public void sendRCON(String command) {
		if(getConnection() != null) {
			if(getConnection().isConnected()) {
				DataContainer container = new DataContainer();
				container.add("rcon");
				container.add(command);
				connection.sendData(container);
				return;
			}
		}
		ConsoleColors.write(ConsoleColors.RED, "[CORE] The server isn't linked to any proxy.");
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
		
		if(isStatic) {
			return;
		}

		File serverFolder = new File("server/temp/" + getServerName());
		File backendTemplates = new File("backend/templates/" + servergroup);
		if(!backendTemplates.exists()) {
			backendTemplates.mkdirs();
		} else if(backendTemplates.listFiles().length != 0) {
			delete(backendTemplates);
		}
		try {
			copyFolder(serverFolder.toPath(), backendTemplates.toPath());
			ConsoleColors.write(ConsoleColors.GREEN, "[PLUGIN] Template created.");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		
		
	}
	
	public void copyFolder(Path src, Path dest) throws IOException {
		try (Stream<Path> stream = Files.walk(src)) {
			stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
		}
	}

	private void copy(Path source, Path dest) {
		try {
			Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
