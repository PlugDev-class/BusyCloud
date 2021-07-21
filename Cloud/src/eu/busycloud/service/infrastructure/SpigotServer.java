package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import de.terrarier.netlistening.Connection;
import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;
import eu.busycloud.service.infrastructure.generate.SpigotGenerator;
import eu.busycloud.service.utils.FileUtils;

public class SpigotServer {

	private Process instance;

	private int id;
	private String serverName;
	private String serverGroup;
	private int port;
	private int proxyId;
	private int maxRam;
	private ServerSoftware serverSoftware;
	private boolean eula;
	private boolean isStatic;

	private Connection connection;
	private String registerKey;

	private ProxyServer prefferedProxy = null;
	private boolean isMain;

	public void startServer(String serverGroup, ServerSoftware serverSoftware, boolean eula, int maxRam,
			boolean isMain) {
		registerKey = "KEY_" + new Random().nextInt(Integer.MAX_VALUE);
		setServerSoftware(serverSoftware);
		setServerGroup(serverGroup != null ? serverGroup : "Ungrouped");
		this.id = new Random().nextInt(20000);
		this.eula = eula;
		this.isMain = isMain;
		setServerName(serverGroup + "#" + getId());

		new SpigotGenerator(this);

		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("java ");
		commandBuilder.append(
				"-XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxPermSize=256M -XX:-UseAdaptiveSizePolicy -XX:CompileThreshold=100 "
						+ "-Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 "
						+ "-Dio.netty.recycler.maxCapacity.default=0 "
						+ "-Djline.terminal=jline.UnsupportedTerminal -Xmx" + maxRam + "M -jar "
						+ this.serverSoftware.getVersionName() + ".jar");
		try {
			this.instance = Runtime.getRuntime().exec(commandBuilder.toString().split(" "), null,
					new File("server/" + ("temp") + "/" + this.getServerName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ProxyServer prefferedProxy = null;
		for (ProxyServer proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
			if (prefferedProxy == null
					|| prefferedProxy.getRegisteredServer().size() > proxy.getRegisteredServer().size()) {
				prefferedProxy = proxy;
			}
		}

		if (prefferedProxy == null) {

			if (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() == 0) {
				boolean did = false;
				for (ServerSoftware serverSoftware2 : ApplicationInterface.getAPI()
						.getInfrastructure().serverSoftwares) {
					if (serverSoftware2.isAvailable() && serverSoftware2.getType() == ServerSoftwareType.PROXY) {
						int proxyID = ApplicationInterface.getAPI().getInfrastructure().startProxyServer("Proxy-"
								+ (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + 1),
								serverSoftware2);
						prefferedProxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(proxyID);
						did = true;
						break;
					}
				}
				if (!did) {
					CloudInstance.LOGGER.info(
							"[Core] BusyCloud aren't able to start a proxy. Please install a proxy-version: /install");
				}
			}
		}

		prefferedProxy.addSpigotServer(this, isMain);
	}

	public File getLatestLog() {
		return new File("server/" + ("temp") + "/" + this.getServerName() + "/logs/latest.log");
	}

	public void startStaticServer(String serverName, ServerSoftware serverSoftware, boolean eula, int maxRam) {
		registerKey = "KEY_" + new Random().nextInt(Integer.MAX_VALUE);
		setServerSoftware(serverSoftware);
		setServerGroup("Static");
		this.id = new Random().nextInt(20000);
		this.eula = eula;
		setServerName(serverName);
		setStatic(true);

		if (!new File("server/static/" + getServerName()).exists()) {
			new SpigotGenerator(this);
		} else {
			FileUtils.writeFile(new File("server/static/" + getServerName() + "/" + getRegisterKey()),
					getRegisterKey());
			FileUtils.copyFile(new File("backend/downloads/SpigotCloudBridge.jar").toPath(),
					new File("server/static/" + getServerName() + "/plugins/SpigotCloudBridge.jar").toPath());
		}

		try {
			List<String> strings = Files
					.readAllLines(new File("server/static/" + getServerName() + "/server.properties").toPath());
			for (String string : strings) {
				if (string.startsWith("server-port=")) {
					port = Integer.parseInt(string.replaceAll("server-port=", ""));
					strings.clear();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		CloudInstance.LOGGER
				.info("[CORE] Starting SpigotServer(\"" + getServerName() + " - localhost:" + getPort() + "\")");

		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("java ");
		commandBuilder.append(
				"-XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxPermSize=256M -XX:-UseAdaptiveSizePolicy -XX:CompileThreshold=100 "
						+ "-Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 "
						+ "-Dio.netty.recycler.maxCapacity.default=0 "
						+ "-Djline.terminal=jline.UnsupportedTerminal -jar " + serverSoftware.getVersionName()
						+ ".jar");
		try {
			this.instance = Runtime.getRuntime().exec(commandBuilder.toString().split(" "), null,
					new File("server/" + ("static") + "/" + this.getServerName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ProxyServer prefferedProxy = null;
		for (ProxyServer proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
			if (prefferedProxy == null
					|| prefferedProxy.getRegisteredServer().size() > proxy.getRegisteredServer().size()) {
				prefferedProxy = proxy;
			}
		}

		if (prefferedProxy == null) {
			if (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() == 0) {
				boolean did = false;
				for (ServerSoftware serverSoftware2 : ApplicationInterface.getAPI()
						.getInfrastructure().serverSoftwares) {
					if (serverSoftware2.isAvailable() && serverSoftware2.getType() == ServerSoftwareType.PROXY) {
						int proxyID = ApplicationInterface.getAPI().getInfrastructure().startProxyServer("Proxy-"
								+ (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + 1),
								serverSoftware2);
						prefferedProxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(proxyID);
						did = true;
						break;
					}
				}
				if (!did) {
					CloudInstance.LOGGER.info(
							"[Core] Cloud aren't able to start a proxy. Please install a proxy-version: /install");
				}
			}
		}

		prefferedProxy.addSpigotServer(this, false);
	}

	public void printInfo() {
		try {
			CloudInstance.LOGGER.info("<=====================> " + getServerName() + " <=====================>");
			CloudInstance.LOGGER.info("General-Informations: {");
			CloudInstance.LOGGER.info("\tServergroup: " + getServerGroup());
			CloudInstance.LOGGER.info("\tServername: " + getServerName());
			CloudInstance.LOGGER.info("\tServerID: " + getId());
			CloudInstance.LOGGER.info("\tServerport: " + getPort());
			CloudInstance.LOGGER.info("\tProxyID: " + getProxyId());
			CloudInstance.LOGGER.info("\tConnectKey: " + getRegisterKey());
			CloudInstance.LOGGER.info("}");
			CloudInstance.LOGGER.info("Intern-Connection: {");
			CloudInstance.LOGGER.info("\tConnectionstate: " + (getConnection() != null));
			CloudInstance.LOGGER.info("\tConnectionID: " + getConnection().getId());
			CloudInstance.LOGGER.info("\tRemoteAddress-Hostname: " + getConnection().getRemoteAddress().getHostName());
			CloudInstance.LOGGER
					.info("\tRemoteAddress-Hoststring: " + getConnection().getRemoteAddress().getHostString());
			CloudInstance.LOGGER.info("\tRemoteAddress-Port: " + getConnection().getRemoteAddress().getPort());
			CloudInstance.LOGGER.info("}");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
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
		CloudInstance.LOGGER.info("[CORE] Stopping SpigotServer(\"" + serverGroup + " - localhost:" + port + "\")");
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

	public void ping() {
		if (this.getConnection() != null) {
			this.getConnection().sendData("ping", System.currentTimeMillis());
			CloudInstance.LOGGER.info("Sent pingrequest to Spigotserver '" + getServerName() + "'");
		} else {
			CloudInstance.LOGGER.info("Spigotserver isn't linked.");
		}
	}

	public void sendRCON(String command) {
		if (getConnection() != null)
			if (getConnection().isConnected()) {
				connection.sendData("rcon", command);
				return;
			}
		CloudInstance.LOGGER.info("Server " + serverName + " isn't linked.");
	}

	public boolean doesAcceptEula() {
		return eula;
	}

	public Process getInstance() {
		return instance;
	}

	public void setServerSoftware(ServerSoftware serverSoftware) {
		this.serverSoftware = serverSoftware;
	}

	public ServerSoftware getServerSoftware() {
		return serverSoftware;
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
			CloudInstance.LOGGER.info("[PLUGIN] Template created.");
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

}
