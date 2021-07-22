package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import de.terrarier.netlistening.Connection;
import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.PlayerInfo;
import eu.busycloud.service.infrastructure.generate.ProxyGenerator;
import eu.busycloud.service.utils.FileUtils;

public class ProxyServer {

	private Process instance;

	private String proxyName = "";
	private int proxyid = 0;
	private ServerSoftware software;
	private int port;

	private boolean maintenance = false;
	private List<UUID> whitelistedPlayers = new LinkedList<>();

	private Connection connection;
	private String registerKey;

	private List<SpigotServer> registeredServer = new LinkedList<>();
	private List<PlayerInfo> registeredPlayer = new LinkedList<>();

	public void startProxy(ServerSoftware software) {
		setRegisterKey("KEY_" + new Random().nextInt(Integer.MAX_VALUE));
		this.proxyid = new Random().nextInt(20000);
		this.port = new Random().nextInt(40000) + 20000;
		setProxyName("Proxy-" + getProxyid());
		setSoftware(software);
		new ProxyGenerator(this);

		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("java ");
		commandBuilder.append(
				"-XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxPermSize=256M -XX:-UseAdaptiveSizePolicy -XX:CompileThreshold=100 "
						+ "-Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 "
						+ "-Dio.netty.recycler.maxCapacity.default=0 "
						+ "-Djline.terminal=jline.UnsupportedTerminal -Xmx" + 512 + "M -jar "
						+ software.getVersionName() + ".jar");
		try {
			this.instance = Runtime.getRuntime().exec(commandBuilder.toString().split(" "), null,
					new File("server/" + ("temp") + "/" + this.getProxyName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stopProxy() {
		if (getConnection() != null) {
			if (getConnection().isConnected()) {
				getConnection().disconnect();
			}
		}
		CloudInstance.LOGGER.info("Stopping Proxy(\"Proxy-" + getProxyid() + " - localhost:" + port + "\")");
		if (instance != null)
			if (instance.isAlive())
				instance.destroyForcibly();

		FileUtils.deleteFolderRecursivly("server/" + ("temp") + "/" + this.getProxyName());
		new File("server/" + ("temp") + "/" + this.getProxyName()).delete();
	}

	public void addSpigotServer(SpigotServer spigotServer, boolean isMain) {

		registeredServer.add(spigotServer);

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				if (connection != null) {
					connection.sendData("registerserver", spigotServer.getServerName(), spigotServer.getPort(), isMain);
					cancel();
				}
			}
		}, 1000, 1000);

	}

	public void sendRCON(String command) {
		connection.sendData("rcon", command);
	}

	public void setSoftware(ServerSoftware software) {
		this.software = software;
	}

	public ServerSoftware getSoftware() {
		return software;
	}

	private void setRegisterKey(String registerKey) {
		this.registerKey = registerKey;
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
		if (!backendTemplates.exists()) {
			backendTemplates.mkdirs();
		} else if (backendTemplates.listFiles().length != 0) {
			FileUtils.deleteFolderRecursivly(backendTemplates.getPath());
		}
		try {
			FileUtils.copyFolder(serverFolder.toPath(), backendTemplates.toPath());
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public void removeSpigotServer(SpigotServer spigotServer) {
		connection.sendData("unregisterserver", spigotServer.getServerName());
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
