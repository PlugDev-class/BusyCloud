package eu.busycloud.service.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;
import eu.busycloud.service.utils.ServerGroupContainer;
import eu.busycloud.service.utils.SingleServerInstance;

public class ServerGroup {

	ServerGroupContainer serverGroupContainer = null;
	private List<SingleServerInstance> groupList = new ArrayList<>();
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
		UUID serverID = null;
		if (serverGroupContainer.getServerSoftware().getType() == ServerSoftwareType.BEDROCK)
			serverID = ApplicationInterface.getAPI().getInfrastructure().startNukkitServer(
					serverGroupContainer.getGroupName(), serverGroupContainer.getServerSoftware(), 1,
					serverGroupContainer.getMaxRamEachServer(), true,
					groupList.size() + serverGroupContainer.getStartPort(), serverGroupContainer.isLobbyState());
		if (serverGroupContainer.getServerSoftware().getType() == ServerSoftwareType.JAVA)
			serverID = ApplicationInterface.getAPI().getInfrastructure().startSpigotServer(
					serverGroupContainer.getGroupName(), serverGroupContainer.getServerSoftware(), 1,
					serverGroupContainer.getMaxRamEachServer(), true,
					groupList.size() + serverGroupContainer.getStartPort(), serverGroupContainer.isLobbyState());
		groupList.add(ApplicationInterface.getAPI().getInfrastructure().getServerById(serverID));
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void stopServer(UUID serverID) {
		ApplicationInterface.getAPI().getInfrastructure().stopServer(serverID);
		groupList.remove(ApplicationInterface.getAPI().getInfrastructure().getServerById(serverID));
	}

	public void stopServer(String serverName) {
		UUID uuid = ApplicationInterface.getAPI().getInfrastructure().getServerByName(serverName).getId();
		ApplicationInterface.getAPI().getInfrastructure().stopServer(uuid);
		groupList.remove(ApplicationInterface.getAPI().getInfrastructure().getServerById(uuid));
	}

	public void stopServers() {
		List<UUID> ids = new ArrayList<>();
		for (SingleServerInstance server : getGroupList())
			ids.add(server.getId());
		ids.forEach((id) -> {
			ApplicationInterface.getAPI().getInfrastructure().stopServer(id);
		});
		ids.clear();
	}

	public void rconGroup(String command) {
		groupList.forEach((server) -> {
			server.rconServer(command);
		});
	}

	public void rconServer(String serverName, String command) {
		ApplicationInterface.getAPI().getInfrastructure().rconServer(serverName, command);
	}

	public List<SingleServerInstance> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<SingleServerInstance> groupList) {
		this.groupList = groupList;
	}

	public ServerGroupContainer getServerGroupContainer() {
		return serverGroupContainer;
	}

}
