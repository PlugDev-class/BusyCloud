package de.plugdev.cloud.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.stream.Stream;

import de.plugdev.cloud.api.PlayerInfo;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.infrastructure.generate.ServerGenerator;
import de.terrarier.netlistening.Connection;
import de.terrarier.netlistening.api.DataContainer;

public class Proxy {

	private Process instance;

	private String proxyName = "";
	private int proxyid = 0;
	private MinecraftVersion mcversion;
	private int port;
	
	private boolean maintenance = false;
	private List<UUID> whitelistedPlayers = new LinkedList<>();

	private Connection connection;
	private String registerKey;

	private List<SpigotServer> registeredServer = new LinkedList<>();
	private List<PlayerInfo> registeredPlayer = new LinkedList<>();

	public void startProxy(MinecraftVersion version) {
		setRegisterKey("KEY_" + new Random().nextInt(Integer.MAX_VALUE));
		this.proxyid = new Random().nextInt(20000);
		this.port = new Random().nextInt(40000) + 20000;
		setProxyName("Proxy-" + getProxyid());
		
		setVersion(version);

		new ServerGenerator(version, this);

		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("java ");
		commandBuilder.append(
				"-XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxPermSize=256M -XX:-UseAdaptiveSizePolicy -XX:CompileThreshold=100 "
						+ "-Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 "
						+ "-Dio.netty.recycler.maxCapacity.default=0 "
						+ "-Djline.terminal=jline.UnsupportedTerminal -Xmx" + 512 + "M -jar " + version.getVersion()
						+ ".jar");
		try {
			this.instance = Runtime.getRuntime().exec(commandBuilder.toString().split(" "), null,
					new File("server/" + ("temp") + "/" + this.getProxyName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stopProxy() {
		if(getConnection() != null) {
			if(getConnection().isConnected()) {
				getConnection().disconnect();
			}
		}
		ConsoleColors.write(ConsoleColors.GREEN_BOLD, "[CORE] Stopping Proxy(\"Proxy-" + getProxyid() + " - localhost:" + port + "\")");
		if (instance.isAlive()) {
			instance.destroyForcibly();
		}
		
		delete(new File("server/" + ("temp") + "/" + this.getProxyName()));
		new File("server/" + ("temp") + "/" + this.getProxyName()).delete();
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
		DataContainer container = new DataContainer();
		container.add("rcon");
		container.add(command);
		connection.sendData(container);
	}
	
	public void setVersion(MinecraftVersion version) {
		this.mcversion = version;
	}
	
	public MinecraftVersion getVersion() {
		return mcversion;
	}

	private void setRegisterKey(String registerKey) {
		this.registerKey = registerKey;
	}

	public void addSpigotServer(SpigotServer spigotServer, boolean isMain) {
		
		registeredServer.add(spigotServer);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (connection != null) {
					DataContainer container = new DataContainer();
					container.add("registerserver");
					container.add(spigotServer.getServerName());
					container.add(spigotServer.getPort());
					container.add(isMain);
					connection.sendData(container);
					timer.cancel();
 				}
			}
		}, 1000, 1000);

	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getKey() {
		return registerKey;
	}

	public int getProxyid() {
		return proxyid;
	}

	public Connection getConnection() {
		return connection;
	}

	public List<SpigotServer> getRegisteredServer() {
		return registeredServer;
	}

	public List<PlayerInfo> getOnlinePlayer() {
		return registeredPlayer;
	}

	public String getProxyName() {
		return proxyName;
	}

	public int getPort() {
		return port;
	}
	
	public void doTemplate() {

		File serverFolder = new File("server/temp/" + getProxyName());
		File backendTemplates = new File("backend/templates/proxy");
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

	public void removeSpigotServer(SpigotServer spigotServer) {
		DataContainer container = new DataContainer();
		container.add("unregisterserver");
		container.add(spigotServer.getServerName());
		connection.sendData(container);
	}
	
	public boolean isMaintenance() {
		return maintenance;
	}
	
	public void setMaintenance(boolean maintenance) {
		this.maintenance = maintenance;
	}
	
	public List<UUID> getWhitelistedPlayers() {
		return whitelistedPlayers;
	}

}
