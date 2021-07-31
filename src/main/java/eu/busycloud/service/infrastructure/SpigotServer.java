package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.terrarier.netlistening.Connection;
import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;
import eu.busycloud.service.infrastructure.generate.SpigotGenerator;
import eu.busycloud.service.utils.FileUtils;
import eu.busycloud.service.utils.ParseUtils;
import eu.busycloud.service.utils.SingleServerInstance;

public class SpigotServer extends SingleServerInstance {

	public SpigotServer(String serverGroup, ServerSoftware serverSoftware, boolean eula, int maxRam, boolean isLobbyServer) {
		this.serverGroup = serverGroup;
		this.serverSoftware = serverSoftware;
		this.eula = eula;
		this.maxRam = maxRam;
		this.isLobbyServer = isLobbyServer;
		setStatic(false);
	}
	
	public SpigotServer(String serverName, ServerSoftware serverSoftware, boolean eula, int maxRam) {
		this.serverGroup = "Static";
		this.serverSoftware = serverSoftware;
		this.eula = eula;
		this.maxRam = maxRam;
		setStatic(true);
	}
	
	
	boolean did = false;
	@Override
	public void startServer() {
		this.id = UUID.randomUUID();
		super.setRegisterKey("KEY_" + new SecureRandom().nextInt(Integer.MAX_VALUE));
		if(isStatic) {
			startStaticServer();
			return;
		}
		setServerName(serverGroup + "#" + getId());
		new SpigotGenerator(this);
		
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get("configurations", "cloudconfig.json"))));
		} catch (JSONException | IOException exception) {
			CloudInstance.LOGGER.info("JVM-Startparameter has moved to cloudconfig.json.");
			CloudInstance.LOGGER.info("Please reinstall the cloud or download the latest cloudconfig.json from GitHub.");
			return;
		}
		
		JSONArray jvmParameter = jsonObject.getJSONArray("jvmStartparameter");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("java ");
		jvmParameter.toList().forEach((parameter) -> {
			stringBuilder.append("-" + ((String) parameter).replaceAll("%MAX%", maxRam + "")
					.replaceAll("%VERSION-IN-JAR%", serverSoftware.getVersionName()) + " ");
		});
		
		try {
			this.instance = Runtime.getRuntime().exec(stringBuilder.toString().split(" "), null,
					new File("server/temp/" + this.getServerName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(ProxyServer proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies())
			if(this.preferredProxy == null
					|| this.preferredProxy.getRegisteredServer().size() > proxy.getRegisteredServer().size())
				this.preferredProxy = proxy;
		if(this.preferredProxy == null)
			if(ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() == 0) {
				did = false;
				Arrays.asList(ApplicationInterface.getAPI().getInfrastructure().serverSoftwares).forEach((serverSoftware) -> {
					if(serverSoftware.isAvailable() && serverSoftware.getType() == ServerSoftwareType.PROXY) {
						int proxyId = ApplicationInterface.getAPI().getInfrastructure().startProxyServer("Proxy-" +
								(ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + 1), serverSoftware);
						this.preferredProxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(proxyId);
						this.preferredProxy.getRegisteredServer().add(this);
						did = true;
					}
				});
				if(!did) {
					CloudInstance.LOGGER.info("We couldn't find a proxy-environment. Please install a proxy by using the command: /install");
					CloudInstance.LOGGER.info("Do you need help with the setup? Type '/introduction' for some help.");
				}
			}
		did = false;
	}

	public void startStaticServer() {
		if (!new File("server/static/" + getServerName()).exists()) {
			new SpigotGenerator(this);
		} else {
			FileUtils.writeFile(new File("server/static/" + getServerName() + "/" + getRegisterKey()),
					getRegisterKey());
			FileUtils.copyFile(new File("backend/downloads/SpigotCloudBridge.jar").toPath(),
					new File("server/static/" + getServerName() + "/plugins/SpigotCloudBridge.jar").toPath());
		}

		try {
			Files.readAllLines(new File("server/static/" + getServerName() + "/server.properties").toPath()).forEach((string) -> {
				if(string.startsWith("server-port=")) {
					port = ParseUtils.parseInt(string.replaceAll("server-port=", ""));
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		CloudInstance.LOGGER.info("Starting SpigotServer(\"" + getServerName() + " - localhost:" + getPort() + "\")");
		
		JSONObject settingsFile = null;
		try {
			settingsFile = new JSONObject(new String(Files.readAllBytes(Paths.get("configurations", "cloudconfig.json"))));
		} catch (JSONException | IOException e1) {
			CloudInstance.LOGGER.info("JVM-Startparameter has moved to cloudconfig.json.");
			CloudInstance.LOGGER.info("Please reinstall the cloud or download the latest cloudconfig.json from GitHub.");
			return;
		}
		JSONArray jvmParameter = settingsFile.getJSONArray("jvmStartparameter");
		StringBuilder stringBuilder = new StringBuilder();
		jvmParameter.toList().forEach((object) -> {
			stringBuilder.append("-" + ((String) object).replaceAll("%MAX%", maxRam + "")
					.replaceAll("%VERSION-IN-JAR%", serverSoftware.getVersionName()) + " ");
		});
		
		try {
			this.instance = Runtime.getRuntime().exec(stringBuilder.toString().split(" "), null,
					new File("server/" + ("static") + "/" + this.getServerName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(ProxyServer proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies())
			if(this.preferredProxy == null
					|| this.preferredProxy.getRegisteredServer().size() > proxy.getRegisteredServer().size())
				this.preferredProxy = proxy;
		if(this.preferredProxy == null)
			if(ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() == 0) {
				did = false;
				Arrays.asList(ApplicationInterface.getAPI().getInfrastructure().serverSoftwares).forEach((serverSoftware) -> {
					if(serverSoftware.isAvailable() && serverSoftware.getType() == ServerSoftwareType.PROXY) {
						int proxyId = ApplicationInterface.getAPI().getInfrastructure().startProxyServer("Proxy-" +
								(ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + 1), serverSoftware);
						this.preferredProxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(proxyId);
						this.preferredProxy.getRegisteredServer().add(this);
						did = true;
					}
				});
				if(!did) {
					CloudInstance.LOGGER.info("We couldn't find a proxy-environment. Please install a proxy by using the command: /install");
					CloudInstance.LOGGER.info("Do you need help with the setup? Type '/introduction' for some help.");
				}
			}
		did = false;
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
			if (getConnection() != null) {
				CloudInstance.LOGGER.info("\tConnectionID: " + getConnection().getId());
				CloudInstance.LOGGER
						.info("\tRemoteAddress-Hostname: " + getConnection().getRemoteAddress().getHostName());
				CloudInstance.LOGGER
						.info("\tRemoteAddress-Hoststring: " + getConnection().getRemoteAddress().getHostString());
				CloudInstance.LOGGER.info("\tRemoteAddress-Port: " + getConnection().getRemoteAddress().getPort());
			} else {
				CloudInstance.LOGGER.info("\tConnectionID: " + null);
				CloudInstance.LOGGER
						.info("\tRemoteAddress-Hostname: " + null);
				CloudInstance.LOGGER
						.info("\tRemoteAddress-Hoststring: " + null);
				CloudInstance.LOGGER.info("\tRemoteAddress-Port: " + null);
			}
			CloudInstance.LOGGER.info("}");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public File getLatestLog() {
		return new File("server/" + ("temp") + "/" + this.getServerName() + "/logs/latest.log");
	}

	public void stopServer() {

		ServerGroup preferredGroup = null;
		for (ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
			if (group.getGroupList().contains(this)) {
				preferredGroup = group;
			}
		}

		if (preferredGroup != null) {
			preferredGroup.getGroupList().remove(this);
		}
		CloudInstance.LOGGER.info("Stopping SpigotServer(\"" + serverGroup + " - localhost:" + port + "\")");
		ApplicationInterface.getAPI().getInfrastructure().getRunningServers().remove(this);

		if (instance.isAlive())
			instance.destroyForcibly();

		if (preferredGroup != null)
			preferredGroup.getGroupList().remove(this);

		if (isStatic)
			return;

		FileUtils.deleteFolderRecursivly(new File("server/" + ("temp") + "/" + this.getServerName()).getPath());
		FileUtils.deleteFile("server/" + ("temp") + "/" + this.getServerName());

	}

	public void ping() {
		if (this.getConnection() != null) {
			CloudInstance.LOGGER.info("Send pingrequest to Spigotserver '" + getServerName() + "'");
			this.getConnection().sendData("ping", System.currentTimeMillis());
		} else {
			CloudInstance.LOGGER.info("Server " + serverName + " isn't linked.");
		}
	}

	@Override
	public void rconServer(String command) {
		if (getConnection() != null)
			if (getConnection().isConnected()) {
				connection.sendData("rcon", command);
				return;
			}
		CloudInstance.LOGGER.info("SpigotServer " + serverName + " isn't linked.");
	}

	public boolean doesAcceptEula() {
		return eula;
	}

	public Process getInstance() {
		return instance;
	}

	public ServerSoftware getServerSoftware() {
		return serverSoftware;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getRegisterKey() {
		return super.registerKey;
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
			CloudInstance.LOGGER.info("Template created.");
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

}
