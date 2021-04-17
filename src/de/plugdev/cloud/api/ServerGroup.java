package de.plugdev.cloud.api;

import java.util.ArrayList;
import java.util.List;

import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.infrastructure.MinecraftVersion;
import de.plugdev.cloud.infrastructure.Proxy;
import de.plugdev.cloud.infrastructure.SpigotServer;

public class ServerGroup {

	private String groupName;
	private int groupID;

	private int maxRamEachServer;
	private MinecraftVersion version;

	private boolean isMain;

	private Proxy proxy;
	private List<SpigotServer> groupList = new ArrayList<>();

	private int defaultActiveServers;
	private int percentage;

	private int startPort;

	public ServerGroup(MinecraftVersion version, int startport, String groupName, int groupID, Proxy proxy,
			int maxRamEachServer, int defaultActiveServers, int percentage, boolean isMain) {

		if (!ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().contains(this)) {
			ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(this);
		}

		if (proxy == null) {
			if (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() == 0) {
				MinecraftVersion version2 = null;
				if (ApplicationInterface.getAPI().getInfrastructure().isValidVersion("BungeeCord")) {
					version2 = ApplicationInterface.getAPI().getInfrastructure().getVersionById("BungeeCord");
				} else if (ApplicationInterface.getAPI().getInfrastructure().isValidVersion("Waterfall")) {
					version2 = ApplicationInterface.getAPI().getInfrastructure().getVersionById("Waterfall");
				} else {
					ConsoleColors.write(ConsoleColors.BLUE,
							"No Proxy available. Maybe custom? Please provide to rename the custom jar to \"BungeeCord.jar\".");
					return;
				}

				int proxyID = ApplicationInterface.getAPI().getInfrastructure().startProxyServer(
						"Proxy-" + ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + 1,
						version2);
				proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(proxyID);
			}
		}

		this.isMain = isMain;
		this.version = version;
		this.startPort = startport;
		this.groupName = groupName;
		this.groupID = groupID;
		this.proxy = proxy;
		this.maxRamEachServer = maxRamEachServer;
		this.defaultActiveServers = defaultActiveServers;
		this.percentage = percentage;

		for (int i = 0; i < defaultActiveServers; i++) {
			startport += 1;
			startServer(true, startport);
		}
	}

	public void startServer(boolean acceptEula, int port) {
		int serverID = ApplicationInterface.getAPI().getInfrastructure().startSpigotServer(groupName, version, 1, false,
				maxRamEachServer, acceptEula, port, isMain);
		SpigotServer server = ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverID);
		groupList.add(server);
	}

	public void stopServer(int serverID) {
		ApplicationInterface.getAPI().getInfrastructure().stopSpigotServer(serverID);
		groupList.remove(ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverID));
	}

	public void rconGroup(String command) {
		for (SpigotServer server : groupList) {
			server.sendRCON(command);
		}
	}

	public void rconServer(int serverID, String command) {
		ApplicationInterface.getAPI().getInfrastructure().rconSpigotServer(serverID, command);
	}

	public String getGroupName() {
		return groupName;
	}

	public int getGroupID() {
		return groupID;
	}

	public int getMaxRamEachServer() {
		return maxRamEachServer;
	}

	public void setMaxRamEachServer(int maxRamEachServer) {
		this.maxRamEachServer = maxRamEachServer;
	}

	public MinecraftVersion getVersion() {
		return version;
	}

	public void setVersion(MinecraftVersion version) {
		this.version = version;
	}

	public boolean isMain() {
		return isMain;
	}

	public void setMain(boolean isMain) {
		this.isMain = isMain;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public List<SpigotServer> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<SpigotServer> groupList) {
		this.groupList = groupList;
	}

	public int getDefaultActiveServers() {
		return defaultActiveServers;
	}

	public void setDefaultActiveServers(int defaultActiveServers) {
		this.defaultActiveServers = defaultActiveServers;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public int getStartPort() {
		return startPort;
	}

	public void setStartPort(int startPort) {
		this.startPort = startPort;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

}
