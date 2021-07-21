package eu.busycloud.service.infrastructure;

import java.util.ArrayList;
import java.util.List;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;

public class ServerGroup {

	private String groupName;
	private int groupID;
	private int maxRamEachServer;
	private ServerSoftware serverSoftware;
	private boolean isMain;
	private ProxyServer proxy;
	private List<SpigotServer> groupList = new ArrayList<>();

	private int defaultActiveServers;
	private int percentage;
	private int onlinePlayers;
	private int startPort;

	public ServerGroup(ServerSoftware serverSoftware, int startport, String groupName, int groupID, ProxyServer proxy,
			int maxRamEachServer, int defaultActiveServers, int percentage, boolean isLobby) {
		ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(this);
		CloudInstance.LOGGER.info("Starting ServerGroup (\"" + groupName + "\")");
		if (proxy == null) {
			if (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() == 0) {
				boolean did = false;
				for(ServerSoftware serverSoftware2 : ApplicationInterface.getAPI().getInfrastructure().serverSoftwares) {
					if(serverSoftware2.isAvailable() && serverSoftware2.getType() == ServerSoftwareType.PROXY) {
						int proxyID = ApplicationInterface.getAPI().getInfrastructure().startProxyServer(
								"Proxy-" + (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + 1),
								serverSoftware2);
						proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(proxyID);
						did = true;
						break;
					}
				}
				if(!did) {
					CloudInstance.LOGGER.info("Couldn't start servergroup " + groupName + " ~ no proxy installed.");
					CloudInstance.LOGGER.info("Install Proxy with '/install' otherwise get introduction with '/introduction'.");
					return;
				}
			} else {
				for(ProxyServer cProxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
					if(proxy == null)
						proxy = cProxy;
					if(cProxy.getOnlinePlayer().size() >= proxy.getOnlinePlayer().size())
						proxy = cProxy;
				}
			}
		}

		if(!serverSoftware.isAvailable()) {
			CloudInstance.LOGGER.info("ServerSoftware '" + serverSoftware.getVersionName() + "' not available. - install...");
			serverSoftware.download();
			CloudInstance.LOGGER.info("ServerSoftware-Install finished");
		}

		this.isMain = isLobby;
		this.serverSoftware = serverSoftware;
		this.startPort = startport;
		this.groupName = groupName;
		this.groupID = groupID;
		this.proxy = proxy;
		this.maxRamEachServer = maxRamEachServer;
		this.defaultActiveServers = defaultActiveServers;
		this.percentage = percentage;

		for (int i = 0; i < defaultActiveServers; i++, startport += 1)
			startServer(true, startport);
	}

	public void startServer(boolean acceptEula, int port) {
		int serverID = ApplicationInterface.getAPI().getInfrastructure().startSpigotServer(groupName, serverSoftware, 1, false,
				maxRamEachServer, acceptEula, port, isMain);
		SpigotServer server = ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverID);
		groupList.add(server);
	}
	
	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void stopServer(int serverID) {
		ApplicationInterface.getAPI().getInfrastructure().stopSpigotServer(serverID);
		groupList.remove(ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverID));
	}
	
	public void stopServers() {
		List<Integer> ids = new ArrayList<Integer>();
		for(SpigotServer server : getGroupList())
			ids.add(server.getId());
		for(Integer i : ids)
			ApplicationInterface.getAPI().getInfrastructure().stopSpigotServer(i);
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

	public ServerSoftware getServerSoftware() {
		return serverSoftware;
	}

	public void setVersion(ServerSoftware serverSoftware) {
		this.serverSoftware = serverSoftware;
	}

	public boolean isMain() {
		return isMain;
	}

	public void setMain(boolean isMain) {
		this.isMain = isMain;
	}

	public ProxyServer getProxy() {
		return proxy;
	}

	public void setProxy(ProxyServer proxy) {
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
