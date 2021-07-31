package eu.busycloud.service.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.terrarier.netlistening.Connection;
import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.api.PlayerInfo;
import eu.busycloud.service.infrastructure.generate.ProxyGenerator;
import eu.busycloud.service.utils.FileUtils;
import eu.busycloud.service.utils.SingleServerInstance;

public class ProxyServer {

	private Process instance;

	private String proxyName = "";
	
	private int maxPlayers;
	private int maxRam;
	private int proxyid = 0;
	private ServerSoftware software;
	private int port;

	private boolean maintenance = false;
	private List<UUID> whitelistedPlayers = new LinkedList<>();

	private Connection connection;
	private String registerKey;

	private List<SingleServerInstance> registeredServers = new LinkedList<>();
	private Collection<PlayerInfo> registeredPlayers = new ArrayList<>();

	public void startProxy(ServerSoftware software) {
		setRegisterKey("KEY_" + new Random().nextInt(Integer.MAX_VALUE));
		this.proxyid = ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size()+1;
		setProxyName("Proxy-" + getProxyid());
		setSoftware(software);
		
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get("configurations", "cloudconfig.json")), "UTF-8"));
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
		maxRam = jsonObject.getJSONObject("bungeeCord").getInt("maxRam");
		this.port = jsonObject.getJSONObject("bungeeCord").getInt("startport") + ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size();
		new ProxyGenerator(this);

		JSONArray jvmParameter = jsonObject.getJSONArray("jvmStartparameter");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("java ");
		for(Object string : jvmParameter.toList()) {
			stringBuilder.append("-" + replace((String) string) + " ");
		}
		
		try {
			this.instance = Runtime.getRuntime().exec(stringBuilder.toString().split(" "), null,
					new File("server/" + ("temp") + "/" + this.getProxyName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stopProxy() {
		if (getConnection() != null)
			if (getConnection().isConnected())
				getConnection().disconnect();
		CloudInstance.LOGGER.info("Stopping Proxy(\"Proxy-" + getProxyid() + " - localhost:" + port + "\")");
		if (instance != null)
			if (instance.isAlive())
				instance.destroyForcibly();

		FileUtils.deleteFolderRecursivly("server/" + ("temp") + "/" + this.getProxyName());
		new File("server/" + ("temp") + "/" + this.getProxyName()).delete();
	}

	public void addSpigotServer(SpigotServer spigotServer, boolean isMain) {

		registeredServers.add(spigotServer);

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
	
	public void addNukkitServer(NukkitServer nukkitServer, boolean isMain) {
		
	}

	public void sendRCON(String command) {
		if (getConnection() != null)
			if (getConnection().isConnected()) {
				connection.sendData("rcon", command);
				return;
			}
		CloudInstance.LOGGER.info("ProxyServer " + getProxyName() + " isn't linked.");
	}
	
	public int getMaxRam() {
		return maxRam;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public void setMaxRam(int maxRam) {
		this.maxRam = maxRam;
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

	public List<SingleServerInstance> getRegisteredServer() {
		return registeredServers;
	}

	public Collection<PlayerInfo> getOnlinePlayers() {
		return registeredPlayers;
	}
	
	public void setPort(int port) {
		this.port = port;
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
	
	private String replace(String input) {
		return input.replaceAll("%MAX%", String.valueOf(maxRam)).replaceAll("%VERSION-IN-JAR%", software.getVersionName());
	}

	public void printInfo() {
		try {
			CloudInstance.LOGGER.info("<=====================> " + getProxyName() + " <=====================>");
			CloudInstance.LOGGER.info("General-Informations: {");
			CloudInstance.LOGGER.info("\tServergroup: PROXY");
			CloudInstance.LOGGER.info("\tServername: " + getProxyName());
			CloudInstance.LOGGER.info("\tServerID: " + getProxyid());
			CloudInstance.LOGGER.info("\tServerport: " + getPort());
			CloudInstance.LOGGER.info("\tConnected Servers: " + getRegisteredServer().size());
			CloudInstance.LOGGER.info("\tConnectKey: " + getKey());
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

}
