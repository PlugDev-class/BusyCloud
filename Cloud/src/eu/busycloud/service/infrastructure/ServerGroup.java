package eu.busycloud.service.infrastructure;

import java.util.ArrayList;
import java.util.List;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.utils.ServerGroupContainer;

public class ServerGroup {

	ServerGroupContainer serverGroupContainer = null;
	private List<SpigotServer> groupList = new ArrayList<>();
	private int onlinePlayers;

	public ServerGroup(ServerGroupContainer serverGroupContainer) {
		this.serverGroupContainer = serverGroupContainer;
		CloudInstance.LOGGER.info("Starting ServerGroup (\"" + serverGroupContainer.getGroupName() + "\")");
		ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(this);
		if (serverGroupContainer.getProxyServer() == null)
			serverGroupContainer.startProxyCauseNotExists();
		if (!serverGroupContainer.getServerSoftware().isAvailable()) {
			CloudInstance.LOGGER.info("ServerSoftware '" + serverGroupContainer.getServerSoftware().getVersionName()
					+ "' not available. - install...");
			serverGroupContainer.getServerSoftware().download();
			CloudInstance.LOGGER.info("ServerSoftware-Install finished");
		}
		for (int i = 0; i < serverGroupContainer.getStartServerByGroupstart(); i++)
			startServer();
	}

	public void startServer() {
		int serverID = ApplicationInterface.getAPI().getInfrastructure().startSpigotServer(
				serverGroupContainer.getGroupName(), 
				serverGroupContainer.getServerSoftware(), 
				1, 
				false,
				serverGroupContainer.getMaxRamEachServer(), 
				true,
				groupList.size() + serverGroupContainer.getStartPort(), 
				serverGroupContainer.isLobbyState());
		groupList.add(ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverID));
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
		for (SpigotServer server : getGroupList())
			ids.add(server.getId());
		for (Integer i : ids)
			ApplicationInterface.getAPI().getInfrastructure().stopSpigotServer(i);
		ids.clear();
	}
	
	public void rconGroup(String command) {
		for (SpigotServer server : groupList) {
			server.sendRCON(command);
		}
	}

	public void rconServer(int serverID, String command) {
		ApplicationInterface.getAPI().getInfrastructure().rconSpigotServer(serverID, command);
	}

	public List<SpigotServer> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<SpigotServer> groupList) {
		this.groupList = groupList;
	}

	public ServerGroupContainer getServerGroupContainer() {
		return serverGroupContainer;
	}

}
